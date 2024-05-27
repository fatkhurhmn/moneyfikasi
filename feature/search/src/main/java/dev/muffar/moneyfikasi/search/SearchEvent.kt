package dev.muffar.moneyfikasi.search

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
}