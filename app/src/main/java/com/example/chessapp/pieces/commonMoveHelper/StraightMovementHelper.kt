package com.example.chessapp.pieces.commonMoveHelper


import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.pieces.Color
import com.example.chessapp.pieces.Piece



fun Piece.getStraightMoves(
    piece:Piece,
    pieces: List<Piece>,
): MutableSet<IntOffset> {

    val x = piece.position.x
    val y = piece.position.y
    val pieceColor:Color = piece.color
    val horizontalMoves = getHorizontalStraightMoves(pieceColor,x,y,pieces)
    val verticalMoves = getVerticalStraightMoves(pieceColor,x,y,pieces)

    val allMoves = mutableSetOf<IntOffset>()

    allMoves.addAll(horizontalMoves)
    allMoves.addAll(verticalMoves)




    return allMoves
}




fun getVerticalStraightMoves(
    color:Color,
    x: Int,
    y: Int,
    pieces: List<Piece>,
):MutableSet<IntOffset> {

    val verticalMoves: MutableSet<IntOffset>

    val allPieceOnSameVerticalLine = pieces.filter{it.position.x == x}

    val pieceClosestToSelectedPieceInUp = (allPieceOnSameVerticalLine.filter{ it.position.y > y }).minByOrNull { it.position.y }

    val pieceClosestToSelectedPieceInDown = (allPieceOnSameVerticalLine.filter{it.position.y < y}).maxByOrNull { it.position.y }



    verticalMoves = when (y) {
        1 -> getUpperVerticalMoves(color,x, y, pieceClosestToSelectedPieceInUp)
        8 -> getLowerVerticalMoves(color,x, y, pieceClosestToSelectedPieceInDown)
        in 2..7 -> {
            val upperMoves = getUpperVerticalMoves(color,x, y, pieceClosestToSelectedPieceInUp)
            val lowerMoves = getLowerVerticalMoves(color,x, y, pieceClosestToSelectedPieceInDown)
            upperMoves.apply { addAll(lowerMoves) }
        }
        else ->  mutableSetOf()// Default case

    }


    return verticalMoves
}


fun getHorizontalStraightMoves(
    color: Color,
    x: Int,
    y: Int,
    pieces: List<Piece>,
):MutableSet<IntOffset> {
    val horizontalMoves: MutableSet<IntOffset>


    val allPiecesOnSameHorizontalLine = pieces.filter{it.position.y == y}

    val pieceClosestToSelectedPieceInLeft:Piece? = (allPiecesOnSameHorizontalLine.filter{ it.position.x < x }).maxByOrNull { it.position.x }

    val pieceClosestToSelectedPieceInRight:Piece? = (allPiecesOnSameHorizontalLine.filter{it.position.x > x}).minByOrNull { it.position.x }

    horizontalMoves = when(x){
        65 -> getRightHorizontalMoves(color,x,y,pieceClosestToSelectedPieceInRight)
        72 -> getLeftHorizontalMoves(color,x,y,pieceClosestToSelectedPieceInLeft)
        in 66..71 ->{
            val leftMoves = getLeftHorizontalMoves(color,x, y, pieceClosestToSelectedPieceInLeft)
            val rightMoves = getRightHorizontalMoves(color,x, y, pieceClosestToSelectedPieceInRight)
            leftMoves.apply { addAll(rightMoves) }
        }
        else ->  mutableSetOf()// Default case

    }

    return horizontalMoves
}

fun getUpperVerticalMoves(color:Color,x:Int,y:Int, pieceClosestToSelectedPieceInUp:Piece? ):MutableSet<IntOffset>{
    val upperVerticalMoves = mutableSetOf<IntOffset>()

    if(pieceClosestToSelectedPieceInUp == null){
        for (i in y + 1..8) {
            val nextPosition = IntOffset(
                x = x,
                y = i
            )
            upperVerticalMoves.add(nextPosition)
        }
    }else {
        if (pieceClosestToSelectedPieceInUp.color == color) {
            for (i in y + 1..<pieceClosestToSelectedPieceInUp.position.y) {
                val nextPosition = IntOffset(
                    x = x,
                    y = i
                )
                upperVerticalMoves.add(nextPosition)
            }

        } else {
            // if piece is opposite then include the square else don't

            for (i in y + 1..pieceClosestToSelectedPieceInUp.position.y) {
                val nextPosition = IntOffset(
                    x = x,
                    y = i
                )
                upperVerticalMoves.add(nextPosition)
            }
        }
    }

    return upperVerticalMoves
}

fun getLowerVerticalMoves(color:Color,x:Int,y:Int,pieceClosestToSelectedPieceInDown:Piece?):MutableSet<IntOffset>{
    val lowerVerticalMoves = mutableSetOf<IntOffset>()
    if(pieceClosestToSelectedPieceInDown == null){
        for (i in 1..<y) {
            val nextPosition = IntOffset(
                x = x,
                y = i
            )
            lowerVerticalMoves.add(nextPosition)
        }
    }else{
        if(pieceClosestToSelectedPieceInDown.color == color){
            for(i in pieceClosestToSelectedPieceInDown.position.y+1..<y){
                val nextPosition = IntOffset(
                    x = x,
                    y = i
                )
                lowerVerticalMoves.add(nextPosition)
            }
        }else {
            for (i in pieceClosestToSelectedPieceInDown.position.y..<y) {
                val nextPosition = IntOffset(
                    x = x,
                    y = i
                )
                lowerVerticalMoves.add(nextPosition)
            }
        }
    }



    return lowerVerticalMoves
}

fun getLeftHorizontalMoves(color:Color,x:Int,y:Int,pieceClosestToSelectedPieceInLeft:Piece?):MutableSet<IntOffset>{
    val leftHorizontalMoves = mutableSetOf<IntOffset>()
    if(pieceClosestToSelectedPieceInLeft == null){
        for(i in 65..<x){
            val nextPosition = IntOffset(
                x = i,
                y = y
            )
            leftHorizontalMoves.add(nextPosition)
        }
    }else if(pieceClosestToSelectedPieceInLeft.color == color){
        for(i in pieceClosestToSelectedPieceInLeft.position.x+1..<x){
            val nextPosition = IntOffset(
                x = i,
                y = y
            )
            leftHorizontalMoves.add(nextPosition)
        }
    }
    else{
        for(i in pieceClosestToSelectedPieceInLeft.position.x..<x){
            val nextPosition = IntOffset(
                x = i,
                y = y
            )
            leftHorizontalMoves.add(nextPosition)

        }
    }

    return leftHorizontalMoves
}

fun getRightHorizontalMoves(color:Color,x:Int,y:Int,pieceClosestToSelectedPieceInRight:Piece?):MutableSet<IntOffset>{
    val rightHorizontalMoves = mutableSetOf<IntOffset>()

    if(pieceClosestToSelectedPieceInRight == null){
        for(i in x+1..72){
            val nextPosition = IntOffset(
                x = i,
                y = y
            )
            rightHorizontalMoves.add(nextPosition)
        }
    }else if(pieceClosestToSelectedPieceInRight.color == color){
        for(i in x+1..<pieceClosestToSelectedPieceInRight.position.x){
            val nextPosition = IntOffset(
                x = i,
                y = y
            )
            rightHorizontalMoves.add(nextPosition)
        }
    }
    else{
        for(i in x+1..pieceClosestToSelectedPieceInRight.position.x){
            val nextPosition = IntOffset(
                x = i,
                y = y
            )
            rightHorizontalMoves.add(nextPosition)

        }
    }

    return rightHorizontalMoves
}