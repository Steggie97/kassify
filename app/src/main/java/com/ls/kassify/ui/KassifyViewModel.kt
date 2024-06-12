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

    var showDeleteDialog by mutableStateOf(false)
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

    fun updateShowDeleteDialog() {
        showDeleteDialog = !showDeleteDialog
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

    fun updateTransaction(updatedTransaction: Transaction) {
        val newTransactionList: MutableList<Transaction> = _uiState.value.transactionList
        val index: Int = getTransactionIndex(updatedTransaction.transId)
        if(index != -1) {
            newTransactionList[index] = updatedTransaction
            _uiState.update { currentState ->
                currentState.copy(transactionList = newTransactionList)
            }
        }
        updateCashBalance()
    }

    fun updateCurrentTransaction(fieldName: String, value: String = "") {
        if (fieldName == "prefix") {
            updateAmountPrefix(value.toBoolean())
        }
        val updatedTransaction: Transaction =
            when (fieldName) {
                "date" -> _uiState.value.currentTransaction.copy(date = value)
                "prefix" -> _uiState.value.currentTransaction.copy(isPositiveAmount = value.toBoolean())
                "amount" -> {
                    _uiState.value.currentTransaction.copy(
                        amount =
                        try {
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
        _uiState.value.transactionList.forEach {
            if (it.isPositiveAmount)
                newCashBalance += it.amount
            else
                newCashBalance -= it.amount
        }
        _uiState.update { currentState ->
            currentState.copy(cashBalance = newCashBalance)
        }
    }

    fun createNewTransaction() {
        val newTransaction = Transaction(transId = _uiState.value.nextTransId)
        val newNextTransId = _uiState.value.nextTransId + 1
        checkAmountPrefix(newTransaction.amount)
        _uiState.update { currentState ->
            currentState.copy(
                currentTransaction = newTransaction,
                amountInput = "",
                nextTransId = newNextTransId
            )
        }

    }
    //TransactionDetail Screen

    fun deleteTransaction() {
        val newTransactionList = _uiState.value.transactionList
        newTransactionList.remove(_uiState.value.currentTransaction)

        _uiState.update { currentState ->
            currentState.copy(
                transactionList = newTransactionList
            )
        }
        updateShowDeleteDialog()
        updateCashBalance()
    }

    //TransactionList Screen
    fun getTransaction(transId: Int) {
        for (transaction in _uiState.value.transactionList) {
            if (transaction.transId == transId) {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentTransaction = transaction,
                        amountInput = NumberFormat.getInstance().format(transaction.amount)
                    )
                }
            }
        }
    }

    fun getTransactionIndex(transId:Int): Int {
        var index: Int = -1
        for(i: Int  in 0..(_uiState.value.transactionList.size-1)) {
            if(transId == _uiState.value.transactionList[i].transId) {
                index = i
            }
        }
        return index
    }
}
