package pl.sparkbit.service.grouper.unionfind

import org.springframework.stereotype.Service

interface UnionFindProvider {
    fun buildUnionFind(size: Int): UnionFind
}

@Service
class UnionFindProviderImpl: UnionFindProvider {
    override fun buildUnionFind(size: Int): UnionFind = UnionFindImpl(size)
}