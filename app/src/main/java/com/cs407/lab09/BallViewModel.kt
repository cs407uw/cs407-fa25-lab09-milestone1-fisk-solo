package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L

    // Expose the ball's position as a StateFlow
    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()

    /**
     * Called by the UI when the game field's size is known.
     */
    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            ball = Ball(
                backgroundWidth = fieldWidth,
                backgroundHeight = fieldHeight,
                ballSize = ballSizePx
            )

            ball?.let { b ->
                _ballPosition.value = Offset(b.posX, b.posY)
            }
        }
    }

    /**
     * Called by the SensorEventListener in the UI.
     */
    fun onSensorDataChanged(event: SensorEvent) {
        // Ensure ball is initialized
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            if (lastTimestamp != 0L) {


                val NS2S = 1.0f / 1000000000.0f
                val dT = (event.timestamp - lastTimestamp).toFloat() * NS2S

                //Update the ball's position and velocity
                val xAcc = -event.values[0] * 200
                val yAcc = event.values[1] * 200

                currentBall.updatePositionAndVelocity(
                    xAcc = xAcc,
                    yAcc = yAcc,
                    dT = dT
                )

                //Updates the StateFlow to notify the UI
                currentBall.checkBoundaries()
                _ballPosition.update { Offset(currentBall.posX, currentBall.posY) }
            }

            lastTimestamp = event.timestamp
        }
    }

    fun reset() {
        //Reset the ball's state
        ball?.reset()

        //Update the StateFlow with the reset position
        ball?.let { b ->
            _ballPosition.value = Offset(b.posX, b.posY)
        }

        //Reset the lastTimestamp
        lastTimestamp = 0L
    }
}