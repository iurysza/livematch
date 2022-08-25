package dev.iurysouza.livematch.data.models.cloned

import dev.iurysouza.livematch.data.models.cloned.models.base.CommentData

class CommentDataIterable : Iterable<CommentData> {

    constructor(list: Collection<CommentData>) {
        array = list.toTypedArray()
    }

    constructor(a: Array<CommentData>) {
        array = a
    }

    private val array: Array<CommentData>

    override fun iterator(): Iterator<CommentData> {
        return CommentDataIterator(array)
    }
}

fun Collection<CommentData>.treeIterable(): Iterable<CommentData> {
    return CommentDataIterable(this)
}

fun Array<CommentData>.treeIterable(): Iterable<CommentData> {
    return CommentDataIterable(this)
}