@file:OptIn(ExperimentalMaterial3Api::class)

package com.kady.muhammad.currencyexchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kady.muhammad.currencyexchange.ui.theme.CurrencyExchangeTheme
import com.kady.muhammad.exchange.presentation.CurrencyConverter
import com.kady.muhammad.exchange.presentation.model.CurrencyExchangeEvent
import com.kady.muhammad.history.domain.model.DomainExchangeHistoryModel
import com.kady.muhammad.history.presentation.ExchangeHistory
import com.kady.muhammad.history.presentation.viewmodel.ExchangeHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point of the Currency Exchange application.
 *
 * This activity is annotated with `@AndroidEntryPoint` to enable dependency injection using Hilt.
 * It is responsible for initializing the app's UI and applying edge-to-edge design for a modern look.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val historyViewModel by viewModels<ExchangeHistoryViewModel>()

    /**
     * Called when the activity is starting. This is where most initialization should occur.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this contains the data it most recently supplied in `onSaveInstanceState`. Otherwise, it is null.
     *
     * In this overridden method:
     * 1. Edge-to-edge design is enabled to ensure content flows seamlessly behind system bars.
     * 2. The application's composable content is set using Jetpack Compose's `setContent` function.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display to create a more immersive user experience.
        enableEdgeToEdge()
        // Set the root composable content for this activity.
        setContent {
            CurrencyExchangeRoot(historyViewModel)
        }
    }
}

/**
 * Root Composable function for the Currency Exchange application.
 *
 * @param historyViewModel The ViewModel for managing exchange history.
 *
 * This function serves as the entry point for the UI, applying the application's theme and
 * setting up the navigation graph.
 */
@Composable
fun CurrencyExchangeRoot(historyViewModel: ExchangeHistoryViewModel) {
    // Apply the CurrencyExchangeTheme to provide a consistent look and feel throughout the app.
    CurrencyExchangeTheme {
        // Create a NavController to manage app navigation.
        val navController = rememberNavController()
        // Set up the NavigationGraph to define available destinations and navigation behavior.
        NavigationGraph(navController, historyViewModel)
    }
}

/**
 * NavigationGraph defines the navigation structure of the application.
 *
 * @param navController The NavController used to navigate between screens.
 * @param historyViewModel The ViewModel for managing exchange history.
 *
 * This function uses a NavHost to define the navigation graph, specifying the start destination
 * and all available composable destinations.
 */
@Composable
fun NavigationGraph(navController: NavHostController, historyViewModel: ExchangeHistoryViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        // Home screen destination
        composable("home") {
            HomeScreen(navController, onCurrencyExchangeResult = { result ->
                historyViewModel.add(
                    DomainExchangeHistoryModel(
                        sourceSymbol = result.sourceCurrencySymbol.symbol,
                        targetSymbol = result.targetCurrencySymbol.symbol,
                        sourceAmount = result.sourceAmount,
                        targetAmount = result.targetAmount,
                        timestamp = System.currentTimeMillis()
                    )
                )
            })
        }
        // History screen destination
        composable("history") {
            HistoryScreen(
                navController,
                historyViewModel
            )
        }
    }
}

/**
 * HomeScreen displays the main currency conversion interface.
 *
 * @param navController The NavController used to navigate to other screens.
 * @param onCurrencyExchangeResult Callback function to handle the result of a currency exchange.
 *
 * This screen uses a Scaffold to provide a consistent layout structure, including a top bar
 * and a content area for the currency converter.
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    onCurrencyExchangeResult: (CurrencyExchangeEvent.CurrencyExchangeResult) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopBar(navController) }) { innerPadding ->
        // The CurrencyConverter is padded to avoid overlapping with the top bar.
        CurrencyConverter(
            modifier = Modifier.padding(innerPadding),
            onCurrencyExchangeResult = onCurrencyExchangeResult
        )
    }
}

/**
 * HomeTopBar is the top bar displayed on the HomeScreen.
 *
 * @param navController The NavController used to navigate to the history screen.
 *
 * This top bar includes a title and an action button for navigating to the Exchange History screen.
 */
@Composable
fun HomeTopBar(navController: NavHostController) {
    TopAppBar(title = { Text(text = "Currency Exchange") }, actions = {
        // Navigate to the HistoryScreen when the history icon is clicked.
        IconButton(onClick = { navController.navigate("history") }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.history_24px),
                contentDescription = "History"
            )
        }
    })
}

/**
 * HistoryTopBar is the top bar displayed on the HistoryScreen.
 *
 * @param navController The NavController used to navigate back to the home screen.
 *
 * This top bar only includes a title specific to the Exchange History.
 */
@Composable
fun HistoryTopBar(navController: NavHostController) {
    TopAppBar(title = { Text(text = "Exchange History") }, navigationIcon = {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back"
            )
        }
    })
}

/**
 * HistoryScreen displays the user's exchange history.
 *
 * This screen uses a Scaffold to provide a consistent layout structure, including a top bar
 * and a content area for displaying the exchange history.
 */
@Composable
fun HistoryScreen(
    navController: NavHostController,
    historyViewModel: ExchangeHistoryViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HistoryTopBar(navController) }) { innerPadding ->
        // The ExchangeHistory is padded to avoid overlapping with the top bar.
        ExchangeHistory(
            modifier = Modifier.padding(innerPadding),
            viewModel = historyViewModel
        )
    }
}
