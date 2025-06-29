package com.example.thecoffeeapp.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme

@Composable
fun WrapBox(
    title: @Composable () -> Unit,
    mainContent: @Composable () -> Unit,
    modifier: Modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
    Surface (
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
        ) {
            Surface (
                color = Color.Transparent,
                modifier = Modifier.padding(horizontal = 26.dp)
            ) {
                title()
            }

            Surface (
                color = Color.Transparent,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                mainContent()
            }
        }
    }
}
@Preview
@Composable
fun WrapBoxPreview() {
    TheCoffeeAppTheme {
        WrapBox(
            title = { Text(
                text = "hello world",
                style = MaterialTheme.typography.titleMedium,
            )},
            mainContent = {},
        )
    }
}
