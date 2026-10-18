package dev.muffar.moneyfikasi.utils.constants

import java.util.UUID

object UUIDConst {
    val empty: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    val TransferInCategoryId: UUID = UUID.fromString("25e23912-70c0-4b52-a67c-05b1c786d032")
    val TransferOutCategoryId: UUID = UUID.fromString("150bbc61-7a8c-4a9a-ab06-da7e4f6d4aa5")
    val TransferFeeCategoryId: UUID = UUID.fromString("abf1e9b7-c898-4717-9d82-f06dd6c476e1")
    val DebtCategoryId: UUID = UUID.fromString("c4d3b2a1-5e6f-4a7b-8c9d-0e1f2a3b4c5d")
    val ReceivableCategoryId: UUID = UUID.fromString("2af51c09-f575-4716-b453-e6861d6c5c8f")
}