package com.example.chessapp.board

import android.util.Log
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.pieces.Color
import com.example.chessapp.pieces.Piece
import com.example.chessapp.pieces.PieceType


fun threateningPieces(
    pieces: List<Piece>,
    enemyPieceColor:Color // onw which has moves suppose black has moves and there is a threat to white's king
): MutableList<Piece> {

    val king = pieces.find { it.color != enemyPieceColor && it.type == PieceType.K } // finding king under threat -> White
    val enemyPieces =  pieces.filter{ piece ->
    piece.color == enemyPieceColor  && piece.getAvailableMoves(piece,pieces).contains(king?.position)
}
    // this function here is too heavy I guess  coz it find all the threatening pieces and then call getAvailableMoves for all these pieces ok so may be 4-5 calls max
        Log.d("enemy pieces","$enemyPieces")

    return enemyPieces.toMutableList()
}



fun threateningPiecesMoves (
   pieces: List<Piece>,
   kingColor:Color,
   x: Int,
   y: Int,
):Set<IntOffset>{

    val kingPosition = IntOffset(
        x = x,
        y = y
    )

    val enemyPieces = pieces.filter{piece ->
        piece.color != kingColor && piece.getAvailableMoves(piece,pieces).contains(kingPosition)
    }

    val allPossibleEnemyMoves: Set<IntOffset> = enemyPieces.flatMap { piece ->
        piece.getAvailableMoves(piece, pieces)
    }.toSet()

 return allPossibleEnemyMoves
}


