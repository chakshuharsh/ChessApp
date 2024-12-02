package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board
import com.example.chessapp.pieces.commonMoveHelper.getDiagonalMovement
import kotlinx.coroutines.Dispatchers
import org.jetbrains.annotations.Async

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


    override fun getAvailableMoves(piece:Piece,pieces:List<Piece>): Set<IntOffset> {


           val moves =
               getDiagonalMovement(piece, pieces)


        return moves
    }

}