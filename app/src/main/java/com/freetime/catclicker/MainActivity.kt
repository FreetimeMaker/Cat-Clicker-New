package com.freetime.catclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.freetime.catclicker.ui.theme.CatClickerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatClickerTheme {
                val navController = rememberNavController()
                val gameViewModel: GameViewModel = viewModel()

                // Auto-clicker logic
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(1000)
                        gameViewModel.addAutoMoney()
                    }
                }

                NavHost(navController = navController, startDestination = "game") {
                    composable("game") {
                        CatClickerGame(
                            cats = gameViewModel.cats,
                            clickPower = gameViewModel.clickPower,
                            autoClickRate = gameViewModel.autoClickRate,
                            onAddClick = { gameViewModel.addClick() },
                            onOpenShop = { navController.navigate("shop") }
                        )
                    }
                    composable("shop") {
                        ShopScreen(
                            cats = gameViewModel.cats,
                            clickPower = gameViewModel.clickPower,
                            autoClickRate = gameViewModel.autoClickRate,
                            onBuyClickPower = { gameViewModel.buyClickPower() },
                            onBuyAutoClicker = { gameViewModel.buyAutoClicker() },
                            onBack = { navController.popBackStack() },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CatClickerGame(
    cats: Int,
    clickPower: Int,
    autoClickRate: Int,
    onAddClick: () -> Unit,
    onOpenShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, end = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onOpenShop) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shop",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$cats Cats",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Click Power: $clickPower",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Auto Click: $autoClickRate/s",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat1),
                    contentDescription = "Clickable Cat",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ShopScreen(
    cats: Int,
    clickPower: Int,
    autoClickRate: Int,
    onBuyClickPower: () -> Unit,
    onBuyAutoClicker: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onBack) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Shop",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Your Cats: $cats",
                style = MaterialTheme.typography.headlineSmall
            )

            ShopItem(
                name = "Upgrade Click",
                description = "Increase click power by 1",
                cost = 10 * clickPower,
                onBuy = onBuyClickPower,
                canAfford = cats >= 10 * clickPower
            )

            ShopItem(
                name = "Auto Clicker",
                description = "Increase auto-click by 1/s",
                cost = 50 * (autoClickRate + 1),
                onBuy = onBuyAutoClicker,
                canAfford = cats >= 50 * (autoClickRate + 1)
            )
        }
    }
}

@Composable
fun ShopItem(
    name: String,
    description: String,
    cost: Int,
    onBuy: () -> Unit,
    canAfford: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, style = MaterialTheme.typography.titleLarge)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Cost: $cost Cats", color = if (canAfford) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = onBuy,
            enabled = canAfford
        ) {
            Text("Buy")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatClickerPreview() {
    CatClickerTheme {
        CatClickerGame(
            cats = 0,
            clickPower = 1,
            autoClickRate = 0,
            onAddClick = {},
            onOpenShop = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShopPreview() {
    CatClickerTheme {
        ShopScreen(
            cats = 0,
            clickPower = 1,
            autoClickRate = 0,
            onBuyClickPower = {},
            onBuyAutoClicker = {},
            onBack = {}
        )
    }
}
