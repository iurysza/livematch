package dev.iurysouza.livematch.data.models.cloned

import dev.iurysouza.livematch.data.models.cloned.models.base.CommentData
import java.util.ArrayDeque

class CommentDataIterator : Iterator<CommentData> {

    constructor(list: Collection<CommentData>) {
        stack.addAll(list)
    }

    constructor(array: Array<CommentData>) {
        stack.addAll(array)
    }

    private val stack = ArrayDeque<CommentData>()

    override fun hasNext(): Boolean {
        return stack.isNotEmpty()
    }

    override fun next(): CommentData {

        if (!hasNext()) {
            throw NoSuchElementException()
        }

        val item = stack.pop()
        if (item.hasReplies) {

            item.replies
                ?.asReversed()
                ?.forEach { stack.push(it) }
        }

        return item
    }
}

fun Collection<CommentData>.treeIterator(): Iterator<CommentData> {
    return CommentDataIterator(this)
}

fun Array<CommentData>.treeIterator(): Iterator<CommentData> {
    return CommentDataIterator(this)
}
