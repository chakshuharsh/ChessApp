package com.example.chessapp.pieces.commonMoveHelper

import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.pieces.Piece

fun Piece.getKingMoves(
    piece:Piece,
    pieces: List<Piece>,
): MutableSet<IntOffset>{

    val allMoves = mutableSetOf<IntOffset>()

    val x = piece.position.x
    val y = piece.position.y
    val pieceColor = piece.color


    val positionToPieceMap = pieces.associateBy { it.position }

    // Define all possible moves a king can make
    val possibleMoves = listOf(
        IntOffset(x - 1, y - 1), // Top-left
        IntOffset(x, y - 1),     // Top
        IntOffset(x + 1, y - 1), // Top-right
        IntOffset(x - 1, y),     // Left
        IntOffset(x + 1, y),     // Right
        IntOffset(x - 1, y + 1), // Bottom-left
        IntOffset(x, y + 1),     // Bottom
        IntOffset(x + 1, y + 1)  // Bottom-right
    ).filter { move ->
        move.x in 65..72 && move.y in 1..8 // Filter moves within board boundaries
    }

    // Process moves
    for (move in possibleMoves) {
            val blockingPiece = positionToPieceMap[move]
            if (blockingPiece == null || blockingPiece.color != pieceColor) {
                allMoves.add(move) // Add move if not blocked or blocked by an opponent's piece
            }

    }


    return allMoves
}