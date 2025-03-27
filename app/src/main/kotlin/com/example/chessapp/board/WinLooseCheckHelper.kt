package com.example.chessapp.board

import android.util.Log
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.pieces.Color
import com.example.chessapp.pieces.Piece
import com.example.chessapp.pieces.PieceType

// this function will be a suspend function with and withContext(IO)
fun threateningPieces(
    pieces: List<Piece>,
    enemyPieceColor:Color // onw which has moves suppose black has moves and there is a threat to white's king
): MutableList<Piece> {

    val king = pieces.find { it.color != enemyPieceColor && it.type == PieceType.K } // finding king under threat -> White
    val enemyPieces =  pieces.filter{ piece ->
    piece.color == enemyPieceColor  && piece.getAvailableMoves(piece,pieces).contains(king?.position)
}
    // this function here is too heavy I guess  coz it find all the threatening pieces and then call getAvailableMoves for all these pieces ok so may be 4-5 calls max
    Log.d("enemy pieces", "$enemyPieces")

    return enemyPieces.toMutableList()
}


fun safeSquaresForKing(pieces:List<Piece>,king:Piece,kingMoves:Set<IntOffset>):Set<IntOffset>{

    // How we can make it main safe -> by using coroutine

    // this function will iterate through the list "pieces" and search for the possible next move of enemy
    // the color should not be equal to King.color and
    // for each opposite piece call the getAvailable move
    // if the coordinates are there in kingMoves already remove them and return the kingMoves
    // Set difference

    // stop if safeMoves becomes empty
    var safeMoves = kingMoves.toMutableSet()

    for (piece in pieces) {
        if (piece.color != king.color) { // Only consider enemy pieces
            val enemyMoves = piece.getAvailableMoves(piece, pieces)
            safeMoves.removeAll(enemyMoves) // Remove squares under attack
        }
    }

    return safeMoves


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


