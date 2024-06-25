package com.ls.kassify.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ls.kassify.Validation.ValidateAmount
import com.ls.kassify.Validation.ValidationResult
import com.ls.kassify.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.time.LocalDate

class KassifyViewModel : ViewModel() {
    // UI-State
    private val _uiState = MutableStateFlow(KassifyUiState())
    val uiState: StateFlow<KassifyUiState> = _uiState.asStateFlow()

    var showDeleteDialog by mutableStateOf(false)
        private set

    var showLogoutDialog by mutableStateOf(false)
        private set

    // Validation:
    var validAmount: ValidationResult = ValidationResult(successful = true)
        private set

    var isError: Boolean = false
        private set

    private fun updateValidationResult() {
        validAmount = ValidateAmount().execute(
            _uiState.value.currentTransaction.amount,
            _uiState.value.currentTransaction.isPositiveAmount,
            _uiState.value.nextCashBalance
        )
        updateErrorState()
    }

    private fun updateErrorState() {
        isError =
            if (validAmount.successful) {
                false
            } else {
                true
            }
    }
    //ViewModel-Functions

    fun updateShowDeleteDialog() {
        showDeleteDialog = !showDeleteDialog
    }

    fun updateShowLogoutDialog() {
        showLogoutDialog = !showLogoutDialog
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
        if (index != -1) {
            newTransactionList[index] = updatedTransaction
            _uiState.update { currentState ->
                currentState.copy(transactionList = newTransactionList)
            }
        }
        updateCashBalance()
    }

    fun updateCurrentTransaction(
        fieldName: String,
        value: String = "",
        date: LocalDate? = null,
        isNewTransaction: Boolean = true
    ) {
        val updatedTransaction: Transaction =
            when (fieldName) {
                "date" -> _uiState.value.currentTransaction.copy(date = date ?: LocalDate.now())
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
        updateValidationResult()
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
            currentState.copy(
                cashBalance = newCashBalance,
                nextCashBalance = newCashBalance
            )
        }
    }

    fun updateNextCashBalance(transaction: Transaction, isNewTransaction: Boolean = true) {
        var cashBalance = _uiState.value.nextCashBalance
        if (!isNewTransaction && transaction.isPositiveAmount) {
            cashBalance = cashBalance - transaction.amount
        }
        if (!isNewTransaction && !transaction.isPositiveAmount) {
            cashBalance = cashBalance + transaction.amount
        }
        _uiState.update { currentState ->
            currentState.copy(
                nextCashBalance = cashBalance
            )
        }
    }

    fun createNewTransaction() {
        val newTransaction = Transaction(transId = _uiState.value.nextTransId)
        val newNextTransId = _uiState.value.nextTransId + 1
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

    fun getTransactionIndex(transId: Int): Int {
        var index: Int = -1
        for (i: Int in 0..(_uiState.value.transactionList.size - 1)) {
            if (transId == _uiState.value.transactionList[i].transId) {
                index = i
            }
        }
        return index
    }

    fun getLastTransactionDate(transaction: Transaction): LocalDate? {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.transId)

        //Check if current transaction is the first transaction in transaction-list
        if (currentTransactionIndex == 0) {
            return null
        }
        return _uiState.value.transactionList[currentTransactionIndex - 1].date
    }

    fun getNextTransactionDate(transaction: Transaction): LocalDate {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.transId)

        //Check if current transaction is the last transaction in transaction-list
        if (currentTransactionIndex == _uiState.value.transactionList.lastIndex) {
            return LocalDate.now()
        }
        return _uiState.value.transactionList[currentTransactionIndex + 1].date
    }

    fun lastTransactionInList(transaction: Transaction): Boolean {
        if (_uiState.value.transactionList.size == 0) {
            return false
        }
        return _uiState.value.transactionList[_uiState.value.transactionList.size - 1] == transaction
    }
}
