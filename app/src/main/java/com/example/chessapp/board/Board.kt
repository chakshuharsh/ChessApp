package com.example.chessapp.board
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
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

@Stable
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

    private var playerTurn by mutableStateOf<Color>(Color.W)


    var whiteAdvantage: MutableIntState = mutableIntStateOf(0)
    private set
    var blackAdvantage: MutableIntState = mutableIntStateOf(0)
    private set



    private var threatToWhiteKing = threateningPieces(pieces,Color.B)
    private var threatToBlackKing = threateningPieces(pieces,Color.W)

    var showPromotionDialog by mutableStateOf(false)
    var pawnToPromote: Piece? by mutableStateOf(null)


    fun selectPiece(piece: Piece) { // we need to pass the current position

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
                    return // a single piece cannot safe the king from double threat
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
    fun moveSelectedPiece(newX: Int, newY: Int) { // need to pass the current position as well to save it
        Log.d("Reached 4 ","YES")
        selectedPiece?.let { piece ->
            if (!isAvailableMove(x = newX, y = newY))
                return

            if (piece.color != playerTurn)
                return // cannot capture own pieces

            // when we know the current and final position of the piece for a move we will save it in some useful data structure


            movePiece(
                piece = piece,
                newPosition = IntOffset(newX, newY),
//                oldPosition = IntOffset(previousX,previousY)
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
        newPosition: IntOffset,
//        oldPosition:IntOffset
    ) {
        Log.d("Reached 5 ","YES")
        val targetPiece = pieces.find { it.position == newPosition }
        var captureMove:Boolean = false


        if (targetPiece != null) {
            captureMove = true
            // increment the advantage on piece.color
            // basis of piece color we have to assign value to the advantage
            if(targetPiece.color == Color.W){
                blackAdvantage.intValue  += when(targetPiece.type){
                    PieceType.P-> targetPiece.value
                    PieceType.Q -> targetPiece.value
                    PieceType.R -> targetPiece.value
                    PieceType.B, PieceType.N -> targetPiece.value

                    else->{
                        0
                    }
                }
            }

            if(targetPiece.color == Color.B){
                whiteAdvantage.intValue  += when(targetPiece.type){
                    PieceType.P-> targetPiece.value
                    PieceType.Q -> targetPiece.value
                    PieceType.R -> targetPiece.value
                    PieceType.B, PieceType.N -> targetPiece.value
                    else->{
                        0
                    }
                }
            }


            removePiece(targetPiece)
        }

        piece.position = newPosition
        addMoves(piece, newPosition,captureMove /*,oldPosition*/)

        if (piece.type == PieceType.P && piece.isEligibleForPromotion()) {
            pawnToPromote = piece
            showPromotionDialog = true
        }

         if(piece.color == Color.W){
            threatToBlackKing =  threateningPieces(pieces,Color.W)
            threatToWhiteKing.clear()
            isWhiteKingUnderThreat.value = false
        }
         else{
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

// this function will update the advantage on basis of promotedPiece
         // if pp is white white adv++ else black
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

         if(promotedPiece.color == Color.W){
             whiteAdvantage.intValue += when (promotedPiece.type) {
                 PieceType.Q -> promotedPiece.value
                 PieceType.R -> promotedPiece.value
                 PieceType.B, PieceType.N -> promotedPiece.value
                 else -> {
                     0
                 }
             }
         }else if(promotedPiece.color == Color.B){
             blackAdvantage.intValue  += when(promotedPiece.type){
                 PieceType.Q -> 9
                 PieceType.R -> 5
                 PieceType.B, PieceType.N -> 3
                 else->{0}
             }
         }

        removePiece(pawnToPromote)
        _pieces.add(promotedPiece)
         showPromotionDialog = false

    }

    private fun removePiece(piece: Piece) {

       // if the attacking piece is king and the piece to be removed is protected by any of it's piece then we cannot move the king to that place
        _pieces.remove(piece)
    }


    private fun addMoves(piece: Piece, newPosition: IntOffset,captureMove:Boolean/*,oldPosition: IntOffset*/){

//        When a pawn makes a capture, the file from which the pawn departed is used to identify the pawn. For example, exd5 (pawn on the e-file captures the piece on d5).
         // for pawn we need to pass the previous position as well
        val coordinate = getChessCoordinatesFromPosition(newPosition)



       var  pieceSymbol = when(piece.type)
        {
            PieceType.K -> "K"
            PieceType.P -> ""
            PieceType.N -> "N"
            PieceType.R -> "R"
            PieceType.B -> "B"
            PieceType.Q -> "Q"
        }

        if(captureMove){
            pieceSymbol =  pieceSymbol.plus("*")
        }


        val move = pieceSymbol.plus(coordinate)




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


