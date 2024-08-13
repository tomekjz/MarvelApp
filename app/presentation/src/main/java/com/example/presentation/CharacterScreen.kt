package com.example.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.presentation.ui.theme.AlphaWhite
import com.example.presentation.ui.theme.CharacterBorder
import com.example.presentation.ui.theme.OffBlack
import com.example.presentation.ui.theme.Pink80
import com.example.presentation.ui.theme.YellowWarning
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterScreen(navController: NavController) {
    val viewModel: CharacterViewModel = koinViewModel()
    val uiState = remember { viewModel.uiEvent }

    Column(Modifier.fillMaxSize()) {
        val image = ImageBitmap.imageResource(R.drawable.marvel_image)
        val brush = remember(image) { ShaderBrush(ImageShader(image, TileMode.Repeated, TileMode.Repeated)) }
        Box(
            Modifier
                .fillMaxSize()
                .background(brush)
                .background(AlphaWhite)

        ) {
            when (uiState.value) {
                is Event.Loading -> {
                    LoadingScreen()
                }

                is Event.Error -> {
                    ErrorScreen((uiState.value as Event.Error).message)
                }

                else -> {
                    val characters = (uiState.value as Event.Success).data
                    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(horizontal = 4.dp, vertical = 16.dp)) {
                        characters.forEach {
                            item {
                                Column(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .border(2.dp, OffBlack, RoundedCornerShape(12.dp))
                                        .background(OffBlack, RoundedCornerShape(12.dp))
                                        .clickable {
                                            navController.navigate(
                                                CharacterDetail(
                                                    name = it.name,
                                                    description = it.description,
                                                    image = it.image
                                                )
                                            )
                                        },
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = it.image, modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(12.dp)), contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(text = it.name, modifier = Modifier.padding(6.dp), color = YellowWarning, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorScreen(message: String) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .padding(6.dp)
                .background(Pink80, RoundedCornerShape(16.dp))
        ) {
            Text(text = message, Modifier.padding(10.dp))
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        Modifier.fillMaxWidth().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Indicator(120.dp)
    }
}

@Composable
fun Indicator(
    size: Dp,
    sweepAngle: Float = 90f,
    color: Color = CharacterBorder,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth
) {
    val transition = rememberInfiniteTransition()
    val currentArcStartAngle by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        )
    )

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }
    Canvas(
        Modifier
            .progressSemantics()
            .size(size)
            .background(AlphaWhite, CircleShape)
            .padding(strokeWidth / 2)
    ) {
        drawCircle(Color.LightGray, style = stroke)

        drawArc(
            color,
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = true,
            style = stroke
        )
    }
}