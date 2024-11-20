package com.example.chessapp.ui.theme


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import com.example.chessapp.R
import com.example.chessapp.board.Board
import com.example.chessapp.pieces.Piece

@Composable
fun BoardCell(
    board: Board,
    piece: Piece?,
    x:Int,
    y:Int,
    modifier:Modifier = Modifier
){

    val backgroundColor =
        when {
            piece != null && piece == board.selectedPiece ->

                Color(0xFFFFF9E8)

            (x + y) % 2 == 0 ->
                Color(0xFFB7C0D8)

            else ->
                Color(0xFFE8EDF9)
        }


    Box(
     modifier = Modifier
         .fillMaxSize()
         .background(color = backgroundColor)
         // ALIGNMENT
         .clickable(
             interactionSource = remember { MutableInteractionSource() },
             indication = null
         )
             {
                 if(piece !=null ){
                     board.selectPiece(piece)
                 } else if(board.isAvailableMove(x,y)) {
                     // draw circle or points as well on available moves
                     board.moveSelectedPiece(x, y) // Try to move a selected piece
                 }else{
                     TODO("when it's just a normal square")
                 }

             },
        contentAlignment = Alignment.Center
    ){

        piece?.let {
            Image(
                painter = painterResource(id = piece.drawable), // Replace with the correct image resource
                contentDescription = null,
                modifier = Modifier.fillMaxSize() // Adjust size as needed
            )
        }


    }


}