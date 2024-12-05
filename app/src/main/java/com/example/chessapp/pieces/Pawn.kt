package com.example.chessapp.pieces

import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.R

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

        if(piece.color == Color.W){
           val targetPosition1 = IntOffset(
               x = position.x+1,
               y = position.y+1,
           )

            val targetPosition2 = IntOffset(
                x = position.x-1,
                y = position.y+1,
            )

            val targetPiece1 = pieces.find{(it.position == targetPosition1)}
            val targetPiece2 = pieces.find{(it.position == targetPosition2)}
            if(targetPiece2 !=null && targetPiece2.color != piece.color){
            moves.add(targetPosition2)
            }
            if(targetPiece1 !=null && targetPiece1.color != piece.color){
                moves.add(targetPosition1)
            }
        }

        if(piece.color == Color.B){
            val targetPosition1 = IntOffset(
                x = position.x+1,
                y = position.y-1,
            )

            val targetPosition2 = IntOffset(
                x = position.x-1,
                y = position.y-1,
            )

            val leftTargetPiece = pieces.find{(it.position == targetPosition1)}
            val rightTargetPiece = pieces.find{(it.position == targetPosition2)}
            if(rightTargetPiece !=null && rightTargetPiece.color != piece.color){
                moves.add(targetPosition2)
            }
            if(leftTargetPiece !=null && leftTargetPiece.color != piece.color){
                moves.add(targetPosition1)
            }
        }

        return moves
    }





}