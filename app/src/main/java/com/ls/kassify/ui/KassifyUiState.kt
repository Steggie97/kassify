package com.ls.kassify.ui

import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.amplifyframework.datastore.generated.model.VatType
import java.time.LocalDate

data class KassifyUiState(
    // cashBalance which is shown in the transactionList
    val cashBalance: Double = 0.00,
    // transaction-list
    val transactions: MutableList<Transaction> = mutableListOf(),
    // category-list, loaded from backend
    val categoryList: List<Category> = listOf(),
    // vat-list, loaded from backend
    val vatList: List<VatType> = listOf(),
    // Transaction model from AWS Amplify
    val currentTransaction: Transaction = Transaction.builder().date(Temporal.Date(LocalDate.now().toString())).amountPrefix(true).amount(0.00).accountNo(1600).categoryNo(9999).transactionNo(transactions.size + 1).transactionText("").receiptNo("").build(),
    // String-amount input
    val amountInput: String = "",
    // temporal cashBalance which is shown in the transactionEditor
    val nextCashBalance: Double = 0.00
)