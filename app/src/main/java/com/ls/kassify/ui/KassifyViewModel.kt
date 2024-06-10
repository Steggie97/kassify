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
    var isDeposit by mutableStateOf(true)
        private set
    /*
    var currentTransaction by mutableStateOf(Transaction())
        private set
    */

    //ViewModel-Functions
    //Login & SignUp Screen
    fun switchShowPassword() {
        showPassword = !showPassword
    }

    fun switchShowPasswordConfirm() {
        showPasswordConfirm = !showPasswordConfirm
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
    }

    fun updateTransaction(newTransaction: Transaction) {
        _uiState.value = _uiState.value.copy(currentTransaction = newTransaction)
    }

    fun updateCurrentTransaction(fieldName: String, value: String) {
        val updatedTransaction =
            when (fieldName) {
                "date" -> _uiState.value.currentTransaction.copy(date = value)
                "amount" -> _uiState.value.currentTransaction.copy(
                    amount =
                    try {
                        NumberFormat.getCurrencyInstance().parse(value)?.toDouble() ?: 0.00
                    } catch (e: Exception) {
                        0.00
                    }
                )

                "category" -> _uiState.value.currentTransaction.copy(category = value)
                "receiptNo" -> _uiState.value.currentTransaction.copy(receiptNo = value)
                "text" -> _uiState.value.currentTransaction.copy(text = value)
                else -> _uiState.value.currentTransaction
            }
        _uiState.value = _uiState.value.copy(currentTransaction = updatedTransaction)
    }


    //TransactionDetail Screen

    fun deleteTransaction() {

    }

    //TransactionList Screen
    fun getTransaction() {

    }


}
