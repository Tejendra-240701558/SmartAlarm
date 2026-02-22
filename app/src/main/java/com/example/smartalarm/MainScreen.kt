package com.example.smartalarm

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun MainScreen(
    magnitude: Float,
    category: String
) {

    var isAlarmOn by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("Not Set") }
    var reportText by remember { mutableStateOf("No Data Yet") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Smart Alarm System",
            style = MaterialTheme.typography.headlineMedium
        )

        // ON / OFF Switch
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Alarm Status: ")
            Switch(
                checked = isAlarmOn,
                onCheckedChange = { isAlarmOn = it }
            )
        }

        // Selected Time Display
        Text("Selected Time: $selectedTime")

        // Time Picker Button
        Button(onClick = {
            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    selectedTime = String.format("%02d:%02d", hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }) {
            Text("Set Alarm Time")
        }

        // Confirm Alarm Button
        Button(onClick = {
            if (isAlarmOn && selectedTime != "Not Set") {
                reportText = "Alarm set for $selectedTime"
            } else {
                reportText = "Please turn ON alarm and set time"
            }
        }) {
            Text("Confirm Alarm")
        }

        Divider()

        // 🔥 Live Sensor Data Section
        Text(
            text = "Live Movement Data",
            style = MaterialTheme.typography.titleMedium
        )

        Text("Magnitude: %.2f".format(magnitude))
        Text("Category: $category")

        Divider()

        // Report Section
        Text(
            text = "Report",
            style = MaterialTheme.typography.titleMedium
        )

        Text(reportText)
    }
}