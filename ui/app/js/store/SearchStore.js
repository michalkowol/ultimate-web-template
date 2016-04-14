import AppDispatcher from 'dispatcher/AppDispatcher';
import SearchActions from 'action/SearchActions';
import signals from 'signals';
import _ from 'lodash';

const SearchStore = (function () {

  var movies = [];
  var searchText = '';
  var forChildren = false;
  const changed = new signals.Signal();

  AppDispatcher.changeSearchText.add(function (newSearchText) {
    searchText = newSearchText;
    changed.dispatch();
  });

  AppDispatcher.moviesForAll.add(function () {
    forChildren = false;
    changed.dispatch();
  });

  AppDispatcher.moviesForChildren.add(function () {
    forChildren = true;
    changed.dispatch();
  });

  AppDispatcher.loadMovies.add(function (newMovies) {
    movies = newMovies;
    changed.dispatch();
  });

  function isForChildren() {
    return forChildren;
  }

  function getSearchText() {
    return searchText;
  }

  function addChangeListener(fn) {
    changed.add(fn);
  }

  function removeChangeListener(fn) {
    changed.remove(fn);
  }

  function filteredMovies() {
    const filteredMovies = _.filter(movies, movie => {
      return (movie.forChildren || forChildren === movie.forChildren) && movie.title.indexOf(searchText) !== -1;
    });
    return filteredMovies;
  }

  return {
    filteredMovies: filteredMovies,
    isForChildren: isForChildren,
    getSearchText: getSearchText,
    addChangeListener: addChangeListener,
    removeChangeListener: removeChangeListener
  };
})();

export default SearchStore;