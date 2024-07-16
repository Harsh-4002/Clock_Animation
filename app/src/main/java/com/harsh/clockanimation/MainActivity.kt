package com.harsh.clockanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harsh.clockanimation.ui.theme.ClockAnimationTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockAnimationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    Clock()
                }
            }
        }
    }
}

@Composable
fun Clock() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition()

        val outerArcRotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing)
            )
        )

        val outerArcAlpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val outerArcWidth by infiniteTransition.animateFloat(
            initialValue = 20f,
            targetValue = 40f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val calendar = remember { Calendar.getInstance() }


        val hours = calendar.get(Calendar.HOUR)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

        val hoursRotation by animateFloatAsState(targetValue = (hours + minutes / 60f) * 30f)
        val minutesRotation by animateFloatAsState(targetValue = (minutes + seconds / 60f) * 6f)

        Canvas(modifier = Modifier.size(300.dp)) {
            val radius = size.minDimension / 2
            val center = center

            val colors = listOf(
                Color(0xFFFF00FF).copy(alpha = outerArcAlpha), // Purple
                Color(0xFFFF69B4).copy(alpha = outerArcAlpha), // Pink
                Color(0xFF00FFFF).copy(alpha = outerArcAlpha)  // Cyan
            )

            val sweepAngle = 360f / colors.size

            // Draw the rotating arcs with dynamic opacity and width
            rotate(degrees = outerArcRotation, pivot = center) {
                colors.forEachIndexed { index, color ->
                    drawArc(
                        color = color,
                        startAngle = index * sweepAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = center.copy(x = center.x - radius + 10.dp.toPx(), y = center.y - radius + 10.dp.toPx()),
                        size = size.copy(width = size.width - 20.dp.toPx(), height = size.height - 20.dp.toPx()),
                        style = Stroke(width = outerArcWidth)
                    )
                }
            }

            // Drawing the black background circle
            drawCircle(
                color = Color.Black,
                radius = radius - 8.dp.toPx()
            )

            // Drawing hour hand
            rotate(degrees = hoursRotation, pivot = center) {
                drawLine(
                    color = Color.Red,
                    start = center,
                    end = center.copy(y = center.y - radius + 80.dp.toPx()),
                    strokeWidth = 8f
                )
            }

            // Drawing minute hand
            rotate(degrees = minutesRotation, pivot = center) {
                drawLine(
                    color = Color.Cyan,
                    start = center,
                    end = center.copy(y = center.y - radius + 40.dp.toPx()),
                    strokeWidth = 6f
                )
            }

            // Drawing the center circle
            drawCircle(
                color = Color.Black,
                radius = 10.dp.toPx(),
                center = center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    ClockAnimationTheme {
        Clock()
    }
}
