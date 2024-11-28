package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.board.Board
import com.example.chessapp.board.BoardXCoordinates
import com.example.chessapp.board.BoardYCoordinates

enum class PieceType { K, Q, R, B, N, P }
enum class Color {
    W, B;
    val isWhite:Boolean
        get() = this==W

    val isBlack:Boolean
        get() = this==B
}



interface Piece {
    val color: Color  // obsolete
//    val type:PieceType

    var position: IntOffset

    val type: PieceType

    val drawable: Int
    fun getAvailableMoves( piece:Piece,pieces: List<Piece>): Set<IntOffset>

    fun encode(): String {
        // W, B
        val colorCode = color.name.first()
        val type: PieceType = type

        return StringBuilder()
            .append(type)
            .append(colorCode)
            .append(position.x - BoardXCoordinates.min())
            .append(position.y - BoardYCoordinates.min())
            .toString()
    }


    companion object {  // encoded String formart =  PW01 -> type, color, x, y
        fun decode(encodedPiece: String): Piece {


            val _pieceColor = encodedPiece[1]

            var pieceColor = Color.W
                pieceColor = if (_pieceColor == 'W') {
                Color.W
            } else {
                Color.B
            }

            val position: IntOffset = IntOffset(
                x = encodedPiece[2].digitToInt() + BoardXCoordinates.min(),
                y = encodedPiece[3].digitToInt() + BoardYCoordinates.min()
            )

             return when (encodedPiece[0].toChar()) {
                'P' -> Pawn(pieceColor, position)

                'K' -> King(pieceColor, position)

                'N' -> Knight(pieceColor, position)

                'Q' -> Queen(pieceColor, position)

                'R' -> Rook(pieceColor, position)

                'B' -> Bishop(pieceColor, position)

                else ->
                    throw IllegalArgumentException("Invalid piece type!")
            }


        }
        const val EncodedPieceLength = 4
    }



}