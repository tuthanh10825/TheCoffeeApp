package com.example.thecoffeeapp

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.component.PageCard
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme
import java.time.LocalDate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.thecoffeeapp.data.sampleRedeemList


@Composable
fun RedeemScreen(
    redeemList: List<RedeemInfo>,
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit,
    onRedeem: (CoffeeTypeData, RedeemInfo) -> Unit
) {
    PageCard(
        title = "Redeem",
        modifier = modifier,
        backButton = true,
        onBackClick = onBackButton,
        mainContent = {
            LazyColumn {
                items(redeemList) { redeemInfo ->
                    RedeemItem(
                        redeemInfo = redeemInfo,
                        onRedeem = { onRedeem(redeemInfo.type, redeemInfo) }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun RedeemScreenPreview() {
    TheCoffeeAppTheme {
        RedeemScreen(
            redeemList = sampleRedeemList,
            onBackButton = {},
            onRedeem = { coffeeType, redeemInfo ->
                // Handle redeem action
            }
        )
    }
}

data class RedeemInfo(
    val type: CoffeeTypeData,
    val validDate: LocalDate,
    val pointsRequired: Int,
)

@Composable
fun RedeemItem(
    redeemInfo: RedeemInfo,
    onRedeem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = redeemInfo.type.drawable),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = stringResource(redeemInfo.type.text),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Valid until ${redeemInfo.validDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Button(
            onClick = { onRedeem() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F3E46)),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "${redeemInfo.pointsRequired} pts",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }
    }
}
