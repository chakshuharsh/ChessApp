package com.example.chessapp.board
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
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
        // this function calls the isAvailable function and saves the value
        // for a coordinate whether it is available or not for the move and also saves the value so that
        // it does not change in recomposition
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

    var selectedPieceMoves = emptySet<IntOffset>()
        private set

    var moveIncrement by mutableIntStateOf(0)
        private set


    var isWhiteKingUnderThreat = mutableStateOf(false)

    var isBlackKingUnderThreat = mutableStateOf(false)

    // why not to have a boolean variable which board cell can read and decide that if isWhiteKingUnder threat and number of threats are more than two

    var playerTurn by mutableStateOf<Color>(Color.W)

    private var threatToWhiteKing = threateningPieces(pieces,Color.B)
    private var threatToBlackKing = threateningPieces(pieces,Color.W)

    var showPromotionDialog by mutableStateOf(false)
    var pawnToPromote: Piece? by mutableStateOf(null)


    fun selectPiece(piece: Piece) {

        if (piece.color != playerTurn) {
            return
        }

        if (piece == selectedPiece) {
           // if king is under threat then we don't want to do the calculation again
            clearSelection()
            return
        }

        if(isWhiteKingUnderThreat.value)
        {

            if(threatToWhiteKing.size > 1) {

                if(piece.type == PieceType.K)
                {
                    selectedPiece = piece
                    selectedPieceMoves = piece.getAvailableMoves(piece,pieces)
                    return
                }
                else{
                    return
                }

            } else if(threatToWhiteKing.size == 1){
                // follow the displacement + attack + pinning
                selectedPiece = piece
                selectedPieceMoves = piece.getAvailableMoves(piece, pieces)
            }

           }

        else if(isBlackKingUnderThreat.value)
        {

            if(threatToBlackKing.size > 1) {

                if (piece.type == PieceType.K) {
                    selectedPiece = piece
                    selectedPieceMoves = piece.getAvailableMoves(piece, pieces)
                    return
                } else {
                    return
                }
            }  else if(threatToBlackKing.size == 1){
                // follow the displacement + attack + pinning
                selectedPiece = piece
                selectedPieceMoves = piece.getAvailableMoves(piece, pieces)
            }
        }

        else {
            selectedPiece = piece
            selectedPieceMoves = piece.getAvailableMoves(piece, pieces)
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
       // make sure that we just remove the UI state not he data
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
    // this function returns true is a cell is available for a piece to move
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

         if(piece.color == Color.W){
            threatToBlackKing =  threateningPieces(pieces,Color.W)
            threatToWhiteKing.clear()
            isWhiteKingUnderThreat.value = false
        }else{
           threatToWhiteKing = threateningPieces(pieces,Color.B)
            threatToBlackKing.clear()
            isBlackKingUnderThreat.value = false
        }

        if(threatToBlackKing.size != 0 ){
            isBlackKingUnderThreat.value = true
        }

        if(threatToWhiteKing.size != 0){
            isWhiteKingUnderThreat.value = true
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

       // if the attacking piece is king and the piece to be removed is protected by any of it's piece then we cannot move the king to that place
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


