package com.example.thecoffeeapp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme

@Composable
fun PageCard(
    title: String,
    mainContent: @Composable () -> Unit,
    backButton: Boolean = false,
    onBackClick: () -> Unit = {},
    rightButton: Boolean = false,
    rightButtonIcon: ImageVector? = null,
    onRightButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 26.dp),
            contentAlignment = Alignment.Center
        ) {
            // Title centered
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            )

            // Back button aligned to start
            if (backButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart).size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                    )
                }
            }
            if (rightButton && rightButtonIcon != null) {
                IconButton(
                    onClick = onRightButtonClick,
                    modifier = Modifier.align(Alignment.CenterEnd).size(26.dp)
                ) {
                    Icon(
                        imageVector = rightButtonIcon,
                        contentDescription = "Right Button",
                    )
                }
            }
        }
        mainContent()
    }
}
