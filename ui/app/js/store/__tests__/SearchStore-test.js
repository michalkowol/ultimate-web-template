jest.dontMock('./../../dispatcher/AppDispatcher');
jest.dontMock('./../../action/SearchActions');
jest.dontMock('../SearchStore');
jest.dontMock('signals');

describe('SearchStore', function () {
  it('should filter movies by name', function () {
    // given
    const SearchActions = require('./../../action/SearchActions');
    const SearchStore = require('../SearchStore');
    const movies = [{name: 'test1', genre: 'SciFi', forChildren: false}];
    // when
    SearchActions.loadMovies(movies);
    SearchActions.changeSearchText('test');
    // then
    expect(SearchStore.filteredMovies()).toEqual([{name: 'test1', genre: 'SciFi', forChildren: false}]);
    // when
    SearchActions.changeSearchText('spec');
    // then
    expect(SearchStore.filteredMovies()).toEqual([]);
  });
});