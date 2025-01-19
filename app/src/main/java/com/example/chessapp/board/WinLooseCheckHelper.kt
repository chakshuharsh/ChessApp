package com.example.chessapp.board

import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.pieces.Color
import com.example.chessapp.pieces.Piece
import com.example.chessapp.pieces.PieceType


fun threateningPieces(
    pieces: List<Piece>,
    enemyPieceColor:Color
):List<Piece>{


    val king = pieces.find { it.color != enemyPieceColor && it.type == PieceType.K }
    val enemyPieces = pieces.filter{piece ->
    piece.color == enemyPieceColor  && piece.getAvailableMoves(piece,pieces).contains(king?.position)
}

    return enemyPieces
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


