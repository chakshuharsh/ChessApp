package com.example.chessapp.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chessapp.board.Board
import com.example.chessapp.board.BoardXCoordinates
import com.example.chessapp.board.rememberIsAvailableMove
import com.example.chessapp.board.rememberPieceAt


@Composable

fun BoardUI(
    board: Board,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .border(
                width = 8.dp,
                color = Color.White
            )
            .padding(8.dp)
    ) {

        BoardXCoordinates
            .forEach { y-> // 1-8
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    BoardXCoordinates
                        .forEach { x-> // A-H
                            val piece = board.rememberPieceAt(x, y)

//                            val isAvailableMove = board.rememberIsAvailableMove(x , y  )

                            BoardCell(board,
                                piece,
                                x,
                                y,  modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight())
                        }


                }

        }


    }

}