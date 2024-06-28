package com.ls.kassify.ui

import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Transaction
import com.ls.kassify.R
import com.ls.kassify.ui.screens.TransactionDetailsScreen
import com.ls.kassify.ui.screens.TransactionEditorScreen
import com.ls.kassify.ui.screens.TransactionListScreen
import com.ls.kassify.ui.theme.KassifyTheme

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
    val appUiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = KassifyScreen.valueOf(
        backStackEntry?.destination?.route ?: KassifyScreen.TransactionList.name
    )
    val onCancelButtonClicked: () -> Unit = {
        if (currentScreen == KassifyScreen.TransactionList) {
            viewModel.updateShowLogoutDialog()
        } else {
            navController.navigateUp()
        }
    }
    Scaffold(topBar = {
        KassifyAppBar(
            title = currentScreen.title,
            onCancelButtonClicked = onCancelButtonClicked,
        )
    }) { innerPadding ->
        if (viewModel.showLogoutDialog) {
            LogoutDialog(
                onConfirmButtonClicked = {
                    Amplify.Auth.signOut { }
                    viewModel.updateShowLogoutDialog()
                },
                onCancelButtonClicked = { viewModel.updateShowLogoutDialog() }
            )
        }

        NavHost(
            navController = navController,
            startDestination = KassifyScreen.TransactionList.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            //TransactionList-Screen
            composable(route = KassifyScreen.TransactionList.name) {
                TransactionListScreen(
                    onTransactionCardClicked = {
                        viewModel.getTransaction(it)
                        navController.navigate(KassifyScreen.TransactionDetails.name)
                    },
                    onAddButtonClicked = {
                        viewModel.createNewTransaction()
                        viewModel.updateNextCashBalance(transaction = appUiState.currentTransaction)
                        navController.navigate(KassifyScreen.NewTransaction.name)
                    },
                    transactions = appUiState.transactionList,
                    cashBalance = appUiState.cashBalance,
                )
            }
            //TransactionDetail-Screen
            composable(route = KassifyScreen.TransactionDetails.name) {
                TransactionDetailsScreen(
                    onEditButtonClicked = {
                        viewModel.getTransaction(it)
                        viewModel.updateNextCashBalance(
                            transaction = appUiState.currentTransaction,
                            isNewTransaction = false
                        )
                        navController.navigate(KassifyScreen.TransactionEditor.name)
                    },
                    onDeleteButtonClicked = { viewModel.updateShowDeleteDialog() },
                    onDeleteConfirmedClicked = {
                        viewModel.deleteTransaction()
                        navController.navigateUp()
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_transaction_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCancelButtonClicked = {
                        navController.navigateUp()
                    },
                    onCancelDeleteDialogClicked = { viewModel.updateShowDeleteDialog() },
                    transaction = appUiState.currentTransaction,
                    showDeleteDialog = viewModel.showDeleteDialog,
                    lastTransaction = viewModel.lastTransactionInList(appUiState.currentTransaction)
                )
            }
            //TransactionEditor-Screen
            composable(route = KassifyScreen.TransactionEditor.name) {
                TransactionEditorScreen(
                    onSaveButtonClicked = {
                        viewModel.updateTransaction(it)
                        navController.navigateUp()
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
                    dateOfLastTransaction = viewModel.getLastTransactionDate(appUiState.currentTransaction),
                    dateOfNextTransaction = viewModel.getNextTransactionDate(appUiState.currentTransaction),
                    categories = appUiState.categoryList,
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
                    isError = viewModel.isError
                )
            }
            //NewTransaction-Screen
            composable(route = KassifyScreen.NewTransaction.name) {
                TransactionEditorScreen(
                    onSaveButtonClicked = {
                        //Adding new transaction to transactionList
                        viewModel.addTransaction(it)
                        navController.navigateUp()
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
                    categories = appUiState.categoryList,
                    amountInput = appUiState.amountInput,
                    amountErrorMessage = viewModel.validAmount.errorMessage,
                    dateOfLastTransaction =
                    if (appUiState.transactionList.size > 0)
                        appUiState.transactionList[appUiState.transactionList.lastIndex].date
                    else
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
                    isError = viewModel.isError
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
        onDismissRequest = { onCancelButtonClicked() },
        title = { Text(text = stringResource(R.string.logout)) },
        text = { Text(text = stringResource(R.string.logout_question)) },
        dismissButton = {
            TextButton(onClick = { onCancelButtonClicked() }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmButtonClicked() }) {
                Text(text = stringResource(R.string.logout))
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
