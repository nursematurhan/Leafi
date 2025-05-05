package com.nursematurhan.leafi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageWithBackButton(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        TopAppBar(
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Geri"
                    )
                }
            }
        )
        content()
    }
}
