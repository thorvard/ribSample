package com.ender.search.interactor

internal class KeywordValidator {

    private val rules = listOf(
        KeywordLengthValidator(),
        BannedKeywordValidator()
    )

    fun validate(keyword: String): Boolean {
        rules.forEach { rule ->
            if (!rule(keyword))
                return false
        }
        return true
    }
}

internal typealias KeywordValidatorRule = (keyword: String) -> Boolean

internal class KeywordLengthValidator : KeywordValidatorRule {
    override fun invoke(keyword: String): Boolean =
        (keyword.isEmpty() || keyword.length >= 3)
}

internal class BannedKeywordValidator : KeywordValidatorRule {
    private val dictionary = listOf("Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing")

    @ExperimentalStdlibApi
    override fun invoke(keyword: String): Boolean {
        return !dictionary.contains(keyword.toLowerCase())
    }
}