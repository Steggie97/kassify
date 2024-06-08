package com.ls.kassify.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ls.kassify.R
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
    TransactionDetails(title = R.string.transaction_details),
    TransactionEditor(title = R.string.transaction_edit),
    NewTransaction(title = R.string.transaction_new)
}

@Composable
fun KassifyApp(
    viewModel: KassifyViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = KassifyScreen.valueOf(
        backStackEntry?.destination?.route ?: KassifyScreen.Login.name
    )
    Scaffold(
        topBar = {
            KassifyAppBar(
                title = currentScreen.title
            )
        }
    ) { innerPadding ->
        //val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = KassifyScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            //Login Screen
            composable(route = KassifyScreen.Login.name) {
                LogInScreen(
                    onForgotPasswordButtonClicked = {},
                    onLoginButtonClicked = {
                        navController.navigate(KassifyScreen.TransactionList.name)
                    },
                    onSignUpButtonClicked = {
                        navController.navigate(KassifyScreen.SignUp.name)
                    }
                )
            }

            //SignUp Screen
            composable(route = KassifyScreen.SignUp.name) {
                SignUpScreen(
                    onSignUpButtonClicked = {
                        navController.navigate(KassifyScreen.Login.name)
                    },
                    onCancelButtonClicked = {
                        navController.popBackStack(KassifyScreen.Login.name, inclusive = false)
                    }
                )
            }

            //TransactionList-Screen
            composable(route = KassifyScreen.TransactionList.name) {
                TransactionListScreen(
                    onTransactionCardClicked = {
                        navController.navigate(KassifyScreen.TransactionDetails.name)
                    },
                    onAddButtonClicked = {
                        navController.navigate(KassifyScreen.NewTransaction.name)
                    }
                )
            }
            //TransactionDetail-Screen
            composable(route = KassifyScreen.TransactionDetails.name) {
                TransactionDetailsScreen(
                    onEditButtonClicked = {
                        navController.navigate(KassifyScreen.TransactionEditor.name)
                    },
                    onDeleteButtonClicked = {
                        navController.popBackStack()
                    },
                    onCancelButtonClicked = {
                        navController.popBackStack()
                    }
                )
            }
            //TransactionEditor-Screen
            composable(route = KassifyScreen.TransactionEditor.name) {
                TransactionEditorScreen(
                    onSaveButtonClicked = {
                        navController.popBackStack()
                    },
                    onCancelButtonClicked = {
                        navController.popBackStack()
                    }

                )
            }
            //NewTransaction-Screen
            composable(route = KassifyScreen.NewTransaction.name) {
                TransactionEditorScreen(
                    onSaveButtonClicked = {
                        navController.popBackStack()
                    },
                    onCancelButtonClicked = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}


//Previews
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun KassifyAppPreview() {
    KassifyTheme(darkTheme = false) {
        KassifyApp()
    }

}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun KassifyAppDarkThemePreview() {
    KassifyTheme(darkTheme = true) {
        KassifyApp()
    }
}