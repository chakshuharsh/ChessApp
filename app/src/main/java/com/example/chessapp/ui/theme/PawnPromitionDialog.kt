package com.example.chessapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.chessapp.pieces.PieceType

@Composable
fun PawnPromotionDialog(
    onPieceSelected: (PieceType) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Promote Pawn") },
        text = {
            Column {
                Button(onClick = { onPieceSelected(PieceType.Q) }) {
                    Text("Queen")
                }
                Button(onClick = { onPieceSelected(PieceType.R) }) {
                    Text("Rook")
                }
                Button(onClick = { onPieceSelected(PieceType.B) }) {
                    Text("Bishop")
                }
                Button(onClick = { onPieceSelected(PieceType.N) }) {
                    Text("Knight")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
