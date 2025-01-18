package com.kovalenko.promovatask.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kovalenko.promovatask.domain.model.Movie

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieListItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    setLikeStatus: (Int, Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterPath)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .weight(1f)
                    .aspectRatio(.67f),
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(2f)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                MovieChips {
                    MovieChip(movie.originalLanguage.uppercase())
                    MovieChip(movie.releaseDate.substringBefore('-'))
                    movie.genres.forEach { MovieChip(it.name) }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieChips(
    modifier: Modifier = Modifier,
    content: @Composable (FlowRowScope.() -> Unit)
) {
    FlowRow(
        modifier = modifier,
        content = content,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    )
}

@Composable
fun MovieChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        modifier = modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(40),
                color = MaterialTheme.colorScheme.secondary
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}