import signals from 'signals';

const AppDispatcher = {
  loadMovies: new signals.Signal(),
  moviesForAll: new signals.Signal(),
  moviesForChildren: new signals.Signal(),
  changeSearchText: new signals.Signal()
};

export default AppDispatcher;