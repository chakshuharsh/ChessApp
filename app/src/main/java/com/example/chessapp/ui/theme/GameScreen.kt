package com.example.chessapp.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessapp.board.InitialEncodedPiecesPosition
import com.example.chessapp.board.rememberBoard

@Composable
fun GameScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.DarkGray)
        ) {

                val initialEncodedPosition = InitialEncodedPiecesPosition
                val board = rememberBoard(initialEncodedPosition)
                val whiteMoves = board.getWhiteMovesList()
                val blackMoves = board.getBlackMovesList()


                     ListOfMoves(
                        color = com.example.chessapp.pieces.Color.B,
                        listOfMoves = blackMoves,
                     )

                    Spacer(modifier = Modifier.height(50.dp))

                    BoardUI(board, modifier = Modifier)

                     ListOfMoves(
                        color = com.example.chessapp.pieces.Color.W,
                        listOfMoves = whiteMoves
                     )


        }
    }
}

@Composable
fun ListOfMoves(color:com.example.chessapp.pieces.Color,listOfMoves:List<String>) {

    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    LaunchedEffect(listOfMoves.size) {
        if (listOfMoves.isNotEmpty()) {
            lazyListState.scrollToItem(listOfMoves.size - 1)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp, top = 10.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            state = lazyListState,


            ) {

            items(listOfMoves.size) { index ->
                val move = listOfMoves[index]
                val isMostRecent = index == listOfMoves.size - 1 // Highlight the most recent move

                Text(
                    text = move,
                    fontSize = 16.sp,
                    fontWeight = if (isMostRecent) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .background(
                            if (isMostRecent) Color.LightGray else Color.Transparent
                        )
                        .padding(4.dp)
                )

            }
        }
    }
}