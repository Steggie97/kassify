package com.ls.kassify.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ls.kassify.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat

class KassifyViewModel : ViewModel() {
    // UI-State
    private val _uiState = MutableStateFlow(KassifyUiState())
    val uiState: StateFlow<KassifyUiState> = _uiState.asStateFlow()

    var showPassword by mutableStateOf(false)
        private set
    var showPasswordConfirm by mutableStateOf(false)
        private set
    var passwordConfirm by mutableStateOf("")
        private set

    // amountPrefix:
    // true -> positive amount,
    // false -> negative amount
    var amountPrefix by mutableStateOf(true)
        private set

    //ViewModel-Functions
    //Login & SignUp Screen
    fun switchShowPassword() {
        showPassword = !showPassword
    }

    fun switchShowPasswordConfirm() {
        showPasswordConfirm = !showPasswordConfirm
    }

    fun updateAmountPrefix(value: Boolean) {
        amountPrefix = value
    }

    fun checkAmountPrefix(amount: Double) {
        if (amount < 0.00) {
            amountPrefix = false
        } else {
            amountPrefix = true
        }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = newPassword
            )
        }
    }

    fun updatePasswordConfirm(newPasswordConfirm: String) {
        passwordConfirm = newPasswordConfirm
    }

    fun updateEmail(newEmail: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = newEmail
            )
        }
    }

    //Transaction-Editor Screen

    fun addTransaction(transaction: Transaction) {
        val newTransactionList = _uiState.value.transactionList
        newTransactionList.add(transaction)
        _uiState.update { currentState ->
            currentState.copy(
                transactionList = newTransactionList
            )
        }
        updateCashBalance()
    }

    fun updateTransaction(newTransaction: Transaction) {
        _uiState.value = _uiState.value.copy(currentTransaction = newTransaction)
    }

    fun updateCurrentTransaction(fieldName: String, value: String = "") {
        if (fieldName == "prefix") {
            updateAmountPrefix(value.toBoolean())
        }
        val updatedTransaction: Transaction =
            when (fieldName) {
                "date" -> _uiState.value.currentTransaction.copy(date = value)
                "prefix" -> if ((amountPrefix && _uiState.value.currentTransaction.amount < 0.00) || (!amountPrefix && _uiState.value.currentTransaction.amount > 0.00))
                    _uiState.value.currentTransaction.copy(amount = (_uiState.value.currentTransaction.amount * -1.00))
                else
                    _uiState.value.currentTransaction.copy(amount = (_uiState.value.currentTransaction.amount))

                "amount" -> {
                    _uiState.value.currentTransaction.copy(
                        amount =
                        try {
                            if (amountPrefix && _uiState.value.currentTransaction.amount < 0.00 || !amountPrefix && _uiState.value.currentTransaction.amount > 0.00)
                                (NumberFormat.getInstance().parse(value)?.toDouble() ?: 0.0) * -1
                            else
                                (NumberFormat.getInstance().parse(value)?.toDouble() ?: 0.0)
                        } catch (e: Exception) {
                            0.0
                        },
                    )
                }

                "category" -> _uiState.value.currentTransaction.copy(category = value)
                "receiptNo" -> _uiState.value.currentTransaction.copy(receiptNo = value)
                "text" -> _uiState.value.currentTransaction.copy(text = value)
                else -> _uiState.value.currentTransaction
            }

        _uiState.update { currentState ->
            currentState.copy(
                currentTransaction = updatedTransaction,
                amountInput =
                if (fieldName == "amount")
                    value
                else
                    _uiState.value.amountInput
            )
        }
    }

    fun updateCashBalance() {
        var newCashBalance: Double = 0.00
        _uiState.value.transactionList.forEach { newCashBalance += it.amount }
        _uiState.update { currentState ->
            currentState.copy(cashBalance = newCashBalance)
        }
    }

    fun createNewTransaction() {
        val newTransaction = Transaction()
        checkAmountPrefix(newTransaction.amount)
        _uiState.update { currentState ->
            currentState.copy(
                currentTransaction = newTransaction,
                amountInput = "",
            )
        }

    }

    //TransactionDetail Screen

    fun deleteTransaction() {

    }

    //TransactionList Screen
    fun getTransaction() {

    }


}
