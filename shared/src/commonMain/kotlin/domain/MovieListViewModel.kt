package domain

import data.PopularMoviesDataRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import util.framework.MultiplatformViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MovieListViewModel(
    private val repository: PopularMoviesDataRepository,
    private val onMovieSelected: (movieId: Int) -> Unit
) : MultiplatformViewModel() {

    private var pageNo by mutableStateOf(1)
    private var shouldPaginate by mutableStateOf(false)
    var listState by mutableStateOf(ListState.IDLE)
    val movieLists = mutableStateListOf<MovieResult>()

    init {
        loadMore()
    }

    fun onItemClicked(movieId: Int) {
        onMovieSelected(movieId)
    }

    fun loadMore() {
        if (pageNo == 1 || (pageNo != 1 && shouldPaginate) && listState == ListState.IDLE) {
            listState = if (pageNo == 1) ListState.LOADING else ListState.PAGINATING
        }
        viewModelScope.launch {
            repository.fetchPopularMovies(pageNo)
                .catch {

                }
                .collect {
                    shouldPaginate = it.size == 20

                    if(pageNo == 1){
                        movieLists.clear()
                        movieLists.addAll(it)
                    }else{
                        movieLists.addAll(it)
                    }
                    listState = ListState.IDLE
                    if(shouldPaginate) {
                        pageNo++
                    }

                    if(pageNo == 1) {
                        listState = ListState.PAGINATION_EXHAUST
                    }
                }
        }
    }
}