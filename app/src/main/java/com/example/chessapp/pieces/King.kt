package com.example.chessapp.pieces


import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R


class King(
    override val color:Color,
    override var position: IntOffset
) :Piece{

    override val type: PieceType = PieceType.K


    override val drawable =
        if(color.isWhite){
            R.drawable.kingwhite

        }
        else{
            R.drawable.kingblack
        }


    override fun getAvailableMoves( piece:Piece,pieces:List<Piece>): Set<IntOffset> {


        val allMoves = mutableSetOf<IntOffset>()

        val x = piece.position.x
        val y = piece.position.y
        val pieceColor = piece.color


        val positionToPieceMap = pieces.associateBy { it.position }

        // Define all possible moves a king can make
        val possibleMoves = listOf(
            IntOffset(x - 1, y - 1),
            IntOffset(x, y - 1),
            IntOffset(x + 1, y - 1),
            IntOffset(x - 1, y),
            IntOffset(x + 1, y),
            IntOffset(x - 1, y + 1),
            IntOffset(x, y + 1),
            IntOffset(x + 1, y + 1)
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
    }