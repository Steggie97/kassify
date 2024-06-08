package com.ls.kassify.ui

import com.ls.kassify.data.Transaction

data class KassifyUiState(
    val userId: String = "",
    val cashBalance: Int = 0,
    val transactionList: List<Transaction> = listOf(),

    )