package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.board.Board

class Knight(
    override val color:Color,
    override var position: IntOffset
) :Piece {
    override val type:PieceType = PieceType.N


    override val drawable: DrawableRes =
        if(color.isWhite){
            TODO("Res.drawable.knight_white")

        }
        else{
            TODO("Res.drawable.knight_black")
        }


    override fun getAvailableMoves( pieces:List<Piece>): Set<IntOffset> {
        val moves = mutableSetOf<IntOffset>()


        return moves
    }
}