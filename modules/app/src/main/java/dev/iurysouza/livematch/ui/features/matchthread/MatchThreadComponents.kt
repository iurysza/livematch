package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.iurysouza.livematch.ui.features.matchlist.CommentItem

@Composable
fun MatchDescription(
    htmlDescription: String,
) {
    val modifier = Modifier

    RichText(
        modifier = modifier.padding(16.dp)
    ) {
        Markdown(htmlDescription)
    }
}

@Composable
fun CommentList(
    commentList: List<CommentItem>,
    onClick: (CommentItem) -> Unit,
) {
    LazyColumn {
        itemsIndexed(commentList) { _, commentItem ->
            Column(
                Modifier
                    .clickable { onClick(commentItem) }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
            ) {
                CommentItemComponent(commentItem)
            }
        }
    }
}

@Composable
fun CommentItemComponent(commentItem: CommentItem) {

}
