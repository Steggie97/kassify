package com.ls.kassify.ui

import com.ls.kassify.data.TransactionModel

data class KassifyUiState(
    val cashBalance: Double = 0.00,
    val nextTransId: Int  = 1,
    val transactionList: MutableList<TransactionModel> = mutableListOf(),
    val currentTransaction: TransactionModel = TransactionModel(),
    val amountInput: String = "",
    val nextCashBalance: Double = 0.00
)