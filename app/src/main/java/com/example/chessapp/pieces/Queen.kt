package com.example.chessapp.pieces

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board
import com.example.chessapp.pieces.commonMoveHelper.getDiagonalMovement
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
        val straightMoves = getStraightMoves(piece, pieces)
        val diagonalMoves = getDiagonalMovement(piece,pieces)
         moves .addAll(straightMoves)
         moves.addAll(diagonalMoves)
        Log.d("diagonal moves","$diagonalMoves")
        return moves
    }

}