package com.example.thecoffeeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thecoffeeapp.component.PageCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalPolice
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.data.sampleProfileInfo
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme

data class ProfileInfo(
    val name: String,
    val email: String,
    val phone: String,
    val address: String
)
@Composable
fun ProfileScreen(profileInfo: ProfileInfo, modifier: Modifier = Modifier,
                  onBackButton: () -> Unit, onTrailingIconClick: () -> Unit) {
    PageCard(
        title = "Profile",
        modifier = modifier,
        backButton = true,
        onBackClick = onBackButton,
        mainContent =  {
            ProfileItem(Icons.Outlined.Person, "Full name", profileInfo.name,
                onTrailingIconClick = onTrailingIconClick)
            ProfileItem(Icons.Outlined.Phone, "Phone number", profileInfo.phone,
                onTrailingIconClick = onTrailingIconClick)
            ProfileItem(Icons.Outlined.Mail, "Email", profileInfo.email,
                onTrailingIconClick = onTrailingIconClick)
            ProfileItem(Icons.Outlined.LocationOn, "Address", profileInfo.address,
                onTrailingIconClick = onTrailingIconClick)
    })
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    value: String,
    onTrailingIconClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = Icons.Outlined.Edit, // Optional, configurable
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(7.dp)
                    .fillMaxSize(),
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = Int.MAX_VALUE,
                overflow = TextOverflow.Clip,
            )

        }

        if (onTrailingIconClick != null && trailingIcon != null) {
            IconButton(onClick = onTrailingIconClick) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = "Action",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileItemPreview() {
    TheCoffeeAppTheme {
       ProfileItem(icon = Icons.Outlined.Person,
            label = "Name",
            value = "John Doe",
            onTrailingIconClick = { /* Handle click */ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
   TheCoffeeAppTheme {
       ProfileScreen(
           profileInfo = sampleProfileInfo,
           onBackButton = {},
           onTrailingIconClick = {},
       )
   }
}