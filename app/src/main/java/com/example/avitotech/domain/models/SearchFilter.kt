package com.example.avitotech.domain.models

data class SearchFilter(
    val query: String = "",
    val sortBy: SortType = SortType.TITLE
)

enum class SortType {
    TITLE, DATE
}