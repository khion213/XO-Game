package com.example.xokhion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState

    private var isBotTurn = false

    fun startGame() {
        _uiState.update { it.copy(showPlayButton = false, board = Array(3) { arrayOfNulls<String>(3) }) }
    }

    fun makeMove(row: Int, col: Int) {
        if (_uiState.
            value.
            board[row][col] == null && !_uiState.
            value.showResult && !isBotTurn) {
            _uiState.
            update { state ->
                val newBoard = state.board.map { it.copyOf() }.toTypedArray()
                newBoard[row][col] = "X"
                state.copy(board = newBoard)
            }
            if (checkWinner("X")) {
                _uiState.update { it.copy(showResult = true, resultMessage = "X wins!") }
            }    else if (isBoardFull()) {
                _uiState.update { it.copy(showResult = true, resultMessage = "Tie!") }
        } else {
                isBotTurn = true
                viewModelScope.launch(Dispatchers.Main) {
                    delay(1000)
                    botMove()
                }
            }
        }
    }

    private fun botMove() {
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        _uiState.value.board.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                if (cell == null) {
                    availableMoves.add(rowIndex to colIndex)
                }
            }
        }
        if (availableMoves.isNotEmpty()) {
            val (row, col) = availableMoves.random()
            _uiState.update { state ->
                val newBoard = state.board.map { it.copyOf() }.toTypedArray()
                newBoard[row][col] = "O"
                state.copy(board = newBoard)
            }
            if (checkWinner("O")) {
                _uiState.update { it.copy(showResult = true, resultMessage = "O wins!") }
            } else if (isBoardFull()) {
                _uiState.update { it.copy(showResult = true, resultMessage = "Tie!") }
            }

            isBotTurn = false
        }
    }

    private fun checkWinner(player: String): Boolean {
        val board = _uiState.value.board

        for (i in 0..2) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player)
                ||
                (board[0][i] == player && board[1][i] == player && board[2][i] == player)
            ) {
                return true
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player)
                ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player)
    }

    private fun isBoardFull(): Boolean {
        return _uiState.value.board.all { row -> row.all { cell -> cell != null } }
    }

    fun resetGame() {
        _uiState.update { GameUiState() }
        isBotTurn = false
    }
}