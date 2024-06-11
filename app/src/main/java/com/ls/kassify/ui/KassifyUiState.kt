package com.ls.kassify.ui

import com.ls.kassify.data.Transaction

data class KassifyUiState(
    //Login & SignUp Screen Variables
    val email: String = "",
    val password: String = "",

    //General-Variables
    val cashBalance: Double = 0.00,
    val transactionList: MutableList<Transaction> = mutableListOf(),
    val currentTransaction: Transaction = Transaction(),
    val amountInput: String = ""
)