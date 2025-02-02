package com.example.chessapp.ui.theme


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessapp.board.Board
import com.example.chessapp.board.BoardXCoordinates
import com.example.chessapp.pieces.Piece
import com.example.chessapp.pieces.PieceType

@Composable
fun BoardCell(
    piece: Piece?,
    x:Int,
    y:Int,
    isAvailableMove: Boolean,
    onSelectPiece: (Piece) -> Unit,
    onMovePiece: () -> Unit,
    modifier:Modifier = Modifier,
    backgroundColor:Color,
    textColor:Color
){

//    Log.d("BoardCell", "Recomposed for x=$x, y=$y")
//    Log.d("isAvailable","$isAvailableMove for x = $x and y = $y")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = backgroundColor
            )
    ) {
        if (x == BoardXCoordinates.first()) {
            // draw y text
            Text(
                text = y.toString(),
                color = textColor,
                fontSize = 7.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(3.dp)
            )
        }

        if (y == 1) {
            // draw x text
            Text(
                text = x.toChar().toString(),
                color = textColor,
                fontSize = 7.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(3.dp)
            )
        }

        piece?.let {
            Image(
                painter = painterResource(it.drawable),
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onSelectPiece(it)
                    }
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }

        if (isAvailableMove)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onMovePiece()
                    }
                    .drawBehind {
                        drawCircle(
                            color = ActiveColor,
                            radius = size.width / 6,
                            center = center,
                            alpha = 0.3f

                        )
                    }
            ){

            }


    }


}