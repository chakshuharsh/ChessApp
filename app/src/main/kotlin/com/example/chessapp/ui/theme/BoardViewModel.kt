package com.example.chessapp.ui.theme

import androidx.lifecycle.ViewModel

enum class EndState {
    CheckMate,
    Stalemate,
    DrawByAgreement,
    DrawByRepetition,
    Resignation
}

sealed class GameState {
    data object NotStarted : GameState()
    data object Playing : GameState()
    data class Ended(val reason: EndState) : GameState()
}


class BoardViewModel:ViewModel() {








// this is a viewmodel class which will expose some function that UI need from data layer/business layer
    // this should have an instance of a data layer class
    // Should Survive configuration changes


    // List of moves
    //List of pieces
    // Selected Piece
    // Selected Piece Moves
    // IsUnderThreat for both Kings

}