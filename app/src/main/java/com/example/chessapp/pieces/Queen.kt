package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.board.Board

class Queen(
    override val color:Color,
    override var position: IntOffset
) :Piece {

    override val type: PieceType = PieceType.Q


    override val drawable: DrawableRes =
        if(color.isWhite){
            TODO("Res.drawable.queen_white")

        }
        else{
            TODO("Res.drawable.queen_black")
        }

    override fun getAvailableMoves( pieces:List<Piece>): Set<IntOffset> {
        val moves = mutableSetOf<IntOffset>()


        return moves
    }

}