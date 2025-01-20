package com.kovalenko.promovatask.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kovalenko.promovatask.R
import com.kovalenko.promovatask.domain.model.Genre
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.presentation.ui.theme.PromovaTaskTheme
import java.util.Locale

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
                .height(intrinsicSize = IntrinsicSize.Min)
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
                Spacer(Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MovieVotesSection(voteAverage = movie.voteAverage, voteCount = movie.voteCount)
                    MovieLikeButton(
                        isLiked = movie.isLiked,
                        toggleLiked = { setLikeStatus(movie.id, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieLoadingItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(Modifier.padding(8.dp))
    }
}

@Composable
fun MovieErrorItem(
    modifier: Modifier = Modifier,
    retry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.loading_failed),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = retry) {
            Text(text = stringResource(R.string.retry))
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
}

@Composable
fun MovieChip(text: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.tertiary,
        shape = RoundedCornerShape(25)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun MovieVotesSection(
    voteAverage: Double,
    voteCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        Text(
            text = String.format(Locale.getDefault(), "%.1f", voteAverage),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
        Text(
            text = stringResource(
                id = R.string.movie_vote_count_brackets,
                formatArgs = arrayOf(voteCount),
            ),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun MovieLikeButton(
    isLiked: Boolean,
    toggleLiked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 1.dp) {
        IconToggleButton(
            checked = isLiked,
            onCheckedChange = toggleLiked,
            modifier = modifier
        ) {
            when (isLiked) {
                true -> Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null
                )

                false -> Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovieListItemPreview() {
    PromovaTaskTheme {
        MovieListItem(
            movie = Movie(
                adult = false,
                backdropPath = "https://image.tmdb.org/t/p/original/uKb22E0nlzr914bA9KyA5CVCOlV.jpg",
                genres = listOf(
                    Genre(1, "Movie"),
                    Genre(2, "Comedy"),
                    Genre(3, "Thriller"),
                ),
                id = 1,
                originalLanguage = "en",
                originalTitle = "Some Movie 2: Return of the Boilerplate",
                overview = "",
                popularity = 8.45,
                posterPath = "https://image.tmdb.org/t/p/original/uKb22E0nlzr914bA9KyA5CVCOlV.jpg",
                releaseDate = "2024-12-29",
                title = "Some Movie 2: Return of the Boilerplate",
                video = false,
                voteAverage = 8.45,
                voteCount = 700,
                isLiked = true
            )
        ) { _, _ -> }
    }
}