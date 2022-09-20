package dev.iurysouza.livematch.ui.features.matchthread.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemLink(modifier: Modifier = Modifier, title: String, linkUrl: String) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val startIndex = 0
        val endIndex = title.length
        append(title)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )

        // attach a string annotation that stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = linkUrl,
            start = startIndex,
            end = endIndex
        )

    }

// UriHandler parse and opens URI inside AnnotatedString Item in Browse
    val uriHandler = LocalUriHandler.current

// 🔥 Clickable text returns position of text that is clicked in onClick callback
    Column(modifier = modifier) {
        ClickableText(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            text = annotatedLinkString,
            onClick = {
                annotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        uriHandler.openUri(stringAnnotation.item)
                    }
            }
        )
    }

}

@Preview
@Composable
fun ItemLinkPreview() {
    ItemLink(
        title = "Atletico Madrid 0 - [2] Real Madrid - Federico Valverde 36'",
        linkUrl = "https://www.google.com"
    )
}
