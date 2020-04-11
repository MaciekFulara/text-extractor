package pl.sparkbit.domain.geometry

import kotlin.math.max
import kotlin.math.min

object Geometry {
    fun enumeratePointsBetween(p1: Point, p2: Point): Sequence<Point> {
        val minX = min(p1.x, p2.x)
        val maxX = max(p1.x, p2.x)
        val minY = min(p1.y, p2.y)
        val maxY = max(p1.y, p2.y)
        return sequence {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    yield(Point(x, y))
                }
            }
        }
    }
}