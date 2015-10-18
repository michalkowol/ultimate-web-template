import AppDispatcher from './../dispatcher/AppDispatcher';

const SearchActions = {
  changeSearchText(newText) {
    AppDispatcher.changeSearchText.dispatch(newText);
  },
  toggleMoviesForChildren(forChildren) {
    forChildren ? AppDispatcher.moviesForChildren.dispatch() : AppDispatcher.moviesForAll.dispatch();
  },
  loadMovies(movies) {
    AppDispatcher.loadMovies.dispatch(movies);
  }
};

export default SearchActions;