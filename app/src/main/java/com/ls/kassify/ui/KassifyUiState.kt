package com.ls.kassify.ui

import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.amplifyframework.datastore.generated.model.VatType
import com.ls.kassify.data.TransactionModel

data class KassifyUiState(
    val cashBalance: Double = 0.00,
    val nextTransId: Int  = 1,
    val transactionList: MutableList<TransactionModel> = mutableListOf(),
    val categoryList: List<Category> = listOf(),
    val vatList: List<VatType> = listOf(),
    val currentTransaction: TransactionModel = TransactionModel(),
    val amountInput: String = "",
    val nextCashBalance: Double = 0.00
)