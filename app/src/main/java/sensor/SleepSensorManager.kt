package com.example.smartalarm.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class SleepSensorManager(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    var movementCallback: ((Float, String) -> Unit)? = null

    private var lastUpdate = 0L
    private val interval = 2000

    fun startListening() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {

            val currentTime = System.currentTimeMillis()

            if (currentTime - lastUpdate > interval) {

                lastUpdate = currentTime

                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                val magnitude = sqrt(x * x + y * y + z * z)

                val category = when {
                    magnitude > 2.5 -> "HIGH"
                    magnitude > 1.0 -> "MEDIUM"
                    else -> "LOW"
                }

                movementCallback?.invoke(magnitude, category)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}