jest.dontMock('./../../dispatcher/AppDispatcher');
jest.dontMock('./../../action/SearchActions');
jest.dontMock('../SearchStore');
jest.dontMock('signals');

describe('SearchStore', function () {
  it('should filter movies by name', function () {
    // given
    const SearchActions = require('./../../action/SearchActions');
    const SearchStore = require('../SearchStore');
    const movies = [{title: 'test1', genre: 'SciFi', forChildren: false}, {title: 'test2', genre: 'SciFi', forChildren: false}];
    // when no movies are loaded
    expect(SearchStore.filteredMovies()).toEqual([]);
    SearchActions.changeSearchText('test');
    expect(SearchStore.filteredMovies()).toEqual([]);
    SearchActions.changeSearchText('');
    // when
    SearchActions.loadMovies(movies);
    // then
    expect(SearchStore.filteredMovies()).toEqual(movies);
    // when
    SearchActions.changeSearchText('test');
    // then
    expect(SearchStore.filteredMovies()).toEqual(movies);
    // when
    SearchActions.changeSearchText('test1');
    // then
    expect(SearchStore.filteredMovies()).toEqual([{title: 'test1', genre: 'SciFi', forChildren: false}]);
    // when
    SearchActions.changeSearchText('test3');
    // then
    expect(SearchStore.filteredMovies()).toEqual([]);
  });
});