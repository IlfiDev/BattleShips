package com.example.battleships.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class FieldScreen {

    @SuppressLint("NotConstructor")
    @Preview
    @Composable
    fun FieldScreen() {
        val numbers = (1..100).toList()

        LazyVerticalGrid(
            columns = GridCells.Fixed(10)
        ) {
            items(numbers.size) {
                Card(border = BorderStroke(1.dp, color = Color.Black)) {
                    Column() {
                        Text(text = "Number")
                        Text(text = "  $it",)
                    }
                }
            }
        }
    }
}