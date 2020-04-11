package pl.sparkbit.service.grouper.index

import pl.sparkbit.domain.Region
import pl.sparkbit.domain.geometry.Point
import pl.sparkbit.domain.geometry.Rectangle

interface RegionIndex {
    fun find(region: Region, direction: Direction, distance: Int, tolerance: Int): Set<Region>
}

class RegionIndexImp(indexProvider: IndexProvider, regions: Set<Region>) : RegionIndex {

    private val index: Index<IndexPoint>

    init {
        val points = regions.flatMap { it.toIndexPoints() }
        index = indexProvider.buildIndex(points)
    }

    override fun find(region: Region, direction: Direction, distance: Int, tolerance: Int): Set<Region> {
        val searchArea = calculateSearchArea(region, direction, distance, tolerance)
        val foundPoints = index.findInside(searchArea)
        return pointSetToRegionSet(foundPoints)
    }

    private fun calculateSearchArea(region: Region, direction: Direction, distance: Int, tolerance: Int): Rectangle =
        when (direction) {
            Direction.ABOVE -> Rectangle(
                topLeft = region.boundingBox.topLeft.plusY(-distance).plusX(-tolerance),
                height = distance,
                width = region.boundingBox.width + 2 * tolerance
            )
            Direction.BELOW -> Rectangle(
                topLeft = region.boundingBox.bottomLeft.plusX(-tolerance),
                height = distance,
                width = region.boundingBox.width + 2 * tolerance
            )
            Direction.LEFT -> Rectangle(
                topLeft = region.boundingBox.topLeft.plusY(-tolerance).plusX(-distance),
                height = region.boundingBox.height + 2 * tolerance,
                width = distance
            )
            Direction.RIGHT -> Rectangle(
                topLeft = region.boundingBox.topRight.plusY(-tolerance),
                height = region.boundingBox.height + 2 * tolerance,
                width = distance
            )
        }

    private fun pointSetToRegionSet(points: Set<IndexPoint>): Set<Region> =
        points.map { it.region }.toSet()

}

enum class Direction(val horizontal: Boolean) {
    ABOVE(true), BELOW(true), LEFT(false), RIGHT(false)
}

//not used at the moment
private enum class PointLocation {
    TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
}

private data class IndexPoint(override val point: Point, val location: PointLocation, val region: Region) : Indexable

private fun Region.toIndexPoints(): Set<IndexPoint> = setOf(
    IndexPoint(boundingBox.topLeft, PointLocation.TOP_LEFT, this),
    IndexPoint(boundingBox.topRight, PointLocation.TOP_RIGHT, this),
    IndexPoint(boundingBox.bottomRight, PointLocation.BOTTOM_RIGHT, this),
    IndexPoint(boundingBox.bottomLeft, PointLocation.BOTTOM_LEFT, this)
)
