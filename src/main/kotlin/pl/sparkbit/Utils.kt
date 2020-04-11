package pl.sparkbit

object Utils {
    fun <T> mergeIntoOneSet(collections: Iterable<Iterable<T>>): Set<T> =
        collections.fold(setOf()) { acc, col -> acc + col }

    fun <T> mergeIntoOneSet(vararg collections: Iterable<T>): Set<T> = mergeIntoOneSet(collections.asIterable())

    fun <T> mergeIntoOneSet(collections: Sequence<Iterable<T>>): Set<T> = mergeIntoOneSet(collections.asIterable())

}