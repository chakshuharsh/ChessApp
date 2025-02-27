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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chessapp.board.Board
import com.example.chessapp.board.BoardXCoordinates
import com.example.chessapp.board.BoardYCoordinates
import com.example.chessapp.board.rememberIsAvailableMove
import com.example.chessapp.board.rememberPieceAt
import com.example.chessapp.pieces.PieceType


@Composable
fun BoardUI(
    board: Board,
    modifier: Modifier = Modifier
) {

    val showPromotionDialog = board.showPromotionDialog
    val pawnToPromote = board.pawnToPromote
    val isBlackKingUnderThreat: MutableState<Boolean> = board.isBlackKingUnderThreat
    val isWhiteKingUnderThreat: MutableState<Boolean> = board.isWhiteKingUnderThreat
    val selectedPiece = board.selectedPiece


    Column(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .border(
                width = 8.dp,
                color = Color.White
            )
            .padding(8.dp),

    ) {

        BoardYCoordinates
            .forEach { y -> // 1-8
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    BoardXCoordinates
                        .forEach { x -> // A-H
                            val piece = board.rememberPieceAt(x, y)
                            val isAvailableMove = remember{mutableStateOf(false)}

                             isAvailableMove.value =
                                board.rememberIsAvailableMove(x, y)
                             val backgroundColor =
                                when {
                                    piece != null && piece == selectedPiece -> ActiveColor
                                    (x + y) % 2 == 0 -> DarkColor

                                    piece?.type == PieceType.K && piece.color == com.example.chessapp.pieces.Color.W && isWhiteKingUnderThreat.value -> Red
                                    piece?.type == PieceType.K && piece.color == com.example.chessapp.pieces.Color.B && isBlackKingUnderThreat.value -> Red


                                    (x + y) % 2 != 0 -> LightColor

                                    else -> LightColor
                                }

                            val textColor =
                                when {
                                    piece != null && piece == selectedPiece ->
                                        Color.White

                                    (x + y) % 2 == 0 ->
                                        LightColor

                                    else ->
                                        DarkColor
                                }
                                BoardCell(
                                    piece,
                                    x,
                                    y,
                                    isAvailableMove.value,
                                    onSelectPiece = { board.selectPiece(it) },
                                    onMovePiece ={ newX, newY  ->
                                        board.moveSelectedPiece(newX, newY /*, prevX, prevY*/)
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                     backgroundColor,
                                    textColor
                                )

                        }


                }

            }

        if (showPromotionDialog && pawnToPromote != null) {
            PawnPromotionDialog(
                onPieceSelected = { selectedType ->
                    board.promotePawn(pawnToPromote,selectedType)

                },
                onDismissRequest = {  }
            )
        }



    }

}