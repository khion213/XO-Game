package com.example.xokhion

data class GameUiState(
    val board: Array<Array<String?>> = Array(3) { arrayOfNulls<String>(3) },
    val showPlayButton: Boolean = true,
    val showResult: Boolean = false,
    val resultMessage: String = ""
)