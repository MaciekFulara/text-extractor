package pl.sparkbit.service.grouper.unionfind

import org.springframework.stereotype.Service
import pl.sparkbit.domain.Region


interface RegionUnionFindProvider {
    fun buildRegionUnionFind(regions: Collection<Region>): RegionUnionFind
}

@Service
class RegionUnionFindProviderImpl(private val unionFindProvider: UnionFindProvider): RegionUnionFindProvider {

    override fun buildRegionUnionFind(regions: Collection<Region>) =
        RegionUnionFindImpl(regions, unionFindProvider)

}