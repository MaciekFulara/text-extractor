package pl.sparkbit.service.grouper.unionfind

interface UnionFind {
    val disjointSets: Collection<Set<Int>>
    fun find(el: Int): Int
    fun union(el1: Int, el2: Int)
}

class UnionFindImpl(size: Int) : UnionFind {

    private val elements = IntArray(size)
    private val ranks = IntArray(size)

    init {
        for (i in 0.until(size)) {
            elements[i] = i
            ranks[i] = 0
        }
    }

    override val disjointSets: Collection<Set<Int>>
        get() = {
            val elementsByRepresentative = mutableMapOf<Int, MutableSet<Int>>()
            for (el in elements) {
                val representative = find(el)
                val elementsInGroup = elementsByRepresentative.computeIfAbsent(representative) { mutableSetOf() }
                elementsInGroup.add(el)
            }
            elementsByRepresentative.values
        }()

    override fun find(el: Int): Int =
        compressPathAndFind(el, mutableListOf())

    override fun union(el1: Int, el2: Int) {
        val repr1 = find(el1)
        val repr2 = find(el2)

        val rank1 = ranks[repr1]
        val rank2 = ranks[repr2]

        if (rank1 < rank2) {
            elements[repr1] = repr2
            ranks[repr2]++
        } else {
            elements[repr2] = repr1
            ranks[repr1]++
        }
    }

    private fun compressPathAndFind(el: Int, path: MutableCollection<Int>): Int {
        val parent = elements[el]
        return if (parent == el) {
            path.forEach { elements[it] = parent }
            parent
        } else {
            path.add(el)
            compressPathAndFind(parent, path)
        }
    }

}