package com.example.chessapp.pieces.commonMoveHelper

import android.util.Log
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.board.BoardYCoordinates
import com.example.chessapp.pieces.Color
import com.example.chessapp.pieces.Piece



fun Piece.getDiagonalMovement(
    piece:Piece,
    pieces: List<Piece>,
):MutableSet<IntOffset> {





    val x = piece.position.x
    val y = piece.position.y



    val pieceColor: Color = piece.color

    val listOfPiecesOnSameDiagonal = pieces.filter{
            piece ->
        // Check if piece is on the same upper-left diagonal
        kotlin.math.abs(piece.position.x - x) == kotlin.math.abs(piece.position.y - y)
    }

    val closestPieces = findClosestPiecesOnDiagonalOptimized(piece,listOfPiecesOnSameDiagonal)

Log.d("piece position on same diagonal","${closestPieces["upperLeft"]}")
    Log.d("piece position on same diagonal","${closestPieces["upperRight"]}")




    val allMoves: MutableSet<IntOffset> = when(y){
        1-> getUpperDiagonalMoves(pieceColor,x,y,closestPieces)
        8-> getLowerDiagonalMoves(pieceColor,x,y,closestPieces)
        in 2..7 -> {
            val upperMoves = getUpperDiagonalMoves(pieceColor,x,y,closestPieces)
            val lowerMoves = getLowerDiagonalMoves(pieceColor,x,y,closestPieces)
            upperMoves.apply { addAll(lowerMoves) }
        }
        else ->  mutableSetOf()// Default case
    }

    return allMoves
}


fun findClosestPiecesOnDiagonalOptimized(
    selectedPiece: Piece,
    diagonalPieces: List<Piece>
): Map<String, Piece?> {
    val selectedPosition = selectedPiece.position
    val (selectedX, selectedY) = selectedPosition

    // Initialize closest pieces
    var upperLeft: Piece? = null
    var upperRight: Piece? = null
    var lowerLeft: Piece? = null
    var lowerRight: Piece? = null

    // Iterate through the list once
    for (piece in diagonalPieces) {
        val (x, y) = piece.position

        when {
            // Upper-left diagonal: x < selectedX && y > selectedY
            x < selectedX && y > selectedY -> {
                if (upperLeft == null || x > upperLeft.position.x) {
                    upperLeft = piece
                }
            }
            // Upper-right diagonal: x > selectedX && y > selectedY
            x > selectedX && y > selectedY -> {
                if (upperRight == null || x < upperRight.position.x) {
                    upperRight = piece
                }
            }
            // Lower-left diagonal: x < selectedX && y < selectedY
            x < selectedX && y < selectedY -> {
                if (lowerLeft == null || x > lowerLeft.position.x) {
                    lowerLeft = piece
                }
            }
            // Lower-right diagonal: x > selectedX && y < selectedY
            x > selectedX && y < selectedY -> {
                if (lowerRight == null || x < lowerRight.position.x) {
                    lowerRight = piece
                }
            }
        }
    }

    // Return the closest pieces for each diagonal
    return mapOf(
        "upperLeft" to upperLeft,
        "upperRight" to upperRight,
        "lowerLeft" to lowerLeft,
        "lowerRight" to lowerRight
    )
}








fun Piece.getUpperDiagonalMoves(
    color:Color,
    x: Int,
    y: Int,
    closestPieces:Map<String,Piece?>
):MutableSet<IntOffset> {
    Log.d("function called","Yes")
    Log.d("value of x is","$x")

    val upperLeftPiece = closestPieces["upperLeft"]
    val upperRightPiece = closestPieces["upperRight"]
    var upperMoves =  mutableSetOf<IntOffset>()

    Log.d("value of y2","$y")



        upperMoves =  when(x-65){
        0 -> getRightUpperDiagonalMoves(color,x,y,upperRightPiece)
       7 -> getLeftUpperDiagonalMoves(color,x,y,upperLeftPiece)
       in 1..6 ->{

           val leftMoves = getLeftUpperDiagonalMoves(color,x,y,upperLeftPiece)
           val rightMoves = getRightUpperDiagonalMoves(color,x,y,upperRightPiece)
             leftMoves.apply { addAll(rightMoves) }

       }
       else ->  mutableSetOf()// Default case
    }

    Log.d("upper moves","$upperMoves")
return upperMoves
}


fun Piece.getLowerDiagonalMoves(
    color:Color,
    x: Int,
    y: Int,
    closestPieces:Map<String,Piece?>
):MutableSet<IntOffset>{


    var lowerMoves =  mutableSetOf<IntOffset>()

    val lowerLeftPiece = closestPieces["lowerLeft"]
    val lowerRightPiece = closestPieces["lowerRight"]





    lowerMoves = when(x-65){
        0 ->  getRightLowerDiagonalMoves(color,x,y,lowerRightPiece)
        7 ->  getLeftLowerDiagonalMoves(color,x,y,lowerLeftPiece)
        in 1..6 -> {
            val leftMoves = getLeftLowerDiagonalMoves(color,x,y,lowerLeftPiece)
            val rightMoves = getRightLowerDiagonalMoves(color,x,y,lowerRightPiece)
            leftMoves.apply { addAll(rightMoves) }
        }
        else -> mutableSetOf()
    }


    return lowerMoves
}




