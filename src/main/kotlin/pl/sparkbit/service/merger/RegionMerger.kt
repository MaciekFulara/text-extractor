package pl.sparkbit.service.merger

import org.springframework.stereotype.Service
import pl.sparkbit.domain.Region

interface RegionMerger {
    fun merge(groupedRegions: Collection<Collection<Region>>): Collection<Region>
}

@Service
class RegionMergerImpl: RegionMerger {

    override fun merge(groupedRegions: Collection<Collection<Region>>): Collection<Region> =
        groupedRegions.map { merge(it) }

    private fun merge(regions: Collection<Region>): Region {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}