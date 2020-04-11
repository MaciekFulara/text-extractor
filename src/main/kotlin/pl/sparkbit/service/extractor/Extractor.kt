package pl.sparkbit.service.extractor

import org.springframework.stereotype.Service
import pl.sparkbit.domain.Region
import pl.sparkbit.service.grouper.RegionGrouper
import pl.sparkbit.service.merger.RegionMerger
import pl.sparkbit.service.source.RegionSource

interface Extractor {
    fun extract(pdf: Any): Collection<Region>
}

@Service
class ExtractorImpl(
    private val regionSource: RegionSource,
    private val regionGrouper: RegionGrouper,
    private val regionMerger: RegionMerger
) : Extractor {

    //THIS IS THE ENTRY POINT
    override fun extract(pdf: Any): Collection<Region> {
        val regions = regionSource.parsePdf(pdf)
        val groupedRegions = regionGrouper.group(regions)
        return regionMerger.merge(groupedRegions)
    }

}