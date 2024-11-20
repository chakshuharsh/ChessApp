package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board

class Knight(
    override val color:Color,
    override var position: IntOffset
) :Piece {
    override val type:PieceType = PieceType.N


    override val drawable =
        if(color.isWhite){
            R.drawable.knightwhite

        }
        else{
            R.drawable.knightblack        }


    override fun getAvailableMoves( pieces:List<Piece>): Set<IntOffset> {
        val moves = mutableSetOf<IntOffset>()


        return moves
    }
}