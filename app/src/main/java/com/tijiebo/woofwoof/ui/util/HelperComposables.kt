package com.tijiebo.woofwoof.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString

@Composable
fun textMultiStyle(
    originalText: String,
    customTextList: List<TextWithStyle>
): AnnotatedString {
    var annotatedString = buildAnnotatedString { append(originalText) }

    customTextList.forEach { textExcerpt ->
        annotatedString =
            buildAnnotatedString {
                val startIndex = annotatedString.indexOf(textExcerpt.customText, ignoreCase = true)
                val endIndex = startIndex + textExcerpt.customText.length
                append(annotatedString)
                addStyle(
                    style = textExcerpt.style.toSpanStyle(),
                    start = startIndex,
                    end = endIndex
                )
            }
    }
    return annotatedString
}

data class TextWithStyle(
    val customText: String,
    val style: TextStyle
)
