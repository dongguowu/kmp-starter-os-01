package com.lduboscq.appkickstarter.main.book


fun getBookList(): List<BookData> {
    return listOf(
        BookData(
            "1",
            "head first kotlin",
            "https://kotlinlang.org/docs/images/head-first-kotlin.jpeg",
            1000
        ),
        BookData(
            "2",
            "kotlin in action",
            "https://kotlinlang.org/docs/images/kotlin-in-action.png",
            1000
        ),
        BookData(
            "3",
            "head first kotlin",
            "https://kotlinlang.org/docs/images/head-first-kotlin.jpeg",
            1000
        ),
        BookData(
            "4",
            "joy of kotlin",
            "https://kotlinlang.org/docs/images/joy-of-kotlin.png",
            1000
        ),
        BookData(
            "5",
            "kotlin in action",
            "https://kotlinlang.org/docs/images/kotlin-in-action.png",
            1000
        ),
        BookData("6", "joy of kotlin", "https://kotlinlang.org/docs/images/joy-of-kotlin.png", 100),
    )
}