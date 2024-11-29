package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board

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
        val moves = mutableSetOf<IntOffset>()


        return moves
    }
}