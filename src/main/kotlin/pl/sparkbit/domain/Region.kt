package pl.sparkbit.domain

import pl.sparkbit.domain.geometry.Axis
import pl.sparkbit.domain.geometry.Point
import pl.sparkbit.domain.geometry.Rectangle

data class Region(val topLeft: Point, val height: Int, val width: Int, val data: RegionData) {
    val boundingBox = Rectangle(topLeft, height, width)
    fun distance(other: Region, axis: Axis) = boundingBox.distance(other.boundingBox, axis)
}