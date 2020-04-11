package pl.sparkbit.domain.geometry

data class Point(val x: Int, val y: Int) {
    fun plusX(delta: Int) = Point(x + delta, y)
    fun plusY(delta: Int) = Point(x, y + delta)
}