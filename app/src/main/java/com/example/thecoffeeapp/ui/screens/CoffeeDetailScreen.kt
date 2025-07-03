package com.example.thecoffeeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.R
import com.example.thecoffeeapp.ui.component.PageCard
import com.example.thecoffeeapp.data.sampleCoffeeTypes
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme

@Composable
fun CoffeeDetailScreen(
    isRedeemed: Boolean,
    coffeeData: CoffeeTypeData?,
    coffeeDetailDataState: CoffeeDetailDataState,
    onBackButton: () -> Unit,
    onAddToCart: () -> Unit,
    onRightButtonClick: () -> Unit,
    modifier: Modifier
) {
    PageCard(
        title = "Details",
        backButton = true,
        onBackClick = onBackButton,
        rightButton = true,
        rightButtonIcon = Icons.Outlined.ShoppingCart,
        onRightButtonClick = onRightButtonClick,
        mainContent = {
            Surface (modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.large
            ) {
                Image(
                    painter = painterResource(coffeeData?.drawable ?: R.drawable.type1_americano),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp, vertical = 10.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Column (modifier = Modifier.padding(horizontal = 25.dp, vertical = 40.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Americano", style = MaterialTheme.typography.titleMedium)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { if (coffeeDetailDataState.quantity > 1) coffeeDetailDataState.quantity-- }) {
                            Icon(Icons.Default.Remove, contentDescription = "Minus")
                        }
                        Text(coffeeDetailDataState.quantity.toString())
                        IconButton(onClick = { coffeeDetailDataState.quantity++ }) {
                            Icon(Icons.Default.Add, contentDescription = "Plus")
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                 Row(
                   Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically)
                 {
                    // Shot selection
                    Text(
                        text = "Shot",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Row (
                    ) {
                        listOf(
                            CoffeeDetailData.Shot.SINGLE,
                            CoffeeDetailData.Shot.DOUBLE,
                            CoffeeDetailData.Shot.TRIPLE
                        ).forEach { status ->
                            FilterChip(
                                selected = coffeeDetailDataState.shot == status,
                                onClick = { coffeeDetailDataState.shot = status },
                                label = {
                                    when (status) {
                                        CoffeeDetailData.Shot.SINGLE -> Text("Single")
                                        CoffeeDetailData.Shot.DOUBLE -> Text("Double")
                                        CoffeeDetailData.Shot.TRIPLE -> Text("Triple")
                                    }
                                })
                            Spacer(Modifier.width(8.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Select hot/cold

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text("Select", style = MaterialTheme.typography.labelLarge)
                    Row {
                        IconToggleButton(
                            checked = coffeeDetailDataState.select == CoffeeDetailData.Select.TAKE_AWAY,
                            onCheckedChange = {
                                coffeeDetailDataState.select = CoffeeDetailData.Select.TAKE_AWAY
                            }) {
                            Icon(
                                Icons.Default.LocalCafe,
                                contentDescription = "Take Away",
                                modifier = Modifier.size(24.dp),
                                tint = if (coffeeDetailDataState.select == CoffeeDetailData.Select.TAKE_AWAY)
                                    Color.Gray.copy(1f) else Color.Gray.copy(alpha = 0.3f)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        IconToggleButton(
                            checked = coffeeDetailDataState.select == CoffeeDetailData.Select.IN_HOUSE,
                            onCheckedChange = {
                                coffeeDetailDataState.select = CoffeeDetailData.Select.IN_HOUSE
                            }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_coffee),
                                contentDescription = "In House",
                                modifier = Modifier.size(24.dp),
                                tint = if (coffeeDetailDataState.select == CoffeeDetailData.Select.IN_HOUSE)
                                    Color.Gray.copy(1f) else Color.Gray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Size selection

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text("Size", style = MaterialTheme.typography.labelLarge)
                    Row {
                        listOf(
                            CoffeeDetailData.Size.SMALL,
                            CoffeeDetailData.Size.MEDIUM,
                            CoffeeDetailData.Size.LARGE,
                        ).forEach { status ->
                            val colorFilter = if (coffeeDetailDataState.size == status) {
                                ColorFilter.tint(Color.Gray.copy(alpha = 1f))
                            } else ColorFilter.tint(Color.Gray.copy(alpha = 0.5f))
                            IconToggleButton(
                                checked = coffeeDetailDataState.size == status,
                                onCheckedChange = { coffeeDetailDataState.size = status }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_coffee),
                                    colorFilter = colorFilter,
                                    contentDescription = when (status) {
                                        CoffeeDetailData.Size.SMALL -> "Small Size"
                                        CoffeeDetailData.Size.MEDIUM -> "Medium Size"
                                        CoffeeDetailData.Size.LARGE -> "Large Size"
                                    },
                                    modifier = Modifier.size(
                                        when (status) {
                                            CoffeeDetailData.Size.SMALL -> 20.dp
                                            CoffeeDetailData.Size.MEDIUM -> 24.dp
                                            CoffeeDetailData.Size.LARGE -> 28.dp
                                        }
                                    ),
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Ice level

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Text("Ice", style = MaterialTheme.typography.labelLarge)
                    Row {
                        listOf(
                            CoffeeDetailData.Ice.LITTLE,
                            CoffeeDetailData.Ice.NORMAL,
                            CoffeeDetailData.Ice.EXTRA
                        ).forEach { status ->
                            IconToggleButton(
                                checked = coffeeDetailDataState.ice == status,
                                onCheckedChange = { coffeeDetailDataState.ice = status }
                            ) {
                                Icon(
                                    modifier = Modifier.size(
                                        when (status) {
                                            CoffeeDetailData.Ice.NORMAL -> 24.dp
                                            CoffeeDetailData.Ice.EXTRA -> 28.dp
                                            CoffeeDetailData.Ice.LITTLE -> 20.dp
                                        }
                                    ),
                                    tint = if (coffeeDetailDataState.ice == status) Color.Gray else Color.Gray.copy(alpha = 0.5f),
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = when (status) {
                                        CoffeeDetailData.Ice.NORMAL -> "Normal Ice"
                                        CoffeeDetailData.Ice.EXTRA -> "Extra Ice"
                                        CoffeeDetailData.Ice.LITTLE -> "Little Ice"
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Total Amount
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Amount", style = MaterialTheme.typography.bodyLarge)
                    Text("$${if (isRedeemed) 0.00 else (coffeeDetailDataState.quantity * 3.00).format(2)}", style = MaterialTheme.typography.titleMedium)
                }
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Add to Cart",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

class CoffeeDetailDataState(
    quantity: Int = 1,
    shot: CoffeeDetailData.Shot = CoffeeDetailData.Shot.SINGLE,
    size: CoffeeDetailData.Size = CoffeeDetailData.Size.MEDIUM,
    select: CoffeeDetailData.Select = CoffeeDetailData.Select.TAKE_AWAY,
    ice: CoffeeDetailData.Ice = CoffeeDetailData.Ice.NORMAL,
) {
    var quantity by mutableStateOf(quantity)
    var shot by mutableStateOf(shot)
    var size by mutableStateOf(size)
    var select by mutableStateOf(select)
    var ice by mutableStateOf(ice)


       companion object {
        val Saver: Saver<CoffeeDetailDataState, *> = listSaver(
            save = { listOf(it.quantity, it.shot, it.size, it.select, it.ice) },
            restore = {
                CoffeeDetailDataState(
                    quantity = it[0] as Int,
                    shot = it[1] as CoffeeDetailData.Shot,
                    size = it[2] as CoffeeDetailData.Size,
                    select = it[3] as CoffeeDetailData.Select,
                    ice = it[4] as CoffeeDetailData.Ice
                )
            }
        )
    }
}

class CoffeeDetailData(
    val quantity: Int = 1,
    val shot: Shot = Shot.SINGLE,
    val size: Size = Size.MEDIUM,
    val select: Select = Select.TAKE_AWAY,
    val ice: Ice = Ice.NORMAL
) {
    enum class Shot {
        SINGLE, DOUBLE, TRIPLE
    }

    enum class Size {
        SMALL, MEDIUM, LARGE
    }

    enum class Select {
        TAKE_AWAY, IN_HOUSE
    }

    enum class Ice {
        LITTLE, NORMAL, EXTRA
    }
    fun getDescription(): String {
        var answer: String = "${
            when (shot) {
                Shot.SINGLE -> "single"
                Shot.DOUBLE -> "double"
                Shot.TRIPLE -> "triple"
            }
        }, ${
            when (size) {
                Size.SMALL -> "small"
                Size.MEDIUM -> "medium"
                Size.LARGE -> "large"
            }
        }, ${
            when (select) {
                Select.TAKE_AWAY -> "Take Away"
                Select.IN_HOUSE -> "In House"
            }
        }, ${
            when (ice) {
                Ice.LITTLE -> "Little Ice"
                Ice.NORMAL -> "Normal Ice"
                Ice.EXTRA -> "Extra Ice"
            }
        }"
        return answer
    }
}
@Composable
fun rememberCoffeeDetailData(initial: CoffeeDetailDataState): CoffeeDetailDataState {
    return rememberSaveable(initial, saver = CoffeeDetailDataState.Saver) {
        CoffeeDetailDataState(
            quantity = initial.quantity,
            shot = initial.shot,
            size = initial.size,
            select = initial.select,
            ice = initial.ice
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoffeeDetailScreenPreview() {
    TheCoffeeAppTheme {
        CoffeeDetailScreen(
            false,
            coffeeData = sampleCoffeeTypes.first(),
            coffeeDetailDataState = CoffeeDetailDataState(),
            onBackButton = {},
            onAddToCart = {},
            onRightButtonClick = {},
            modifier = Modifier
        )
    }

}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
fun Float.format(digits: Int) = "%.${digits}f".format(this)

@Composable
fun optionSelect(
    modifier: Modifier = Modifier
) {
    
}