package com.example.xokhion

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val state by gameViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = state.showPlayButton,
                enter =
                fadeIn(animationSpec = tween(1000)) + expandVertically(animationSpec = tween(1000)),
                exit =
                fadeOut(animationSpec = tween(1000)) + shrinkVertically(animationSpec = tween(1000))
            ) {
                Button(
                    onClick = { gameViewModel.startGame() },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(16.dp)
                        .defaultMinSize(minWidth = 200.dp, minHeight = 60.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                ) {
                    Text("Play", fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            if (!state.showPlayButton) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    state
                        .board
                        .forEachIndexed { rowIndex, row ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            row.forEachIndexed { colIndex, cell ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(4.dp)
                                        .clickable {
                                            gameViewModel.makeMove(rowIndex, colIndex)
                                        }
                                        .background(Color.LightGray)
                                ) {
                                    Text(text = cell ?: "",
                                        fontSize = 36.sp,
                                        color = if (cell == "X") Color.Blue else if (cell == "O") Color.Red else MaterialTheme.colorScheme.onBackground)
                                }
                            }
                        }
                    }
                    if (state.showResult) {
                        Text(text = state.resultMessage,fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp))
                        Button(onClick = { gameViewModel.resetGame() }) {
                            Text("Again")
                        }
                    }
                }
            }
        }
    }
}