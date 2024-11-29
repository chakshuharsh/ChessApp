package com.example.chessapp.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.chessapp.board.InitialEncodedPiecesPosition

import com.example.chessapp.board.rememberBoard

@Composable
fun GameScreen(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Yellow)
    )
    {
        val initialEncodedPosition = InitialEncodedPiecesPosition
        val board = rememberBoard(initialEncodedPosition)
        BoardUI(board,modifier = Modifier)
    }
}