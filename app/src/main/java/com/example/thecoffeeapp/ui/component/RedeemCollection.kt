package com.example.thecoffeeapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.R

@Composable
fun RedeemCollection(
    modifier: Modifier = Modifier,
    coffeeCnt: Int,
    onRedeemClick: () -> Unit,
) {
    val coffeeList = List(8) { index -> index < coffeeCnt}
    WrapBox(
        title = {
            Row(
                modifier = Modifier.padding(vertical = 14.dp).fillMaxWidth()
            ) {
                Text(
                    text = "Loyalty Card",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text (
                    text = "${coffeeCnt}/8",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        mainContent = {
            Surface (
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 15.dp)
                ) {
                    for (i in 0..7) {
                        val colorFilter = if (i < coffeeCnt) null else ColorFilter.tint(Color.Gray.copy(alpha = 0.3f))
                        Image(
                            painter = painterResource(R.drawable.ic_coffee),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.weight(1f),
                            colorFilter = colorFilter
                        )
                    }
                }
            }
        },
        modifier = modifier.clip(RoundedCornerShape(12.dp))
    )

    if (coffeeCnt >= 8) {
        Button(
            onClick = onRedeemClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Redeem Coffee",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

