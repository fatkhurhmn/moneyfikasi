package dev.muffar.moneyfikasi.search

sealed class SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent()
}