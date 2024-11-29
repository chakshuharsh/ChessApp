package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board
import com.example.chessapp.pieces.commonMoveHelper.getStraightMoves

class Queen(
    override val color:Color,
    override var position: IntOffset
) :Piece {

    override val type: PieceType = PieceType.Q


    override val drawable =
        if(color.isWhite){
            R.drawable.queenwhite

        }
        else{
            R.drawable.queenblack
        }

    override fun getAvailableMoves( piece:Piece,pieces:List<Piece>): MutableSet<IntOffset> {
        val moves = mutableSetOf<IntOffset>()

        moves .addAll(getStraightMoves(piece, pieces))

        return moves
    }

}