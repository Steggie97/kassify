package com.ls.kassify.ui

//import com.ls.kassify.ui.theme.TextDownloadableFontsSnippet2.fontFamily
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.core.Amplify
import com.ls.kassify.R
import com.ls.kassify.ui.screens.TransactionDetailsScreen
import com.ls.kassify.ui.screens.TransactionEditorScreen
import com.ls.kassify.ui.screens.TransactionListScreen
import com.ls.kassify.ui.theme.KassifyTheme
import java.time.ZoneId

enum class KassifyScreen(@StringRes val title: Int) {
    TransactionList(title = R.string.cash_register),
    TransactionDetails(title = R.string.transaction_details),
    TransactionEditor(title = R.string.transaction_edit),
    NewTransaction(title = R.string.transaction_new)
}

@Composable
fun KassifyApp(
    viewModel: KassifyViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // UI-State
    val appUiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = KassifyScreen.valueOf(
        backStackEntry?.destination?.route ?: KassifyScreen.TransactionList.name
    )
    val onCancelButtonClicked: () -> Unit = {
        // Log-Out-Dialog if arrow is clicked in TransactionListScreen
        if (currentScreen == KassifyScreen.TransactionList) {
            viewModel.updateShowLogoutDialog()
        } else {
            // safe back-navigation
            navController.navigateUp()
        }
    }
    Scaffold(topBar = {
        KassifyAppBar(
            // dynamic Appbar-Title
            title = currentScreen.title,
            onCancelButtonClicked = onCancelButtonClicked,
        )
    }) { innerPadding ->
        if (viewModel.showLogoutDialog) {
            LogoutDialog(
                onConfirmButtonClicked = {
                    // AWS -Amplify log-out command -> Authenticator UI will show the sign-in-component
                    Amplify.Auth.signOut { }
                    // updates the showLogOutDialog to false (initial state)
                    viewModel.updateShowLogoutDialog()
                },
                onCancelButtonClicked = {
                    // updates the showLogOutDialog to false (initial state)
                    viewModel.updateShowLogoutDialog()
                }
            )
        }
        //Navigation-Composable
        NavHost(
            navController = navController,
            // first screen main-screen is the TransactionListScreen
            startDestination = KassifyScreen.TransactionList.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            //TransactionList-Screen
            composable(route = KassifyScreen.TransactionList.name) {
                TransactionListScreen(
                    onTransactionCardClicked = {
                        // updates the appUiState.currentTransaction with the selected transaction
                        viewModel.getTransaction(it)
                        // navigates to the transactionDetailsScreen
                        navController.navigate(KassifyScreen.TransactionDetails.name)
                    },
                    onAddButtonClicked = {
                        // updates the _uiState-variables
                        viewModel.createNewTransaction()
                        viewModel.updateNextCashBalance(transaction = appUiState.currentTransaction)
                        // navigates to transactionEditorScreen
                        navController.navigate(KassifyScreen.NewTransaction.name)
                    },
                    transactions = appUiState.transactions,
                    cashBalance = appUiState.cashBalance,
                    categories = appUiState.categoryList
                )
            }
            //TransactionDetail-Screen
            composable(route = KassifyScreen.TransactionDetails.name) {
                TransactionDetailsScreen(
                    onEditButtonClicked = {
                        // updates the currentTransaction with the selection
                        viewModel.getTransaction(it)
                        viewModel.updateNextCashBalance(
                            transaction = appUiState.currentTransaction,
                            isNewTransaction = false
                        )
                        // navigates to the TransactionEditorScreen
                        navController.navigate(KassifyScreen.TransactionEditor.name)
                    },
                    onDeleteButtonClicked = { viewModel.updateShowDeleteDialog() },
                    onDeleteConfirmedClicked = {
                        // deletes the current transaction in backend
                        viewModel.deleteTransaction(appUiState.currentTransaction)
                        // navigation to transactionListScreen
                        navController.navigateUp()
                        // toast message to notify the user of deletion
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_transaction_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCancelButtonClicked = {
                        // Navigates to the transactionListScreen
                        navController.navigateUp()
                    },
                    onCancelDeleteDialogClicked = { viewModel.updateShowDeleteDialog() },
                    transaction = appUiState.currentTransaction,
                    showDeleteDialog = viewModel.showDeleteDialog,
                    lastTransaction = viewModel.lastTransactionInList(appUiState.currentTransaction),
                    categories = appUiState.categoryList,
                    vatList = appUiState.vatList
                )
            }
            //TransactionEditor-Screen
            composable(route = KassifyScreen.TransactionEditor.name) {
                TransactionEditorScreen(
                    onSaveButtonClicked = {
                        viewModel.updateTransaction(it)
                        // navigation to transactionListScreen
                        navController.navigateUp()
                        // toast message to notify the user
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_transaction_saved),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCancelButtonClicked = {
                        navController.navigateUp()
                    },
                    transaction = appUiState.currentTransaction,
                    // retrieval of LocalDates for date-validator of the date picker dialog
                    dateOfLastTransaction = viewModel.getLastTransactionDate(appUiState.currentTransaction),
                    dateOfNextTransaction = viewModel.getNextTransactionDate(appUiState.currentTransaction),
                    categories = appUiState.categoryList,
                    vatList = appUiState.vatList,
                    amountInput = appUiState.amountInput,
                    amountErrorMessage = viewModel.validAmount.errorMessage,
                    cashBalance = appUiState.nextCashBalance,
                    onDateChange = { fieldName, value, date ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value,
                            date
                        )
                    },
                    onChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    isError = viewModel.isError,
                    formatInput = {
                        viewModel.formatAmountInput()
                    }
                )
            }
            //NewTransaction-Screen for creating a new transaction
            composable(route = KassifyScreen.NewTransaction.name) {
                TransactionEditorScreen(
                    onSaveButtonClicked = {
                        //Adding new transaction to transactionList and backend
                        viewModel.addTransaction(it)
                        // navigation to the transactionListScreen
                        navController.navigateUp()
                        // toast message to notify the user
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_transaction_saved),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCancelButtonClicked = {
                        // navigation to the transactionListScreen
                        navController.navigateUp()
                    },
                    transaction = appUiState.currentTransaction,
                    categories = appUiState.categoryList,
                    vatList = appUiState.vatList,
                    amountInput = appUiState.amountInput,
                    amountErrorMessage = viewModel.validAmount.errorMessage,
                    dateOfLastTransaction =
                    if (appUiState.transactions.size > 0)
                        // Date of the last transaction, converted in LocalDate, will be used for the date-validator of the date picker dialog
                        appUiState.transactions[appUiState.transactions.lastIndex].date.toDate()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    else
                        // the transaction list is empty -> no date required
                        null,
                    cashBalance = appUiState.nextCashBalance,
                    onDateChange = { fieldName, value, date ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value,
                            date
                        )
                    },
                    onChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    isError = viewModel.isError,
                    formatInput = {
                        viewModel.formatAmountInput()
                    }
                )
            }
        }
    }
}

@Composable
fun LogoutDialog(
    onConfirmButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)),
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onCancelButtonClicked() },
        title = {
            Text(
                text = stringResource(R.string.logout),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.logout_question),
                modifier = Modifier
                    .padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        dismissButton = {
            TextButton(onClick = { onCancelButtonClicked() }) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmButtonClicked() }) {
                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}

//Previews
@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun KassifyAppPreview() {
    KassifyTheme(darkTheme = false) {
        KassifyApp()
    }
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun KassifyAppDarkThemePreview() {
    KassifyTheme(darkTheme = true) {
        KassifyApp()
    }
}