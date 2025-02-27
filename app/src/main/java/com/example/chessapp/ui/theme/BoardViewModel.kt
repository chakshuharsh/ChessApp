package com.example.chessapp.ui.theme


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.lifecycle.ViewModel
import com.example.chessapp.board.Board
import com.example.chessapp.board.InitialEncodedPiecesPosition
import com.example.chessapp.pieces.Piece
import com.example.chessapp.pieces.PieceType




@Composable
fun rememberBoard(
    encodedPieces: String = InitialEncodedPiecesPosition,
): Board =
    remember {
        Board(encodedPieces = encodedPieces)
    }




@Composable
fun BoardViewModel.rememberIsAvailableMove(x: Int, y: Int): Boolean =
    remember(x, y, selectedPieceMoves) {
        isAvailableMove(
            x = x,
            y = y,
        )

    }

@SuppressLint("UnrememberedMutableState")
@Composable
fun BoardViewModel.rememberPieceAt(x:Int,y:Int):Piece? =
    derivedStateOf {
        getPiece(
            x = x,
            y = y,
        )
    }.value










class BoardViewModel(private val board: Board): ViewModel() {

    val selectedPiece = mutableStateOf(board.selectedPiece)
    var isWhiteKingUnderThreat = mutableStateOf( board.isWhiteKingUnderThreat)
    var isBlackKingUnderThreat = mutableStateOf( board.isBlackKingUnderThreat)
    var showPromotionDialog = mutableStateOf(board.showPromotionDialog)
    var pawnToPromote =  board.pawnToPromote

    fun moveSelectedPiece(newX: Int, newY: Int, previousX: Int, previousY: Int) {

//        board.moveSelectedPiece(newX, newY, previousX, previousY)
    }


    fun getPiece(x: Int, y: Int):Piece? =
         board.getPiece(
           x = x,
           y = y,
       )



    var selectedPieceMoves = mutableStateOf( board.selectedPieceMoves)
    fun isAvailableMove(x:Int,y:Int):Boolean{
       return board.isAvailableMove(x,y)
    }

    fun selectPiece(piece: Piece){
     board.selectPiece(piece)
    }

    fun promotePawn(pawnToPromote:Piece,pieceType: PieceType){
        board.promotePawn(pawnToPromote, pieceType)
    }


}






