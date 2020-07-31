package shinobi9.krcon.protocol

import kotlinx.atomicfu.atomic


interface IdGenerator {
    fun generateId(): Int
}

class AtomicIntIdGenerator : IdGenerator {
    private val atomicInt = atomic(1)
    override fun generateId(): Int = atomicInt.getAndIncrement()
}