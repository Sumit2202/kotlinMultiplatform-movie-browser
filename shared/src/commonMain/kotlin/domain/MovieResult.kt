package domain

data class MovieResult(
    val imageUrl: String,
    val id: Int,
    val title: String,
    val overview: Double,
    val releaseDate: String,
    val voteAverage: Double,
    val hasMorePages: Boolean
)