package com.example.thecoffeeapp

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.example.thecoffeeapp.component.PageCard
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.example.thecoffeeapp.data.sampleCoffeeTypes
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme
import kotlinx.coroutines.launch

class BuyItem(
    val isRedeemed: Boolean,
    val coffeeType: CoffeeTypeData,
    val coffeeDetailData: CoffeeDetailData,
)
{
    fun getPrice() : Double {
        return if(isRedeemed) {
            0.00
        } else {
            coffeeDetailData.quantity * 3.00
        }
    }
}

@Composable
fun CartScreen(
    cartItems: List<BuyItem>,
    onBackButton: () -> Unit,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteItem: (BuyItem) -> Unit
) {
    PageCard(
        title = "My Cart",
        modifier = modifier,
        backButton = true,
        onBackClick = onBackButton,
        mainContent = {
            if (cartItems.isEmpty()) {
                Text(
                    "Your cart is empty",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                    )
            } else {
                Column {
                    LazyColumn (
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        cartItems.forEach{ buyItem ->
                            item {
                                CartItem(
                                    buyItem,
                                    onDeleteItem = { buyItem ->
                                        onDeleteItem(buyItem)
                                    },
                                )
                            }
                        }
                    }
                    Button(
                        onClick = onCheckoutClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Checkout")
                    }
                }
            }
        }
    )
}

@Composable
fun CartItem(
    buyItem: BuyItem,
    onDeleteItem: (BuyItem) -> Unit,
    modifier: Modifier = Modifier
) {

    val maxOffset = 175f
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
    ) {


        Row(
            modifier = Modifier
                .matchParentSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        offsetX.snapTo(0f) // Reset after delete
                        onDeleteItem(buyItem)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(Color(0xFFFFCDD2)),
                modifier = Modifier.fillMaxHeight().width(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -maxOffset / 2) {
                                    offsetX.animateTo(-maxOffset)
                                } else {
                                    offsetX.animateTo(0f)
                                }
                            }
                        },
                        onHorizontalDrag = { _, delta ->
                            coroutineScope.launch {
                                val newOffset = (offsetX.value + delta).coerceIn(-maxOffset, 0f)
                                offsetX.snapTo(newOffset)
                            }
                        }
                    )
                },

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 18.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(buyItem.coffeeType.drawable),
                    modifier = Modifier
                        .size(60.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(buyItem.coffeeType.text),
                        style = MaterialTheme.typography.bodyLarge
                            .copy(fontWeight = FontWeight.SemiBold),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = buyItem.coffeeDetailData.getDescription(),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "x${buyItem.coffeeDetailData.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "$${buyItem.getPrice().format(2)}",
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1
                )
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
private fun CartPreview() {
    TheCoffeeAppTheme {
        CartScreen(
            cartItems = listOf(
                BuyItem(
                    false,
                    coffeeType = sampleCoffeeTypes[0],
                    coffeeDetailData = CoffeeDetailData()
                ),
                BuyItem(
                    true,
                    coffeeType = sampleCoffeeTypes[1],
                    coffeeDetailData = CoffeeDetailData()
                )
            ),
            onBackButton = {},
            onCheckoutClick = {},
            onDeleteItem = {}
        )
    }
}

@Preview
@Composable
private fun CartItemPreview() {
    TheCoffeeAppTheme {
        CartItem(
            BuyItem(
                false,
                coffeeType = sampleCoffeeTypes[0],
                coffeeDetailData = CoffeeDetailData()
            ),
            onDeleteItem = {}
        )
    }
    
}

