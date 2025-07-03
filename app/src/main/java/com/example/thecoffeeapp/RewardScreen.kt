package com.example.thecoffeeapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thecoffeeapp.component.PageCard
import com.example.thecoffeeapp.component.RedeemCollection
import com.example.thecoffeeapp.data.RewardHistory
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme

import java.time.format.DateTimeFormatter

@Composable
fun RewardScreen(

    onRedeemReward: () -> Unit,
     modifier: Modifier = Modifier,
     viewModel: CoffeeViewModel,
    onRedeemLoyalty : () -> Unit,
) {


    val rewardHistoryList = viewModel.rewardList.collectAsState().value
    val point = viewModel.redeemPoint.collectAsState().value
    val coffeeCnt = viewModel.coffeeCnt.collectAsState().value


    PageCard(
        title = "Reward",
        mainContent = {
            Column(modifier = Modifier.fillMaxSize()) {
                RedeemCollection(
                    coffeeCnt = coffeeCnt,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onRedeemClick = onRedeemLoyalty
                )
                Spacer(Modifier.height(16.dp))
                PointsSection(
                    point = point,
                    onClicked = onRedeemReward,
                    modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(Modifier.height(16.dp))
                RewardHistoryList(rewardHistoryList, modifier = Modifier.padding(horizontal = 24.dp))
            }
        },
        modifier = modifier
    )
}


@Composable
fun PointsSection(
    point: Int,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("My Points:", color = MaterialTheme.colorScheme.onPrimary)
                Text("$point", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineMedium)
            }

            Button(
                onClick = onClicked,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryFixed),
            ) {
                Text(
                    text = "Redeem drinks",
                    color = MaterialTheme.colorScheme.onPrimaryFixed,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun RewardHistoryList(list: List<RewardHistory>, modifier: Modifier = Modifier) {
    Column(modifier)
    {
        Text(
            text = "Reward History",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        LazyColumn(
        ) {
            items(list) { item ->
                RewardHistoryItem(item)
            }
        }
    }
}


@Composable
fun RewardHistoryItem(rewardHistory: RewardHistory, modifier: Modifier = Modifier) {
    val formatter = DateTimeFormatter.ofPattern("d MMMM | hh:mm a")
    val formattedDate = rewardHistory.dateTime.format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(stringResource(rewardHistory.type.text), style = MaterialTheme.typography.bodyLarge)
            Text(
                formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
        Text("+ ${rewardHistory.points} Pts", style = MaterialTheme.typography.bodyLarge)
    }
}
