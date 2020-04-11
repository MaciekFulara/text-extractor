package pl.sparkbit.service.grouper.heuristic

import org.springframework.stereotype.Service
import pl.sparkbit.domain.Region
import pl.sparkbit.domain.geometry.Axis

typealias Heuristic = (r1: Region, r: Region) -> Boolean

private object Ops {
    val SAME_FONT: Heuristic =
        { r1, r2 -> r1.data.font == r2.data.font }

    val SAME_COLOR: Heuristic =
        { r1, r2 -> r1.data.color == r2.data.color }

    val DISTANCE_AT_MOST: (distance: Int, axis: Axis) -> Heuristic = { distance, axis ->
        { r1, r2 -> r1.distance(r2, axis) <= distance }
    }
}

infix fun Heuristic.and(other: Heuristic): Heuristic =
    { r1, r2 -> this(r1, r2) and other(r1, r2) }

infix fun Heuristic.or(other: Heuristic): Heuristic =
    { r1, r2 -> this(r1, r2) || other(r1, r2) }


interface GroupingHeuristics {
    val isInSameVerticalGroup: Heuristic
    val isInSameHorizontalGroup: Heuristic
}

@Service
class GroupingHeuristicImpl : GroupingHeuristics {

    private companion object {
        const val VERTICAL_DISTANCE_SAME_FONT_SAME_COLOR = 7
        const val VERTICAL_DISTANCE_SAME_FONT = 10
        const val VERTICAL_DISTANCE = 15
        const val HORIZONTAL_DISTANCE = 6
    }

    override val isInSameVerticalGroup = {
        val verticalSameFontSameColor = Ops.SAME_FONT and Ops.SAME_COLOR and Ops.DISTANCE_AT_MOST(
            VERTICAL_DISTANCE_SAME_FONT_SAME_COLOR, Axis.VERTICAL
        )
        val verticalSameFont = Ops.SAME_FONT and Ops.DISTANCE_AT_MOST(VERTICAL_DISTANCE_SAME_FONT, Axis.VERTICAL)
        val verticalDefault = Ops.DISTANCE_AT_MOST(VERTICAL_DISTANCE, Axis.VERTICAL)

        verticalSameFontSameColor or verticalSameFont or verticalDefault
    }()

    override val isInSameHorizontalGroup = Ops.SAME_FONT and Ops.DISTANCE_AT_MOST(HORIZONTAL_DISTANCE, Axis.HORIZONTAL)

}