package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board

class Bishop(
    override val color:Color,
    override var position: IntOffset
):Piece {

    override val type: PieceType = PieceType.B

    override val drawable: Int =
        if(color.isWhite){
            R.drawable.bishopwhite

        }
        else{
           R.drawable.bishopblack
        }


    override fun getAvailableMoves(pieces:List<Piece>): Set<IntOffset> {

        val moves = mutableSetOf<IntOffset>()



        return moves
    }

}