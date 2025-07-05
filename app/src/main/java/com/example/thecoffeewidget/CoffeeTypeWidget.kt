package com.example.thecoffeewidget

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.background
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.material3.ColorProviders
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextDefaults.defaultTextStyle
import androidx.glance.text.TextStyle
import com.example.thecoffeeapp.MainActivity
import com.example.thecoffeeapp.R
import com.example.thecoffeeapp.navigation.CoffeeDetail
import com.example.thecoffeeapp.ui.screens.CoffeeTypeData
import com.example.thecoffeeapp.data.local.db.CoffeeRoomDatabase
import com.example.thecoffeeapp.ui.theme.darkScheme
import com.example.thecoffeeapp.ui.theme.lightScheme

object GlanceColorScheme {
    val colors = ColorProviders(
        light = lightScheme,
        dark = darkScheme
    )
}

class MyAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CoffeeTypeWidget()
}

class CoffeeTypeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // Set the color scheme for the widget
            GlanceTheme(
                GlanceColorScheme.colors,
                content = {
                    CoffeeTypeWidgetContent()
                }
            )
        }
    }
}

@Composable
fun CoffeeTypeWidgetContent(
    list: List<CoffeeTypeData> = sampleCoffeeTypes,
) {
    val context = LocalContext.current
    val CoffeeTypeKey = ActionParameters.Key<Int>("coffeeType")
    val NavigateTo = ActionParameters.Key<String>("navigateTo")

    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primary), // Use color from resources
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Hello, want to order anything now?",
            modifier = GlanceModifier.padding(all = 8.dp)
                .background(MaterialTheme.colorScheme.surface),
        )
        list.chunked(2).forEach { rowItems ->
            Row(
                modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
            ) {
                rowItems.forEach { item ->
                    Column(
                        modifier = GlanceModifier
                            .padding(8.dp)
                            .defaultWeight()
                            .clickable(
                                actionStartActivity<MainActivity>(
                                    actionParametersOf(
                                        CoffeeTypeKey to item.text,
                                        NavigateTo to CoffeeDetail.route
                                    )
                                )
                            )
                    ) {
                        Box (
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = GlanceModifier.fillMaxWidth()
                            ) {
                                Image(
                                    provider = ImageProvider(item.drawable),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = GlanceModifier.width(80.dp).height(80.dp)
                                )
                                Text(
                                    text = context.getString(item.text),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


val sampleCoffeeTypes = listOf(
    CoffeeTypeData(R.drawable.type1_americano, R.string.type1_americano),
    CoffeeTypeData(R.drawable.type2_cappucino, R.string.type2_cappuccino),
    CoffeeTypeData(R.drawable.type3_mocha, R.string.type3_mocha),
    CoffeeTypeData(R.drawable.type4_flatwhite, R.string.type4_flatwhite),
)

@Preview(showBackground = true)
@Composable
fun CoffeeTypeWidgetPreview() {
    val coffeeTypes = sampleCoffeeTypes
    CoffeeTypeWidgetContent(coffeeTypes)
}



