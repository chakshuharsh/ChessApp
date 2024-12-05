package com.example.chessapp.pieces

fun Piece.isEligibleForPromotion(): Boolean {
    return when (color) {
        Color.W -> this is Pawn && position.y == 8  // White pawn reaches 8th rank (0-indexed 7)
        Color.B -> this is Pawn && position.y == 1  // Black pawn reaches 1st rank (0-indexed 0)
        else -> false
    }
}
