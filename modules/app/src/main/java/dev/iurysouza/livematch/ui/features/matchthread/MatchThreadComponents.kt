package dev.iurysouza.livematch.ui.features.matchthread

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText

@Composable
fun MatchDescription(
    htmlDescription: String,
) {
    Box(modifier = Modifier.height(400.dp)) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())) {
            RichText(Modifier.padding(16.dp)) {
                Markdown(htmlDescription)
            }
        }
    }
}

@Composable
fun CommentList(
    commentList: List<CommentItem>,
    onClick: (CommentItem) -> Unit,
) {
    LazyColumn {
        items(commentList) { commentItem ->
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
    Text(commentItem.author)
    RichText(
        modifier = Modifier.padding(16.dp)
    ) {
        Markdown(commentItem.comment)
    }
}

@Composable
fun CommentProgress() {

}
