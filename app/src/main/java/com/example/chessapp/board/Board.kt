package com.example.chessapp.board
import android.annotation.SuppressLint
import android.util.Log
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

     private val whiteMovesList = mutableStateListOf<String>()
     private val blackMovesList = mutableStateListOf<String>()

    fun getWhiteMovesList(): List<String> {
        return whiteMovesList
    }

    fun getBlackMovesList(): List<String> {
        return blackMovesList
    }




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


    var isWhiteKingUnderThreat by mutableStateOf(false)

    var isBlackKingUnderThreat by mutableStateOf(false)


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
        var captureMove:Boolean = false


        if (targetPiece != null) {
            removePiece(targetPiece)
            captureMove = true
        }

        piece.position = position
        addMoves(piece, position,captureMove)

        if (piece.type == PieceType.P && piece.isEligibleForPromotion()) {
            pawnToPromote = piece
            showPromotionDialog = true
        }

        val threatsToTheKing = threateningPieces(pieces,piece.color)

        // list that returns the enemyPieces which are threat to the king

        if(threatsToTheKing.isNotEmpty() && piece.color == Color.W ){
           Log.d("White Enemy Pieces","$threatsToTheKing")
            isBlackKingUnderThreat = true
        }

        if(threatsToTheKing.isNotEmpty() && piece.color == Color.B ){
            Log.d("Black Enemy Pieces","$threatsToTheKing")
            isWhiteKingUnderThreat = true
        }



    }

// king check checkmate

    // check whether king is under check and then make the square of king red if under checked
    // try to find out pieces that can counter the check either by attacking the threatening piece or we can see if the king can attack the enemy piece or not
    // find pieces which can block the cheque by self pinning
    // if the result from above calculation is empty that means it's a checkmate
    // if we want to highlight the latest move -> that would be the last move of the

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


    private fun addMoves(piece: Piece, position: IntOffset,captureMove:Boolean){


        val coordinate = getChessCoordinatesFromPosition(position)
        Log.d("coordinate",coordinate)


       val  pieceSymbol = when(piece.type)
        {
            PieceType.K -> "K"
            PieceType.P -> ""
            PieceType.N -> "N"
            PieceType.R -> "R"
            PieceType.B -> "B"
            PieceType.Q -> "Q"
        }
        Log.d("capture", "$captureMove")

        if(captureMove){
            pieceSymbol.plus("hello")
        }
        val move = pieceSymbol.plus(coordinate)

        Log.d("move value", move)


        if(piece.color == Color.W){
            whiteMovesList.add(move)
        }
        else{
            blackMovesList.add(move)
        }

    }
    @SuppressLint("SuspiciousIndentation")
    private fun getChessCoordinatesFromPosition(position: IntOffset):String{
        var coordinates:String = "null"

        Log.d("position", "$position.x")
        Log.d("position", "$position.y")

        // so we will have a string of two characters and we need x and y coordinate
        // for position we have to calculate the alphabet and number the y in position would be number and the alphabet can find out by x for x -> 0 alphabet is a and so on
        val yCoordinate = (position.y).toString()
        val xCoordinate = when(position.x){
            65 -> "a"
            66 -> "b"
            67 -> "c"
            68 -> "d"
            69 -> "e"
            70 -> "f"
            71 -> "g"

            else -> {"h"}
        }

      coordinates =   xCoordinate.plus(yCoordinate)
        return coordinates
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


