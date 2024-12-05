package com.example.chessapp.board
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import com.example.chessapp.pieces.Bishop
import com.example.chessapp.pieces.Color
import com.example.chessapp.pieces.Knight
import com.example.chessapp.pieces.Pawn
import com.example.chessapp.pieces.Piece
import com.example.chessapp.pieces.PieceType
import com.example.chessapp.pieces.Queen
import com.example.chessapp.pieces.Rook
import com.example.chessapp.pieces.isEligibleForPromotion


@Composable
fun rememberBoard(
    encodedPieces: String = InitialEncodedPiecesPosition,
): Board =
        remember {
           Board(encodedPieces = encodedPieces)
        }


@SuppressLint("UnrememberedMutableState")
@Composable
fun Board.rememberPieceAt(x: Int, y: Int): Piece? =
    derivedStateOf {
        getPiece(
            x = x,
            y = y,
        )
    }.value

@Composable
fun Board.rememberIsAvailableMove(x: Int, y: Int): Boolean =
    remember(x, y, selectedPieceMoves) {
        isAvailableMove(
            x = x,
            y = y,
        )
    }

@Immutable
class Board(
    encodedPieces: String = InitialEncodedPiecesPosition,
) {
    private val _pieces = mutableStateListOf<Piece>()
    val pieces get() = _pieces.toList()

    init {
        _pieces.addAll(
            decodePieces(encodedPieces = encodedPieces)
        )
    }

    var selectedPiece by mutableStateOf<Piece?>(null)
        private set

    var selectedPieceMoves by mutableStateOf(emptySet<IntOffset>())
        private set

    var moveIncrement by mutableIntStateOf(0)
        private set

    var playerTurn by mutableStateOf<Color>(Color.W)

    var showPromotionDialog by mutableStateOf(false)
    var pawnToPromote: Piece? by mutableStateOf(null)

    /**
     * User events
     */

    fun selectPiece(piece: Piece) {
        if (piece.color != playerTurn)
            return

        if (piece == selectedPiece) {
            clearSelection()
        } else {
            selectedPiece = piece
            selectedPieceMoves = piece.getAvailableMoves(piece,pieces)
            }
    }





    fun moveSelectedPiece(x: Int, y: Int) {
        selectedPiece?.let { piece ->
            if (!isAvailableMove(x = x, y = y))
                return

            if (piece.color != playerTurn)
                return

            movePiece(
                piece = piece,
                position = IntOffset(x, y)
            )

            moveIncrement++


            clearSelection()



            switchPlayerTurn()


        }
    }


    private fun clearSelection() {
        selectedPiece = null
        selectedPieceMoves = emptySet()
    }
    /**
     * Public Methods
     */

    fun getPiece(x: Int, y: Int): Piece? =
        _pieces.find { it.position.x == x && it.position.y == y }

    fun isAvailableMove(x: Int, y: Int): Boolean {
        return selectedPieceMoves.any { it.x == x && it.y == y }
    }


    fun save() {
        val encodedBoard = encode()
 
    }

    /**
     * Private Methods
     */

    private fun movePiece(
        piece: Piece,
        position: IntOffset
    ) {
        val targetPiece = pieces.find { it.position == position }


        if (targetPiece != null)
            removePiece(targetPiece)

        piece.position = position

        if (piece.type == PieceType.P && piece.isEligibleForPromotion()) {
            pawnToPromote = piece
            showPromotionDialog = true
        }

    }



     fun promotePawn(pawnToPromote:Piece,pieceType: PieceType){


        val selectedPieceType:PieceType = pieceType

        val promotedPiece = when (selectedPieceType) {
            PieceType.Q -> Queen(pawnToPromote.color, pawnToPromote.position)
            PieceType.R -> Rook(pawnToPromote.color, pawnToPromote.position)
            PieceType.B -> Bishop(pawnToPromote.color, pawnToPromote.position)
            PieceType.N -> Knight(pawnToPromote.color, pawnToPromote.position)

            else -> {
                TODO()
            }
        }

        removePiece(pawnToPromote)
        _pieces.add(promotedPiece)
         showPromotionDialog =false

    }

    private fun removePiece(piece: Piece) {
        _pieces.remove(piece)
    }



    private fun switchPlayerTurn() {
        playerTurn = if (playerTurn == Color.W) Color.B else Color.W

    }

    private fun encode(): String {
        return pieces.joinToString(separator = "") { it.encode() }
    }

    companion object {
        const val BoardKeyPrefix = "board_"
    }
}


