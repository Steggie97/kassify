package com.ls.kassify.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.api.graphql.model.ModelSubscription
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.amplifyframework.datastore.generated.model.VatType
import com.ls.kassify.Validation.ValidateAmount
import com.ls.kassify.Validation.ValidationResult
import com.ls.kassify.data.TransactionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

    // Initial loading and subscribing to Amplify Events
    init {
        updateCategoryList()
        updateVatList()
        updateTransactionList()
        subscribeToAmplifyEvents()
    }

    private fun updateValidationResult() {
        validAmount = ValidateAmount().execute(
            _uiState.value.currentTransaction.amount,
            _uiState.value.currentTransaction.amountPrefix,
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
        var newCategoryList: List<Category>
        try {
            Amplify.API.query(
                ModelQuery.list(Category::class.java),
                {
                    newCategoryList = it.data.items.toList()
                    _uiState.update { currentState ->
                        currentState.copy(
                            categoryList = newCategoryList
                        )
                    }
                    Log.i("Amplify", "Categories loaded: $it.data")
                },
                { Log.e("Amplify", "Failed to query Categories", it) }
            )
        } catch (e: Exception) {
            Log.e("Amplify", e.toString())
        }
    }

    private fun updateVatList() {
        var newVatList: List<VatType>
        try {
            Amplify.API.query(
                ModelQuery.list(VatType::class.java),
                {
                    newVatList = it.data.items.toList()
                    _uiState.update { currentState ->
                        currentState.copy(
                            vatList = newVatList
                        )
                    }
                    Log.i("Amplify", "VAT-Types loaded: $it.data")
                },
                { Log.e("Amplify", "Failed to query VAT-Types", it) }
            )
        } catch (e: Exception) {
            Log.e("Amplify", e.toString())
        }
    }

    private fun subscribeToAmplifyEvents() {
        //Subscription for Create Events:
        Amplify.API.subscribe(
            ModelSubscription.onCreate(Transaction::class.java),
            { Log.i("Amplify", "Transaction create subscription established") },
            {
                Log.i("Amplify", "Transaction create subscription received: ${it.data}")
                val newTransactions: MutableList<Transaction> = _uiState.value.transactions
                newTransactions.add(it.data)
                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = newTransactions
                    )
                }
                updateCashBalance()
            },
            { Log.e("Amplify", "Create Subscription failed", it) },
            { Log.i("Amplify", "Transaction create subscription completed") }
        )

        // Subscription for Update-Events:
        Amplify.API.subscribe(
            ModelSubscription.onUpdate(Transaction::class.java),
            { Log.i("Amplify", "Transaction update subscription established") },
            {
                Log.i("Amplify", "Transaction update subscription received: ${it.data}")
                val newTransactions: MutableList<Transaction> = _uiState.value.transactions
                newTransactions[getTransactionIndex(it.data.id)] = it.data
                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = newTransactions
                    )
                }
                updateCashBalance()
            },
            { Log.e("Amplify", "Update Subscription failed", it) },
            { Log.i("Amplify", "Transaction update subscription completed") }
        )

        // Subscription for Delete-Events:
        Amplify.API.subscribe(
            ModelSubscription.onDelete(Transaction::class.java),
            { Log.i("Amplify", "Transaction delete subscription established") },
            {
                Log.i("Amplify", "Transaction delete subscription received: ${it.data}")
                val newTransactions: MutableList<Transaction> = _uiState.value.transactions
                newTransactions.remove(it.data)
                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = newTransactions
                    )
                }
                updateCashBalance()
            },
            { Log.e("Amplify", "Delete Subscription failed", it) },
            { Log.i("Amplify", "Transaction delete subscription completed")}
        )
    }

    private fun updateTransactionList() {
        var newTransactionList: MutableList<Transaction>
        try {
            Amplify.API.query(
                ModelQuery.list(Transaction::class.java),
                {
                    newTransactionList = it.data.items.toMutableList()
                    newTransactionList =
                        newTransactionList.sortedBy { it.createdAt.toString() }.toMutableList()
                    Log.i("TransactionList", newTransactionList.toString())
                    _uiState.update { currentState ->
                        currentState.copy(
                            transactions = newTransactionList
                        )
                    }
                    updateCashBalance()
                    Log.i("Amplify", "Transactions updated: ${it.data}")
                },
                { Log.e("Amplify", "Failed to query Transactions: ", it) }
            )
        } catch (e: Exception) {
            Log.e("Amplify", e.toString())
        }

    }


    fun addTransaction(transaction: Transaction) {
        Log.i("AddTransaction", "Transaction to add: $transaction")
        Amplify.API.mutate(
            ModelMutation.create(transaction),
            { Log.i("Amplify", "Added Transaction with id: ${it.data.id}") },
            { Log.e("Amplify", "Create failed", it) }
        )
    }

    fun updateTransaction(transaction: Transaction) {
        Amplify.API.mutate(
            ModelMutation.update(transaction),
            { Log.i("Amplify", "Updated Transaction with id: ${it.data.id}") },
            { Log.e("Amplify", "Update failed", it) }
        )
    }

    fun deleteTransaction(transaction: Transaction) {
        Amplify.API.mutate(
            ModelMutation.delete(transaction),
            { Log.i("Amplify", "Transaction with id ${it.data.id} deleted") },
            { Log.e("Amplify", "Deletion failed", it) }
        )
        updateShowDeleteDialog()
    }
    fun updateCurrentTransaction(
        fieldName: String,
        value: String = "",
        date: LocalDate? = null,
    ) {
        val updatedTransaction: Transaction =
            when (fieldName) {
                "date" ->
                    if (date != null) {
                        _uiState.value.currentTransaction.copyOfBuilder()
                            .date(Temporal.Date(date.toString())).build()
                    } else {
                        _uiState.value.currentTransaction.copyOfBuilder()
                            .date((Temporal.Date(LocalDate.now().toString()))).build()
                    }
                // changing the amountPrefix will reset the category and the vatNo to their default-values
                "prefix" -> _uiState.value.currentTransaction.copyOfBuilder().amountPrefix(value.toBoolean()).categoryNo(9999).vatNo(null).build()
                "amount" -> {
                    _uiState.value.currentTransaction.copyOfBuilder().amount(
                        try {
                            (NumberFormat.getInstance().parse(value)?.toDouble() ?: 0.0)
                        } catch (e: Exception) {
                            0.0
                        }
                    ).build()
                }
                "category" -> _uiState.value.currentTransaction.copyOfBuilder().categoryNo(getCategoryNo(value)).vatNo(null).build()
                "vat" -> _uiState.value.currentTransaction.copyOfBuilder().vatNo(getVatNo(value)).build()
                "receiptNo" -> _uiState.value.currentTransaction.copyOfBuilder().receiptNo(value).build()
                "text" -> _uiState.value.currentTransaction.copyOfBuilder().transactionText(value).build()
                else -> _uiState.value.currentTransaction.copyOfBuilder().build()
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
        _uiState.value.transactions.forEach {
            if (it.amountPrefix)
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
        if (!isNewTransaction && transaction.amountPrefix) {
            cashBalance -= transaction.amount
        }
        if (!isNewTransaction && !transaction.amountPrefix) {
            cashBalance += transaction.amount
        }
        _uiState.update { currentState ->
            currentState.copy(
                nextCashBalance = cashBalance
            )
        }
    }

    fun createNewTransaction() {
        val newTransaction = Transaction.builder()
            .date(Temporal.Date(LocalDate.now().toString()))
            .amountPrefix(true)
            .amount(0.00)
            .accountNo(1600)
            .categoryNo(9999)
            .vatNo(null)
            .transactionText("")
            .receiptNo("")
            .transactionNo(_uiState.value.transactions.size + 1)
            .build()

        _uiState.update { currentState ->
            currentState.copy(
                currentTransaction = newTransaction,
                amountInput = ""
            )
        }

    }

    //TransactionList Screen
    fun getTransaction(id: String) {
        for (transaction in _uiState.value.transactions) {
            if (transaction.id == id) {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentTransaction = transaction,
                        amountInput = NumberFormat.getInstance().format(transaction.amount)
                    )
                }
            }
        }
    }

    private fun getTransactionIndex(id: String): Int {
        var index: Int = -1
        for (i: Int in 0..<_uiState.value.transactions.size) {
            if (id == _uiState.value.transactions[i].id) {
                index = i
            }
        }
        return index
    }

    fun getLastTransactionDate(transaction: Transaction): LocalDate? {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.id)

        //Check if current transaction is the first transaction in transaction-list
        if (currentTransactionIndex == 0) {
            return null
        }
        return convertTemporalDateToLocalDate(_uiState.value.transactions[currentTransactionIndex - 1].date)
    }

    fun getNextTransactionDate(transaction: Transaction): LocalDate {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.id)

        //Check if current transaction is the last transaction in transaction-list
        if (currentTransactionIndex == _uiState.value.transactions.lastIndex) {
            return LocalDate.now()
        }
        return convertTemporalDateToLocalDate(_uiState.value.transactions[currentTransactionIndex + 1].date)
    }

    fun lastTransactionInList(transaction: Transaction): Boolean {
        if (_uiState.value.transactions.size == 0) {
            return false
        }
        return _uiState.value.transactions[_uiState.value.transactions.size - 1] == transaction
    }

    private fun convertTemporalDateToLocalDate(date: Temporal.Date): LocalDate {
        return date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun getCategoryNo(name: String): Int {
        for (i: Int in 0..<_uiState.value.categoryList.size) {
            if (name == _uiState.value.categoryList[i].categoryName)
                return _uiState.value.categoryList[i].categoryNo
        }
        // Fallback-Value
        return 9999
    }

    private fun getVatNo(name: String): Int? {
        for (i: Int in 0..<_uiState.value.vatList.size){
            if(name == _uiState.value.vatList[i].vatType) {
                return _uiState.value.vatList[i].vatNo
            }
        }
        return null
    }

}
