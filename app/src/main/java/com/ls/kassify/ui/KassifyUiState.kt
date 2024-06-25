package com.ls.kassify.ui

import com.ls.kassify.data.Transaction

data class KassifyUiState(
    val cashBalance: Double = 0.00,
    val nextTransId: Int  = 1,
    val transactionList: MutableList<Transaction> = mutableListOf(),
    val currentTransaction: Transaction = Transaction(),
    val amountInput: String = "",
    val nextCashBalance: Double = 0.00
)