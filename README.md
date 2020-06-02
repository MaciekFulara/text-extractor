
This is a sketch of ideas for a pdf to html conversion project.
It was created to help me think about the algorithm and the code layout.
It is not a proof of concept, only a sketch, so there are unimplemented methods in places that didn't seem to me to be relevant.
Also, some implementation details may not be correct - I wrote the code over a weekend and my objective was to sketch out the ideas and a reasonable code layout.


The project is for a company selling subscriptions for major Polish newspapers on the internet.
They get the content as PDFs with text layer and have to transform it into html.
At the moment they use manual labour and they want to automate the process.

The input of our text extractor is a PDF file with text layer.
From the text layer we can get a stream of words with bounding boxes and some metadata, like font, font size, color, etc.
We can not assume the words are in any particular order, the ordering depends on the ordering of instructions in PDF.
In order to produce html we have to divide the words into articles and order them within articles.
The challenging part is that articles in newspapers can have varying layouts - you can have multiple columns, you can have multiple
articles side by side, you can have text flowing around figures in tabloids etc.

One approach would be to use deep learning. To find the next word in article we could look for the next most likely word based on the
words we already have in the article.

Another approach is to ignore the meaning of the words and look for heuristics based solely on the layout, that is to only look at 
the bounding boxes around words and the metadata.
Ie. if two words are within a given distance vertically we can assume they are next lines in the same article, a change in font could indicate a different region (maybe a header)

By looking at major newspapers and trying to verbally describe what it means for two words to be within the same article we decided that hardcoded heuristics should work reasonably well.

High level view of the algorithm is:
- RegionSource produces a Set of Regions. A Region is a bounding box with text and metadata. This is what we can obtain from PDF with text layer, so the implementation will only wrap an existing PDF library and get a stream of Regions
- RegionGrouper groups the region in collections corresponding to articles. After this step an article is a bag of regions (no ordering yet). This is the difficult part
- RegionMerger merges the regions within each of the articles. Not implemented in this sketch. The implementation is not interesting, once you have the set of Regions for an article you have to sort them and merge them going from top to bottom and from left to right.
- Extractor is the entry point for the algorithm. It uses RegionSource, RegionGrouper, RegionMerger to go from unordered set of regions to a set of articles

The objective of this PoC was to sketch out what happens in RegionGrouper.

High level view of the algorithm grouping regions into articles.
- iterate over all Regions
- for each Region find all the Regions within a predefined vertical and horizontal distance (ie. Regions that could be on the next/previous line or preceding/following Regions on the same line)
- for each Region found in the point above (you can think of the Region as a candidate neighbour in the article) decide if the Region is in the same article based on some heuristics
- ensure all the regions from point above are in the same article

Algorithm details:
- efficiently finding Regions within a distance from a given Region requires that we maintain some sort of index for the Regions (their bounding boxes). We could use some sort of KD tree or R tree, but the bounding boxes are uniformly distributed (when you look
at a newspaper the words are more or less uniformly distributed on the page) so a simple grid will also work fine (I divide the space into cells, each corner falls into some cell, if we know the vertical/horizontal radius we want to search and we know the dimensions of a cell
we can quickly find the cells that we're interested in). The corresponding classes are GridIndex and RegionIndex.
- heuristics - sample (silly) heuristics implementetions are in GroupingHeuristics
- ensuring regions matching heuristics are in the same article (Set of Regions). UnionFind and RegionUnionFind. RegionUnionFind works with Regions, it wraps/delegates to UnionFind (works with ints)


Other
- *Provider classes. Those are stateless Spring services that produce stateful objects. Ie. RegionIndex - stateful (because it's an index ;)) and RegionIndexProvider - a stateless class that knows how to created a RegionIndex and can be injected into other services.




