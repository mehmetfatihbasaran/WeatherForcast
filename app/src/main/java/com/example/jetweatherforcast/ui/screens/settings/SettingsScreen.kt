package com.example.jetweatherforcast.ui.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetweatherforcast.model.Unit
import com.example.jetweatherforcast.widgets.WeatherAppBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    var unitToggleState by remember { mutableStateOf(false) }
    val measurementUnits = listOf("Imperial (F) ", "Metric (C) ")
    val choiceFromDB = settingsViewModel.unitList.collectAsState().value
    val defaultChoice = if (choiceFromDB.isEmpty()) measurementUnits[0] else choiceFromDB[0].unit
    var choiceState by remember { mutableStateOf(defaultChoice) }

    Scaffold(
        topBar = {
            WeatherAppBar(
                title = "Settings",
                icon = Icons.Default.ArrowBack,
                isMainScreen = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Units of Measurement",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                IconToggleButton(
                    checked = !unitToggleState,
                    onCheckedChange = {
                        unitToggleState = !it
                        choiceState = if (unitToggleState) {
                            measurementUnits[0]
                        } else {
                            measurementUnits[1]
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(shape = RectangleShape)
                        .padding(5.dp)
                        .background(color = Color.Magenta.copy(alpha = 0.4f))
                ) {
                    Text(text = if (unitToggleState) measurementUnits[0] else measurementUnits[1])
                }
                Button(
                    onClick = {
                        settingsViewModel.deleteAllUnits()
                        settingsViewModel.insertUnit(unit = Unit(unit = choiceState))
                    },
                    modifier = Modifier.padding(3.dp),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEFBE42)
                    )
                ) {
                    Text(
                        text = "Save",
                        modifier = Modifier.padding(4.dp),
                        color = Color.White,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}