package com.example.chessapp.pieces

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R
import com.example.chessapp.board.Board

class Pawn(
    override val color:Color,
    override var position: IntOffset


):Piece {

    override val type: PieceType = PieceType.P


    override val drawable =
        if(color.isWhite){
            R.drawable.pawnwhite

        }
        else{
            R.drawable.pawnblack
        }

    override fun getAvailableMoves(piece:Piece,pieces:List<Piece>): Set<IntOffset> {

        val moves = mutableSetOf<IntOffset>()
        val direction = if(color.isWhite) 1 else -1
        val isFirstMove:Boolean = position.y==2 && color.isWhite||
                position.y==7&& color.isBlack

        val nextPosition1= IntOffset(
            x   =  position.x,
            y   =   position.y + direction
        )

        val nextPositionForFirstMove= IntOffset(
            x   =  position.x,
            y   =   position.y + 2*(direction)
        )





        val nextPiece = pieces.find{it.position == nextPosition1}

        val nextPieceForFirstMove = pieces.find{it.position == nextPositionForFirstMove}

        if(nextPiece == null){
            moves.add(nextPosition1)
        }

        if(isFirstMove && (nextPieceForFirstMove == null) && (nextPiece == null)){
            moves.add(nextPositionForFirstMove)
        }

        return moves
    }


}