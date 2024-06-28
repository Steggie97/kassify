package com.ls.kassify.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.ls.kassify.Validation.ValidateAmount
import com.ls.kassify.Validation.ValidationResult
import com.ls.kassify.data.TransactionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.time.LocalDate
import com.amplifyframework.datastore.generated.model.Transaction

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
        isError = !validAmount.successful
    }
    //ViewModel-Functions

    fun updateShowDeleteDialog() {
        showDeleteDialog = !showDeleteDialog
    }

    fun updateShowLogoutDialog() {
        showLogoutDialog = !showLogoutDialog
    }

    private fun updateCategoryList() {
        var newCategoryList: List<Category> = listOf()
        try {
            Amplify.API.query(
                ModelQuery.list(Category::class.java),
                { newCategoryList = it.data.items.toList() },
                { Log.e("Amplify", "Failed to query Categories", it) }
            )

            _uiState.update { currentState ->
                currentState.copy(
                    categoryList = newCategoryList
                )
            }
        } catch (e: Exception) {
            Log.e("Amplify", e.toString())
        }
    }

    //Transaction-Editor Screen
    fun addTransaction(transaction: TransactionModel) {
        val newTransaction = Transaction.builder()
            .date(Temporal.Date(transaction.date.toString()))
            .amountPrefix(transaction.isPositiveAmount)
            .amount(transaction.amount)
            .accountNo(transaction.accountNo)
            //.categoryNo(transaction.category)
            //.vatNo(transaction.vat)
            .receiptNo(transaction.receiptNo)
            .transactionText(transaction.text)
            .build()
        Amplify.API.mutate(
            ModelMutation.create(newTransaction),
            { Log.i("Amplify", "Added Transaction with id: ${it.data.id}") },
            { Log.e("Amplify", "Create failed", it) }
        )
        /*
        val newTransactionList = _uiState.value.transactionList
        newTransactionList.add(transaction)
        _uiState.update { currentState ->
            currentState.copy(
                transactionList = newTransactionList
            )
        }
        updateCashBalance()

         */
    }

    fun updateTransaction(updatedTransaction: TransactionModel) {
        val newTransactionList: MutableList<TransactionModel> = _uiState.value.transactionList
        val index: Int = getTransactionIndex(updatedTransaction.transNo)
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
    ) {
        val updatedTransaction: TransactionModel =
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

    private fun updateCashBalance() {
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

    fun updateNextCashBalance(transaction: TransactionModel, isNewTransaction: Boolean = true) {
        var cashBalance = _uiState.value.nextCashBalance
        if (!isNewTransaction && transaction.isPositiveAmount) {
            cashBalance -= transaction.amount
        }
        if (!isNewTransaction && !transaction.isPositiveAmount) {
            cashBalance += transaction.amount
        }
        _uiState.update { currentState ->
            currentState.copy(
                nextCashBalance = cashBalance
            )
        }
    }

    fun createNewTransaction() {
        updateCategoryList()
        val newTransaction = TransactionModel(transNo = _uiState.value.nextTransId)
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
            if (transaction.transNo == transId) {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentTransaction = transaction,
                        amountInput = NumberFormat.getInstance().format(transaction.amount)
                    )
                }
            }
        }
    }

    private fun getTransactionIndex(transId: Int): Int {
        var index: Int = -1
        for (i: Int in 0..<_uiState.value.transactionList.size) {
            if (transId == _uiState.value.transactionList[i].transNo) {
                index = i
            }
        }
        return index
    }

    fun getLastTransactionDate(transaction: TransactionModel): LocalDate? {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.transNo)

        //Check if current transaction is the first transaction in transaction-list
        if (currentTransactionIndex == 0) {
            return null
        }
        return _uiState.value.transactionList[currentTransactionIndex - 1].date
    }

    fun getNextTransactionDate(transaction: TransactionModel): LocalDate {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.transNo)

        //Check if current transaction is the last transaction in transaction-list
        if (currentTransactionIndex == _uiState.value.transactionList.lastIndex) {
            return LocalDate.now()
        }
        return _uiState.value.transactionList[currentTransactionIndex + 1].date
    }

    fun lastTransactionInList(transaction: TransactionModel): Boolean {
        if (_uiState.value.transactionList.size == 0) {
            return false
        }
        return _uiState.value.transactionList[_uiState.value.transactionList.size - 1] == transaction
    }
}
