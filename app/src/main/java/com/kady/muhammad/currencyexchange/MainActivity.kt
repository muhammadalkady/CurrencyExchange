@file:OptIn(ExperimentalMaterial3Api::class)

package com.kady.muhammad.currencyexchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import com.kady.muhammad.currencyexchange.ui.theme.CurrencyExchangeTheme
import com.kady.muhammad.exchange.presentation.CurrencyConverter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyExchangeTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = {
                        Text(text = "Currency Exchange")
                    })
                }
                ) { innerPadding ->
                    CurrencyConverter(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

