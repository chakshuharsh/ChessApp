package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board

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
            R.drawable.queenblack        }

    override fun getAvailableMoves( pieces:List<Piece>): Set<IntOffset> {
        val moves = mutableSetOf<IntOffset>()


        return moves
    }

}