fun Piece.getLeftUpperDiagonalMoves(
    color:Color,
    x: Int,
    y: Int,
    pieceClosestToSelectedPieceInLeftUp:Piece?
):MutableSet<IntOffset>{
    Log.d("function called 1","yes 1")
    Log.d("value of y3","$y")


    val upperLeftMoves = mutableSetOf<IntOffset>()

    var i = x
    var j = y



    if(pieceClosestToSelectedPieceInLeftUp == null){

        while(i>=66 && j<=7){
            i -= 1
            j += 1
            Log.d("value of i and j is","$i, $j")
            upperLeftMoves.add(IntOffset(i, j))
        } // 68>66 && 0<
    }else{
        if(pieceClosestToSelectedPieceInLeftUp.color == color){
            while(i>pieceClosestToSelectedPieceInLeftUp.position.x-1 && j<pieceClosestToSelectedPieceInLeftUp.position.y-1){
                i -= 1
                j += 1
                upperLeftMoves.add(IntOffset(i, j))
            }
        }else{
            while(i>pieceClosestToSelectedPieceInLeftUp.position.x && j<pieceClosestToSelectedPieceInLeftUp.position.y){
                i -= 1
                j += 1
                upperLeftMoves.add(IntOffset(i, j))
            }
        }
    }

    Log.d("upper left move","$upperLeftMoves")
 return upperLeftMoves
}



fun Piece.getRightUpperDiagonalMoves(
    color:Color,
    x: Int,
    y: Int,
    pieceClosestToSelectedPieceInRightUp:Piece?
):MutableSet<IntOffset>{



    val upperRightMoves = mutableSetOf<IntOffset>()

    var i = x
    var j = y

    if(pieceClosestToSelectedPieceInRightUp == null){

        while(i<=71 && j<=7){
            i += 1
            j += 1
            val move = IntOffset(
                x = i,
                y = j,
            )
            upperRightMoves.add(move)
        }
    }else{
        if(pieceClosestToSelectedPieceInRightUp.color == color){
            while(i<pieceClosestToSelectedPieceInRightUp.position.x-1 && j<pieceClosestToSelectedPieceInRightUp.position.y-1){
                i += 1
                j += 1
                val move = IntOffset(
                    x = i,
                    y = j,
                )
                upperRightMoves.add(move)
            }
        }else{
            while(i<pieceClosestToSelectedPieceInRightUp.position.x && j<pieceClosestToSelectedPieceInRightUp.position.y){
                i += 1
                j += 1
                val move = IntOffset(
                    x = i,
                    y = j,
                )
                upperRightMoves.add(move)
            }
        }
    }



  return upperRightMoves
}



fun Piece.getLeftLowerDiagonalMoves(
    color:Color,
    x: Int,
    y: Int,
    pieceClosestToSelectedPieceInLeftDown:Piece?,
):MutableSet<IntOffset>{




    var i = x
    var j = y

    val lowerLeftMoves = mutableSetOf<IntOffset>()

    if(pieceClosestToSelectedPieceInLeftDown == null){

        while(i>=66 && j>=2){
            i -= 1
            j -= 1
            val move = IntOffset(
                x = i,
                y = j,
            )
            lowerLeftMoves.add(move)
        }
    }else{
        if(pieceClosestToSelectedPieceInLeftDown.color == color){
            while(i>pieceClosestToSelectedPieceInLeftDown.position.x+1 && j>pieceClosestToSelectedPieceInLeftDown.position.y+1){
                i -= 1
                j -= 1
                val move = IntOffset(
                    x = i,
                    y = j,
                )
                lowerLeftMoves.add(move)
            }
        }else{
            while(i>pieceClosestToSelectedPieceInLeftDown.position.x && j>pieceClosestToSelectedPieceInLeftDown.position.y){
                i -= 1
                j -= 1
                val move = IntOffset(
                    x = i,
                    y = j,
                )
                lowerLeftMoves.add(move)
            }
        }
    }




    return lowerLeftMoves
}


fun Piece.getRightLowerDiagonalMoves(
    color:Color,
    x: Int,
    y: Int,
    pieceClosestToSelectedPieceInRightDown:Piece?
):MutableSet<IntOffset>{




    val lowerRightMoves = mutableSetOf<IntOffset>()

    var i = x
    var j = y



    if( pieceClosestToSelectedPieceInRightDown == null){

        while(i<=71 && j>=2){
            i += 1
            j -= 1
            val move = IntOffset(
                x = i,
                y = j,
            )
            lowerRightMoves.add(move)
        }
    }else{
        if(pieceClosestToSelectedPieceInRightDown.color == color){
            while(i<pieceClosestToSelectedPieceInRightDown.position.x-1 && j>pieceClosestToSelectedPieceInRightDown.position.y+1){
                i += 1
                j -= 1
                val move = IntOffset(
                    x = i,
                    y = j,
                )
                lowerRightMoves.add(move)
            }
        }else{
            while(i<pieceClosestToSelectedPieceInRightDown.position.x && j>pieceClosestToSelectedPieceInRightDown.position.y){
                i += 1
                j -= 1
                val move = IntOffset(
                    x = i,
                    y = j,
                )
                lowerRightMoves.add(move)
            }
        }
    }




 return lowerRightMoves
}










