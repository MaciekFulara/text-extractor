package pl.sparkbit.service.grouper

import org.springframework.stereotype.Service
import pl.sparkbit.Utils
import pl.sparkbit.domain.Region
import pl.sparkbit.service.grouper.heuristic.GroupingHeuristics
import pl.sparkbit.service.grouper.index.Direction
import pl.sparkbit.service.grouper.index.RegionIndex
import pl.sparkbit.service.grouper.index.RegionIndexProvider
import pl.sparkbit.service.grouper.unionfind.RegionUnionFind
import pl.sparkbit.service.grouper.unionfind.RegionUnionFindProvider

interface RegionGrouper {
    fun group(regions: Set<Region>): Collection<Collection<Region>>
}

@Service
class RegionGrouperImpl(
    private val regionIndexProvider: RegionIndexProvider,
    private val regionUnionFindProvider: RegionUnionFindProvider,
    private val heuristics: GroupingHeuristics): RegionGrouper {

    companion object {
        const val VERTICAL_DISTANCE = 20
        const val TOLERANCE = 5
    }

    override fun group(regions: Set<Region>): Collection<Collection<Region>> {
        val index = regionIndexProvider.buildIndex(regions)
        val unionFind = regionUnionFindProvider.buildRegionUnionFind(regions)

        for (region in regions) {
            unionRegionWithNeighbours(region, index, unionFind)
        }

        return unionFind.disjointSets
    }

    private fun unionRegionWithNeighbours(region: Region, index: RegionIndex, unionFind: RegionUnionFind) {
        val verticalNeighbours = findVerticalNeighbours(region, index)
        val horizontalNeighbours = findHorizontalNeighbours(region, index)
        val neighbours = Utils.mergeIntoOneSet(verticalNeighbours, horizontalNeighbours)

        for (neighbour in neighbours) {
            unionFind.union(region, neighbour)
        }
    }

    private fun findVerticalNeighbours(region: Region, index: RegionIndex): Collection<Region> {
        val candidatesAbove = index.find(region, Direction.ABOVE, VERTICAL_DISTANCE, TOLERANCE)
        val candidatesBelow = index.find(region, Direction.BELOW, VERTICAL_DISTANCE, TOLERANCE)
        val verticalCandidates = Utils.mergeIntoOneSet(candidatesAbove, candidatesBelow)
        return verticalCandidates.filter { candidate -> heuristics.isInSameVerticalGroup(region, candidate) }
    }

    private fun findHorizontalNeighbours(region: Region, index: RegionIndex): Collection<Region> {
        val candidatesLeft = index.find(region, Direction.LEFT, region.data.fontSize, TOLERANCE)
        val candidatesRight = index.find(region, Direction.RIGHT, region.data.fontSize, TOLERANCE)
        val horizontalCandidates = Utils.mergeIntoOneSet(candidatesLeft, candidatesRight)
        return horizontalCandidates.filter { candidate -> heuristics.isInSameHorizontalGroup(region, candidate) }
    }
}