package pl.sparkbit.service.grouper.unionfind

import pl.sparkbit.domain.ExtractorException
import pl.sparkbit.domain.Region

interface RegionUnionFind {
    fun union(r1: Region, r2: Region)
    val disjointSets: Collection<Collection<Region>>
}

class RegionUnionFindImpl(regions: Collection<Region>, unionFindProvider: UnionFindProvider): RegionUnionFind {
    private val regionToIdx: Map<Region, Int>
    private val idxToRegion: Map<Int, Region>
    private val unionFind: UnionFind

    init {
        regionToIdx = regions
            .mapIndexed { idx, region -> region to idx }
            .toMap()
        idxToRegion = regions
            .mapIndexed { idx, region -> idx to region }
            .toMap()
        unionFind = unionFindProvider.buildUnionFind(regions.size)
    }

    override fun union(r1: Region, r2: Region) {
        val idx1 = validateAndGetIndex(r1)
        val idx2 = validateAndGetIndex(r2)
        unionFind.union(idx1, idx2)
    }

    override val disjointSets: Collection<Set<Region>>
        get() = unionFind.disjointSets.map { set ->
            set.map { validateAndGetRegion(it) }.toSet()
        }

    private fun validateAndGetIndex(region: Region): Int =
        regionToIdx.get(region) ?: throw ExtractorException("Region not part of union find ${region}")

    private fun validateAndGetRegion(idx: Int): Region =
        idxToRegion.get(idx) ?: throw ExtractorException("Unknown index ${idx}")


}