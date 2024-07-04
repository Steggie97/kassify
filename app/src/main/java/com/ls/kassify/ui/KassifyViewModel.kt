package com.ls.kassify.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId

class KassifyViewModel : ViewModel() {
    // UI-State-instance
    private val _uiState = MutableStateFlow(KassifyUiState())
    val uiState: StateFlow<KassifyUiState> = _uiState.asStateFlow()

    //Viewmodel variables
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

    // updates the isError
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

    //loads categories from backend end into the _uistate.values.categoryList
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

    // //loads vatTypes from backend end into the _uistate.values.vatList
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

    // function to subscribe to the Amplify-Events create, delete and update
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

    // loads the saved transactions from backend and updates the _uistate.values.transactions
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

    // function to create a new transaction-record in backend
    fun addTransaction(transaction: Transaction) {
        Log.i("AddTransaction", "Transaction to add: $transaction")
        Amplify.API.mutate(
            ModelMutation.create(transaction),
            { Log.i("Amplify", "Added Transaction with id: ${it.data.id}") },
            { Log.e("Amplify", "Create failed", it) }
        )
    }

    // updates a transaction-record in backend
    fun updateTransaction(transaction: Transaction) {
        Amplify.API.mutate(
            ModelMutation.update(transaction),
            { Log.i("Amplify", "Updated Transaction with id: ${it.data.id}") },
            { Log.e("Amplify", "Update failed", it) }
        )
    }

    // deletes a transaction-record in backend
    fun deleteTransaction(transaction: Transaction) {
        Amplify.API.mutate(
            ModelMutation.delete(transaction),
            { Log.i("Amplify", "Transaction with id ${it.data.id} deleted") },
            { Log.e("Amplify", "Deletion failed", it) }
        )
        updateShowDeleteDialog()
    }

    //currentTransaction is a variable of _uiState and holds the selectedTransaction.
    fun updateCurrentTransaction(
        fieldName: String,
        value: String = "",
        date: LocalDate? = null,
    ) {
        val updatedTransaction: Transaction =
            // saves the altered value in a temporary variable in dependency of the fieldname
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
                        // value is a string and needs to be converted in double
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
                // in case of no changes:
                else -> _uiState.value.currentTransaction.copyOfBuilder().build()
            }

        // Updating the currentTransaction variable with the temporary transaction-variable
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
        // checking the new amount and  new cashBalance with validator
        updateValidationResult()
    }

    // update of _uiState.value.cashBalance
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

    // updates of _uiState.value.nextCashBalance.
    fun updateNextCashBalance(transaction: Transaction, isNewTransaction: Boolean = true) {
        var cashBalance = _uiState.value.nextCashBalance
        //
        if (!isNewTransaction && transaction.amountPrefix) {
            cashBalance -= transaction.amount
        }
        if (!isNewTransaction && !transaction.amountPrefix) {
            cashBalance += transaction.amount
        }
        // update
        _uiState.update { currentState ->
            currentState.copy(
                nextCashBalance = cashBalance
            )
        }
    }

    // creating a new transaction-object and save it into the _uiState.value.currentTransaction
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
                // found transaction with id. This transaction is the new currentTransaction. Update of the currentTransaction-value of the _UiState-variable
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
        // returns the list-index of the transaction with an specific id. The function returns -1 if the transaction was not found in the transactionlist
        return index
    }

    fun getLastTransactionDate(transaction: Transaction): LocalDate? {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.id)

        //Check if current transaction is the first transaction in transaction-list
        if (currentTransactionIndex == 0) {
            // not
            return null
        }
        // returns a LocalDate which is retrieved from the transaction before the current transaction
        return convertTemporalDateToLocalDate(_uiState.value.transactions[currentTransactionIndex - 1].date)
    }

    fun getNextTransactionDate(transaction: Transaction): LocalDate {
        val currentTransactionIndex: Int = getTransactionIndex(transaction.id)

        //Check if current transaction is the last transaction in transaction-list
        if (currentTransactionIndex == _uiState.value.transactions.lastIndex) {
            // returns the current date
            return LocalDate.now()
        }
        // returns a LocalDate for the transaction after the currentTransaction
        return convertTemporalDateToLocalDate(_uiState.value.transactions[currentTransactionIndex + 1].date)
    }

    // checks if the transaction is the last transaction in list
    fun lastTransactionInList(transaction: Transaction): Boolean {
        if (_uiState.value.transactions.size == 0) {
            return false
        }
        return _uiState.value.transactions[_uiState.value.transactions.size - 1] == transaction
    }

    // converts TemporalDate into a LocalDate instance
    private fun convertTemporalDateToLocalDate(date: Temporal.Date): LocalDate {
        return date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    // returns the categoryNo for a given categoryName
    private fun getCategoryNo(name: String): Int {
        for (i: Int in 0..<_uiState.value.categoryList.size) {
            if (name == _uiState.value.categoryList[i].categoryName)
                return _uiState.value.categoryList[i].categoryNo
        }
        // Fallback-Value
        return 9999
    }

    // returns the vatNo for a given vatName/ vatType
    private fun getVatNo(name: String): Int? {
        for (i: Int in 0..<_uiState.value.vatList.size){
            if(name == _uiState.value.vatList[i].vatType) {
                return _uiState.value.vatList[i].vatNo
            }
        }
        return null
    }

}
