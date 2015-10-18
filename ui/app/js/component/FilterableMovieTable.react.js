import React from 'react';
import _ from 'lodash';
import SearchActions from './../action/SearchActions';
import SearchStore from './../store/SearchStore';

const FilterableMovieTable = React.createClass({
  onChange() {
    this.setState({
      movies: SearchStore.filteredMovies(),
      searchText: SearchStore.getSearchText(),
      forChildren: SearchStore.isForChildren()
    });
  },
  componentDidMount() {
    SearchStore.addChangeListener(this.onChange);
  },
  componentWillUnmount() {
    SearchStore.removeChangeListener(this.onChange);
  },
  getInitialState() {
    return {
      searchText: '',
      forChildren: false,
      movies: []
    };
  },
  render() {
    return (
        <div>
          <SearchBar
              searchText={this.state.searchText}
              forChildren={this.state.forChildren}
              />
          <MovieTable
              movies={this.state.movies}
              />
        </div>
    );
  }
});

const SearchBar = React.createClass({
  forChildrenCheckboxChanged() {
    const forChildren = React.findDOMNode(this.refs.ForChildrenInput).checked;
    SearchActions.toggleMoviesForChildren(forChildren);
  },
  searchTextChanged() {
    const searchText = React.findDOMNode(this.refs.SearchInput).value;
    SearchActions.changeSearchText(searchText);
  },
  render() {
    return (
        <div>
          <input type="text" placeholder="Search..." value={this.props.searchText} onChange={this.searchTextChanged} ref="SearchInput"/>

          <p><input type="checkbox" checked={this.props.forChildren} onChange={this.forChildrenCheckboxChanged} ref="ForChildrenInput"/> Movie for children</p>
        </div>
    );
  }
});

const MovieTable = React.createClass({
  render() {
    const rows = _.map(this.props.movies, movie => {
      return <MovieRow movie={movie}/>
    });
    return (
        <table>
          <thead>
          <th>Name</th>
          <th>Genre</th>
          </thead>
          <tbody>{rows}</tbody>
        </table>
    );
  }
});

const MovieRow = React.createClass({
  render() {
    const name = this.props.movie.forChildren ? this.props.movie.name : <span style={{color: 'red'}}>{this.props.movie.name}</span>;
    return (
        <tr>
          <td>{name}</td>
          <td>{this.props.movie.genre}</td>
        </tr>
    );
  }
});

export default FilterableMovieTable;