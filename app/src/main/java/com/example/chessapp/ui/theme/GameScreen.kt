package com.example.chessapp.ui.theme


import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessapp.board.InitialEncodedPiecesPosition


@SuppressLint("SuspiciousIndentation")
@Composable
fun GameScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray),

    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.DarkGray),
                    verticalArrangement = Arrangement.SpaceBetween,
        ) {

                val initialEncodedPosition = InitialEncodedPiecesPosition
                val board = rememberBoard(initialEncodedPosition)

            val whiteAdvantage = remember{board.whiteAdvantage}

            Log.d("whiteAdvantage","${whiteAdvantage.intValue}")


            val blackAdvantage = remember{board.blackAdvantage}
            Log.d("blackAdvantage","${blackAdvantage.intValue}")


            val netAdvantage = whiteAdvantage.intValue - blackAdvantage.intValue


//                val boardViewModel = BoardViewModel(board)
                val whiteMoves = remember {
                    board.getWhiteMovesList()
                }
                val blackMoves = remember {
                    board.getBlackMovesList()
                }


                     ListOfMoves(
                        color = com.example.chessapp.pieces.Color.B,
                        listOfMoves = blackMoves,
                     )

//            Spacer(modifier = Modifier.height(10.dp))

            if(netAdvantage<0){
                BlackAdvantageText(netAdvantage)
            }


                    BoardUI(board, modifier = Modifier)

            if(netAdvantage>0){
                WhiteAdvantageText(netAdvantage)
            }


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
            .padding(bottom = 50.dp, top = 50.dp, start = 10.dp, end = 10.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            state = lazyListState,


            ) {

            items(listOfMoves.size) { index ->
                val move = index.toString()+". "+listOfMoves[index]
                val isMostRecent = index == listOfMoves.size - 1 // Highlight the most recent move

                Text(
                    text = move,
                    color = if(color == com.example.chessapp.pieces.Color.W){Color.White}else{Color.Black},
                    fontSize = 16.sp,
                    fontWeight = if (isMostRecent) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .background(
                            if (isMostRecent) Color.LightGray else Color.Transparent
                        )
                        .padding(10.dp)
                )

            }
        }
    }
}

@Composable
fun WhiteAdvantageText(netAdvantage:Int){
    Text(
        text = "+$netAdvantage",
        fontSize = 15.sp,
        color = Color.White,
        modifier = Modifier.padding(8.dp)
    )
}


@Composable
fun BlackAdvantageText(netAdvantage: Int){
  val  netAdvantageText = -1*netAdvantage
    Text(
        text = "+$netAdvantageText",
        fontSize = 15.sp,
        color = Color.Black,
        modifier = Modifier.padding(8.dp)
    )
}

// for having a single list of moves and just changing the color we can merge these two lists

//val whiteMoves = remember {
//    board.getWhiteMovesList()
//}
//val blackMoves = remember {
//    board.getBlackMovesList()
//}