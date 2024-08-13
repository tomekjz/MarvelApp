package com.example.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.presentation.ui.theme.DarkGrey
import com.example.presentation.ui.theme.OffBlack
import com.example.presentation.ui.theme.YellowWarning

@Composable
fun CharacterDetailScreen(
    character: CharacterDetail
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(OffBlack)
    ) {
        AsyncImage(model = character.image, contentDescription = "", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.Crop)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(text = character.name, fontSize = 24.sp, fontFamily = FontFamily.Monospace, color = YellowWarning)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .background(DarkGrey)
                    .height(2.dp)
            ) {}
            val description = character.description.ifEmpty {
                stringResource(R.string.no_description_found)
            }
            Text(text = description, fontSize = 20.sp, color = YellowWarning)
        }
    }
}