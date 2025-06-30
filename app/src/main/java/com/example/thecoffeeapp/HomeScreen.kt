package com.example.thecoffeeapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.thecoffeeapp.component.RedeemCollection
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.thecoffeeapp.component.WrapBox
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme
import kotlinx.coroutines.delay
import java.time.LocalTime


@Composable
fun HomeScreen(
    username: String,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxHeight()) {

        Greeting(
            username = username,
            onProfileClick = onProfileClick,
            modifier = Modifier.padding(horizontal = 26.dp, vertical = 30.dp)
        )
        RedeemCollection(modifier = Modifier.padding(horizontal = 24.dp))
        CoffeeTypeChoosing(modifier = modifier.padding(top = 40.dp).weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Scaffold (
        floatingActionButton = {
            BottomNavBar(
                Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.systemBarsPadding().navigationBarsPadding()

    ) { padding ->
        HomeScreen(username = "Tu Thanh", {}, modifier = Modifier.padding(padding))
    }
}



data class CoffeeTypeData (
    //This holds the drawable resource.
    @DrawableRes val drawable: Int,
    //This holds the string resource, or we can understand it as the identifier for the of the drink.
    @StringRes val text: Int
)

private val iconList = listOf(
    R.drawable.type1_americano to R.string.type1_americano,
    R.drawable.type2_cappucino to R.string.type2_cappuccino,
    R.drawable.type3_mocha to R.string.type3_mocha,
    R.drawable.type4_flatwhite to R.string.type4_flatwhite,
).map { CoffeeTypeData(it.first, it.second)}


@Composable
fun CoffeeTypeCard(
    modifier: Modifier = Modifier.Companion,
    @DrawableRes drawable: Int,
    @StringRes text: Int
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            modifier = Modifier.Companion
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Companion.Crop,
                modifier = Modifier.Companion
                    .padding(horizontal = 22.dp, vertical = 24.dp)
                    .width(110.dp)
            )
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.Companion.padding(bottom = 28.dp)
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun CoffeeTypeCardPreview() {
    TheCoffeeAppTheme {
        CoffeeTypeCard(
            modifier = Modifier.Companion.padding(8.dp),
            drawable = R.drawable.type2_cappucino,
            text = R.string.type2_cappuccino
        )
    }
}

@Composable
fun CoffeeTypeCollectionGrid(
    list: List<CoffeeTypeData>,
    modifier: Modifier = Modifier.Companion
){
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(iconList) { item ->
            CoffeeTypeCard(Modifier.Companion, item.drawable, item.text)
        }
    }
}

@Composable
fun CoffeeTypeChoosing(
    modifier: Modifier = Modifier.Companion
) {
    WrapBox(
        title = {
            Text(
                text = "Choose your coffee",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.Companion.padding(vertical = 24.dp)
            )
        },
        mainContent = {
            CoffeeTypeCollectionGrid(
                listOf(),
                Modifier.Companion.padding(bottom = 24.dp)
            )
        },
        modifier = modifier
            .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp)),
    )
}


@Preview(showBackground = true)
@Composable
fun CoffeeTypeCollectionGridPreview() {
    TheCoffeeAppTheme {
        CoffeeTypeChoosing()
    }
}

@Composable
fun Greeting(
    username: String,
    onProfileClick: () -> Unit,
    onOrderClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var greeting by remember { mutableStateOf(getGreeting()) }

    // Update every minute
    LaunchedEffect(Unit) {
        while (true) {
            greeting = getGreeting()
            delay(60_000L)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Companion.Gray)
            )
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Companion.Bold)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconButton(
                onClick = onOrderClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Cart",
                    modifier = Modifier.Companion.size(26.dp)
                )
            }
            IconButton(
                onClick = onProfileClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheCoffeeAppTheme {
        Greeting("Tu Thanh", {})
    }
}

fun getGreeting(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..21 -> "Good evening"
        else -> "Good night"
    }
}


