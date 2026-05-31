package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun QrCodeComponent(
    text: String,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    qrColor: Color = Color.Black,
    backgroundColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(8.dp)) // ASCL Gold border
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.size(size - 24.dp)) {
            val gridSize = 21 // 21x21 standard QR grid
            val cellSizeWidth = this.size.width / gridSize
            val cellSizeHeight = this.size.height / gridSize

            // Seed a random generator with the text's hash so the QR code is deterministic per ID
            val seed = text.hashCode().toLong()
            val random = Random(seed)

            // Create a matrix initialized to false
            val matrix = Array(gridSize) { BooleanArray(gridSize) }

            // Fill matrix with deterministic pseudo-random values
            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    matrix[r][c] = random.nextBoolean()
                }
            }

            // Function to superimpose a Finder Pattern
            fun drawFinderPattern(startRow: Int, startCol: Int) {
                for (r in 0 until 7) {
                    for (c in 0 until 7) {
                        val absoluteR = startRow + r
                        val absoluteC = startCol + c
                        if (absoluteR in 0 until gridSize && absoluteC in 0 until gridSize) {
                            // Draw outer black ring (7x7) or inner black core (3x3), otherwise white
                            val isOuterBorder = r == 0 || r == 6 || c == 0 || c == 6
                            val isInnerCore = r in 2..4 && c in 2..4
                            matrix[absoluteR][absoluteC] = isOuterBorder || isInnerCore
                        }
                    }
                }
            }

            // Draw three finder patterns in standard QR code positions
            drawFinderPattern(0, 0) // Top Left
            drawFinderPattern(0, gridSize - 7) // Top Right
            drawFinderPattern(gridSize - 7, 0) // Bottom Left

            // Draw a protective white border around the finder patterns
            fun protectFinderPattern(startRow: Int, startCol: Int) {
                // Clear the row bordering the finder patterns (row of size 8)
                val borderSize = 8
                for (r in -1..borderSize) {
                    for (c in -1..borderSize) {
                        val absoluteR = startRow + r
                        val absoluteC = startCol + c
                        if (absoluteR in 0 until gridSize && absoluteC in 0 until gridSize) {
                            // Skip the core finder cells themselves
                            val isInsideFinder = r in 0..6 && c in 0..6
                            if (!isInsideFinder) {
                                matrix[absoluteR][absoluteC] = false
                            }
                        }
                    }
                }
            }
            protectFinderPattern(0, 0)
            protectFinderPattern(0, gridSize - 7)
            protectFinderPattern(gridSize - 7, 0)

            // Now draw the grid physically onto the canvas
            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    if (matrix[r][c]) {
                        drawRect(
                            color = qrColor,
                            topLeft = Offset(c * cellSizeWidth, r * cellSizeHeight),
                            size = Size(cellSizeWidth + 0.5f, cellSizeHeight + 0.5f) // add slight overlap helper
                        )
                    }
                }
            }
        }
    }
}
