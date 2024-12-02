package com.example.chessapp.pieces.commonMoveHelper

import android.util.Log
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.pieces.Piece

fun Piece.getKnightMoves(
    piece: Piece,
    pieces: List<Piece>,
): MutableSet<IntOffset>{

    val x = piece.position.x
    val y = piece.position.y
    val pieceColor = piece.color


    val allMoves = mutableSetOf<IntOffset>()
    val positionToPieceMap = pieces.associateBy { it.position }


    val possibleMoves = listOf(
        IntOffset(x - 1, y - 2),
        IntOffset(x + 1, y - 2),
        IntOffset(x + 1, y + 2),
        IntOffset(x - 1, y + 2),
        IntOffset(x + 2, y + 1),
        IntOffset(x + 2, y - 1),
        IntOffset(x - 2, y + 1),
        IntOffset(x - 2, y - 1)
    ).filter { move ->
        move.x in 65..72 && move.y in 0..7 // Check if the move is within the board
    }

    Log.d("all Possible Moves","$possibleMoves")


    for (move in possibleMoves) {

            val blockingPiece = positionToPieceMap[move]
            if (blockingPiece == null || blockingPiece.color != pieceColor) {
                allMoves.add(move) // Add move if not blocked or blocked by an opponent's piece
            }

    }


    Log.d("allMoves","$allMoves")


    return allMoves

}