package com.ls.kassify.ui

import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ls.kassify.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    //ViewModel-Functions

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

    fun addTransaction(transaction: Transaction) {
        val newTransactionList = _uiState.value.transactionList
        newTransactionList.add(transaction)
        _uiState.update { currentState ->
            currentState.copy(
                transactionList = newTransactionList
            )
        }
    }

    fun updateTransaction() {

    }
}
