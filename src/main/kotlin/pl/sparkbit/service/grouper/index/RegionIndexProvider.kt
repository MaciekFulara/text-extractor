package pl.sparkbit.service.grouper.index

import org.springframework.stereotype.Service
import pl.sparkbit.domain.Region

interface RegionIndexProvider {
    fun buildIndex(regions: Set<Region>): RegionIndex
}

@Service
class RegionIndexProviderImpl(private val indexProvider: IndexProvider): RegionIndexProvider {
    override fun buildIndex(regions: Set<Region>): RegionIndex {
        return RegionIndexImp(indexProvider, regions)
    }

}