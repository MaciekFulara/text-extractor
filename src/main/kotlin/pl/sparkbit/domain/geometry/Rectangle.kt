package pl.sparkbit.domain.geometry

data class Rectangle(val topLeft: Point, val height: Int, val width: Int) {
    val topRight = topLeft.plusX(width)
    val bottomRight = topLeft.plusX(width).plusY(height)
    val bottomLeft = topLeft.plusY(height)

    fun distance(other: Rectangle, axis: Axis): Int =
            TODO()
//        when(axis) {
//            Axis.HORIZONTAL ->
//            Axis.VERTICAL ->
//        }
}