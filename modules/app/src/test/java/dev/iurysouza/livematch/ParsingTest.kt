package dev.iurysouza.livematch

import dev.iurysouza.livematch.ui.features.matchthread.MatchEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe


class ParsingTest : BehaviorSpec({

    given("given a champions league input") {
        val anInput = "[](#icon-whistle-big) 0' We're underway in Munich"
        `when`("parsed") {
            val result = parseChampionsLeagueEvents(input)
            then("should get a valid result") {
                println(result)
                result shouldNotBe null
            }
        }
    }
})

private const val TIME_PATTERN = """((\d)*')"""
private const val ICONS_PATTERN = """\[([^\[\]]*?)]\((\S*?)\)"""

private fun parseChampionsLeagueEvents(text: String): List<MatchEvent> =
    text.split("\n").mapNotNull { input ->
        runCatching {
            val icon = Regex(ICONS_PATTERN).findAll(input).first().value
            val time = Regex(TIME_PATTERN).findAll(input).first().value
            val description = input
                .remove(time)
                .remove(icon)
                .trim()

            MatchEvent(
                relativeTime = time.replace("'", ""),
                icon = icon.substringAfter("(").substringBefore(")"),
                description = description
            )
        }.getOrNull()
    }

fun String.remove(regex: String): String {
    return replace(regex, "")
}
