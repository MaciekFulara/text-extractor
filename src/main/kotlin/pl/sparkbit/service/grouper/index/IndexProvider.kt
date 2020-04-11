package pl.sparkbit.service.grouper.index

import org.springframework.stereotype.Service

interface IndexProvider {
    fun <T : Indexable> buildIndex(indexables: Collection<T>): Index<T>
}

@Service
class GridIndexProvider : IndexProvider {

    private companion object {
        const val GRID_SIZE = 300
    }

    override fun <T : Indexable> buildIndex(indexables: Collection<T>): Index<T> =
        GridIndexImp<T>(GRID_SIZE, indexables)
}