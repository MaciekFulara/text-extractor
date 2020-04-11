package pl.sparkbit.service.grouper.index

import pl.sparkbit.Utils
import pl.sparkbit.domain.geometry.Geometry
import pl.sparkbit.domain.geometry.Point
import pl.sparkbit.domain.geometry.Rectangle
import kotlin.math.floor
import kotlin.math.roundToInt

interface Index<T: Indexable> {
    fun findInside(rectangle: Rectangle): Set<T>
}

class GridIndexImp<T: Indexable>(gridSize: Int, indexables: Collection<T>): Index<T> {
    private val buckets: Array<Array<MutableSet<T>>>
    private val bucketHeight: Double
    private val bucketWidth: Double

    init {
        //todo this loses points with x == maxX or y == maxY
        val (maxX, maxY) = getMaxXMaxY(indexables)
        bucketHeight = maxY.toDouble() / gridSize
        bucketWidth = maxX.toDouble() / gridSize

        buckets = createEmptyBuckets(gridSize)
        insert(indexables)
    }

    override fun findInside(rectangle: Rectangle): Set<T> {
        val topLeftBucketCoords = getBucketCoordinates(rectangle.topLeft)
        val bottomRightBucketCoords = getBucketCoordinates(rectangle.bottomRight)

        val allBucketCoords = Geometry.enumeratePointsBetween(topLeftBucketCoords, bottomRightBucketCoords)
        val allBuckets = allBucketCoords.map { getBucket(it) }

        return Utils.mergeIntoOneSet(allBuckets)
    }

    private fun getBucketCoordinates(point: Point): Point =
        Point(floor(point.x / bucketHeight).roundToInt(),
            floor(point.y / bucketWidth).roundToInt())

    private fun getBucket(point: Point): MutableSet<T> {
        val coords = getBucketCoordinates(point)
        return buckets[coords.x][coords.y]
    }

    private fun createEmptyBuckets(gridSize: Int): Array<Array<MutableSet<T>>> {
        val index = arrayOf<Array<MutableSet<T>>>()
        for (i in 0 until gridSize) {
            for (k in 0 until gridSize) {
                index[i][k] = mutableSetOf()
            }
        }
        return index
    }

    private fun insert(indexables: Collection<T>) {
        for (indexable in indexables) {
            val bucket = getBucket(indexable.point)
            bucket.add(indexable)
        }
    }

    private fun getMaxXMaxY(indexables: Collection<Indexable>): Pair<Int, Int> {
        var maxX = 0
        var maxY = 0
        for (indexable in indexables) {
            maxX = maxOf(maxX, indexable.point.x)
            maxY = maxOf(maxY, indexable.point.y)
        }
        return Pair(maxX, maxY)
    }

}

interface Indexable {
    val point: Point
}
