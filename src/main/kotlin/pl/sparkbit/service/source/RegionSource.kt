package pl.sparkbit.service.source

import org.springframework.stereotype.Service
import pl.sparkbit.domain.Region

interface RegionSource {
    fun parsePdf(pdf: Any): Set<Region>
}

@Service
class RegionSourceImpl: RegionSource {
    override fun parsePdf(pdf: Any): Set<Region> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}