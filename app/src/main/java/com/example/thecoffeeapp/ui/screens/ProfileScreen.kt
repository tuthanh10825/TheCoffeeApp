package com.example.thecoffeeapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thecoffeeapp.ui.component.PageCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thecoffeeapp.viewmodel.CoffeeViewModel
import com.example.thecoffeeapp.data.sampleProfileInfo
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
                  onBackButton: () -> Unit,
                  viewModel: CoffeeViewModel
) {

    val profileInfo = viewModel.userInfo.collectAsState().value
    PageCard(
        title = "Profile",
        modifier = modifier,
        backButton = true,
        onBackClick = onBackButton,
        mainContent =  {
            ProfileItem(Icons.Outlined.Person, "Full name", profileInfo?.name?: "N/A",
                onSave = {newName ->
                    viewModel.updateUserInfo(profileInfo!!.copy(name = newName))
                }
            )
            ProfileItem(Icons.Outlined.Phone, "Phone number", profileInfo?.phone?: "N/A",
                onSave = { newPhone ->
                    viewModel.updateUserInfo(profileInfo!!.copy(phone = newPhone))
                }
            )
            ProfileItem(Icons.Outlined.Mail, "Email", profileInfo?.email?: "N/A",
                onSave = { newEmail ->
                    viewModel.updateUserInfo(profileInfo!!.copy(email = newEmail))
                },
            )
            ProfileItem(Icons.Outlined.LocationOn, "Address", profileInfo?.address?: "N/A",
                onSave = { newAddress ->
                    viewModel.updateUserInfo(profileInfo!!.copy(address = newAddress))
                }
            )
    })
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    value: String,
    onSave : (String) -> Unit,
    trailingIcon: ImageVector? = Icons.Outlined.Edit, // Optional, configurable
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var localValue by remember { mutableStateOf(value) }

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
            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = localValue,
                        onValueChange = {
                            localValue = it
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                            disabledIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    IconButton(
                        onClick = {
                            onSave(localValue)
                            isEditing = false
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Save,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
            else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = value,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Clip,
                    )
                    if (trailingIcon != null) {
                        IconButton(
                            onClick = { isEditing = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
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
            onSave = { /* Handle click */ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
   TheCoffeeAppTheme {
       ProfileScreen(
           onBackButton = {},
           viewModel = viewModel<CoffeeViewModel>().apply {
                updateUserInfo(sampleProfileInfo)
           }
       )
   }
}