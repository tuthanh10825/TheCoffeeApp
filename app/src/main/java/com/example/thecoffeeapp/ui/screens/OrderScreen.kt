package com.example.thecoffeeapp.ui.screens

import java.time.format.DateTimeFormatter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Place
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.thecoffeeapp.ui.component.PageCard
import com.example.thecoffeeapp.data.local.entity.OrderInfo



import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme


@Composable
fun OrderItem(order: OrderInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Date and Time

        Row {
            Text(
                text = order.orderTime.format(DateTimeFormatter.ofPattern("dd MMMM | hh:mm a")),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${"%.2f".format(order.cost)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))


            // Coffee Icon + Name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocalCafe,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(order.coffeeType.text),
                    style = MaterialTheme.typography.bodySmall
                )
            }


        Spacer(modifier = Modifier.height(10.dp))

        // Address row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Place,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = order.address,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
fun OrderList(
    list: List<OrderInfo>,
    modifier: Modifier = Modifier,
    isHistoryOrders: Boolean = true,
    onMoveToHistory: (OrderInfo) -> Unit = {},
) {
    LazyColumn (modifier = modifier
        .padding(bottom = 80.dp)
    ) {
       items(list) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OrderItem(item)
                if (!isHistoryOrders) {
                    // If it's an ongoing order, add a button to move it to history
                    Button(
                        onClick = { onMoveToHistory(item) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = "Move to History")
                    }
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
            }
       }
    }
}


//// TODO: Use ViewModel to handle the business logic and state management
//@Composable
//fun OrderScreen(modifier: Modifier = Modifier) {
//    var selectedTab by rememberSaveable { mutableStateOf(0) }
//    var option = listOf("On going", "History")
//    Column(modifier = modifier) {
//        Text(
//            text = "My Order",
//            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(top = 24.dp, bottom = 16.dp)
//        )
//        OrderTabs(
//            selectedTab = selectedTab,
//            option = option,
//            onTabSelected = { selectedTab = it },
//        )
//        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
//        when (selectedTab) {
//            0 -> OrderList(sampleOrders, modifier = Modifier.padding(32.dp))
//            1 -> OrderList(sampleHist, modifier = Modifier.padding(32.dp))
//        }
//    }
//}

@Composable
fun OrderScreen(
    onGivenOrder: (OrderInfo) -> Unit = {},
    modifier: Modifier = Modifier,
    onGoingOrderList: List<OrderInfo>, orderHistoryList: List<OrderInfo>) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var option = listOf("On going", "History")
    PageCard(
        title = "My Order",
        mainContent = {
            Column(
                modifier = modifier
            )
            {
                OrderTabs(
                    selectedTab = selectedTab,
                    option = option,
                    onTabSelected = { selectedTab = it },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
                when (selectedTab) {
                    0 -> OrderList(
                        onGoingOrderList, modifier = Modifier.padding(32.dp), false,
                        onMoveToHistory = { order ->
                            onGivenOrder(order) // Pass the order to the callback
                        }
                    )

                    1 -> OrderList(orderHistoryList, modifier = Modifier.padding(32.dp))
                }
            }
        },
        backButton = false,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTabs(
    selectedTab: Int,
    option: List<String>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTab,
        modifier = modifier
    ) {
        option.forEachIndexed { index, text ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = if (index == selectedTab)
                                MaterialTheme.colorScheme.onBackground
                            else
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        ),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun PageCardPreview() {
    TheCoffeeAppTheme {

    }
}
