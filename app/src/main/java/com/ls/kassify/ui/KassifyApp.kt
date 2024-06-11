package com.ls.kassify.ui

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ls.kassify.R
import com.ls.kassify.data.Transaction
import com.ls.kassify.ui.screens.LogInScreen
import com.ls.kassify.ui.screens.SignUpScreen
import com.ls.kassify.ui.screens.TransactionDetailsScreen
import com.ls.kassify.ui.screens.TransactionEditorScreen
import com.ls.kassify.ui.screens.TransactionListScreen
import com.ls.kassify.ui.theme.KassifyTheme

enum class KassifyScreen(@StringRes val title: Int) {
    Login(title = R.string.app_name),
    SignUp(title = R.string.app_name),
    TransactionList(title = R.string.cash_register),
    TransactionDetails(
        title = R.string.transaction_details
    ),
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
        backStackEntry?.destination?.route ?: KassifyScreen.Login.name
    )
    Scaffold(topBar = {
        KassifyAppBar(
            title = currentScreen.title
        )
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = KassifyScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            //Login Screen
            composable(route = KassifyScreen.Login.name) {
                LogInScreen(onForgotPasswordButtonClicked = {},
                    onLoginButtonClicked = {
                        navController.navigate(KassifyScreen.TransactionList.name)
                    },
                    onSignUpButtonClicked = {
                        navController.navigate(KassifyScreen.SignUp.name)
                    },
                    email = appUiState.email,
                    password = appUiState.password,
                    showPassword = viewModel.showPassword,
                    onEmailChange = { viewModel.updateEmail(it) },
                    onPasswordChange = { viewModel.updatePassword(it) },
                    onShowPasswordClick = { viewModel.switchShowPassword() })
            }

            //SignUp Screen
            composable(route = KassifyScreen.SignUp.name) {
                SignUpScreen(onSignUpButtonClicked = {
                    navController.navigate(KassifyScreen.Login.name)
                },
                    onCancelButtonClicked = {
                        navController.popBackStack(KassifyScreen.Login.name, inclusive = false)
                    },
                    email = appUiState.email,
                    password = appUiState.password,
                    passwordConfirm = viewModel.passwordConfirm,
                    showPassword = viewModel.showPassword,
                    showPasswordConfirm = viewModel.showPasswordConfirm,
                    onEmailChange = { viewModel.updateEmail(it) },
                    onPasswordChange = { viewModel.updatePassword(it) },
                    onPasswordConfirmChange = { viewModel.updatePasswordConfirm(it) },
                    onShowPasswordClick = { viewModel.switchShowPassword() },
                    onShowPasswordConfirmClick = { viewModel.switchShowPasswordConfirm() })
            }

            //TransactionList-Screen
            composable(route = KassifyScreen.TransactionList.name) {
                TransactionListScreen(
                    onTransactionCardClicked = {
                        viewModel.getTransaction(it)
                        navController.navigate(KassifyScreen.TransactionDetails.name)
                    },
                    onAddButtonClicked = {
                        viewModel.createNewTransaction()
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
                        navController.navigate(KassifyScreen.TransactionEditor.name)
                    },
                    onDeleteButtonClicked = { viewModel.updateShowDeleteDialog() },
                    onDeleteConfirmedClicked = {
                        viewModel.deleteTransaction()
                        navController.popBackStack()
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_transaction_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCancelButtonClicked = {
                        navController.popBackStack()
                    },
                    onCancelDeleteDialogClicked = { viewModel.updateShowDeleteDialog() },
                    transaction = appUiState.currentTransaction,
                    showDeleteDialog = viewModel.showDeleteDialog,
                )
            }
            //TransactionEditor-Screen
            composable(route = KassifyScreen.TransactionEditor.name) {
                TransactionEditorScreen(onSaveButtonClicked = {
                    navController.popBackStack()
                    //Updating current transaction in transactionlist
                    //ToDO: Add a Function in Viewmodel
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_transaction_saved),
                        Toast.LENGTH_SHORT
                    ).show()

                }, onCancelButtonClicked = {
                    navController.popBackStack()
                },
                    transaction = appUiState.currentTransaction,
                    amountInput = appUiState.amountInput,
                    amountPrefix = viewModel.amountPrefix,
                    cashBalance = appUiState.cashBalance,
                    onDateChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onCheckedChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onAmountChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onCategoryChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onReceiptNoChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onTextChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    }
                )
            }
            //NewTransaction-Screen
            composable(route = KassifyScreen.NewTransaction.name) {
                TransactionEditorScreen(onSaveButtonClicked = {
                    navController.popBackStack()
                    //Adding new transaction to transactionList
                    viewModel.addTransaction(it)
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_transaction_saved),
                        Toast.LENGTH_SHORT
                    ).show()
                }, onCancelButtonClicked = {
                    navController.popBackStack()
                },
                    transaction = appUiState.currentTransaction,
                    amountInput = appUiState.amountInput,
                    amountPrefix = viewModel.amountPrefix,
                    cashBalance = appUiState.cashBalance,
                    onDateChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onCheckedChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onAmountChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onCategoryChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    },
                    onReceiptNoChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value

                        )
                    },
                    onTextChange = { fieldName, value ->
                        viewModel.updateCurrentTransaction(
                            fieldName,
                            value
                        )
                    }
                )
            }
        }
    }
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