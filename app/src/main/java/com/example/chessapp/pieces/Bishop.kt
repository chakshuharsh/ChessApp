package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.board.Board

class Bishop(
    override val color:Color,
    override var position: IntOffset
):Piece {

    override val type: PieceType = PieceType.B

    override val drawable: DrawableRes =
        if(color.isWhite){
            TODO("Res.drawable.bishop_white")

        }
        else{
            TODO("Res.drawable.bishop_black")
        }


    override fun getAvailableMoves(pieces:List<Piece>): Set<IntOffset> {

        val moves = mutableSetOf<IntOffset>()



        return moves
    }

}