import React from 'react';
import ReactDOM from 'react-dom';
import _ from 'lodash';
import SearchActions from 'action/SearchActions';
import SearchStore from 'store/SearchStore';

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
    const forChildren = ReactDOM.findDOMNode(this.refs.ForChildrenInput).checked;
    SearchActions.toggleMoviesForChildren(forChildren);
  },
  searchTextChanged() {
    const searchText = ReactDOM.findDOMNode(this.refs.SearchInput).value;
    SearchActions.changeSearchText(searchText);
  },
  render() {
    return (
      <div>
        <input type="text" placeholder="Search..." value={this.props.searchText} onChange={this.searchTextChanged} ref="SearchInput"/>
        <p><label><input type="checkbox" checked={this.props.forChildren} onChange={this.forChildrenCheckboxChanged} ref="ForChildrenInput"/> Movie for children</label></p>
      </div>
    );
  }
});

const MovieTable = ({movies}) => {
  const rows = _.map(movies, movie => <MovieRow movie={movie} key={movie.id}/>);
  return (
    <table>
      <thead>
      <tr>
        <th>Name</th>
        <th>Genre</th>
      </tr>
      </thead>
      <tbody>{rows}</tbody>
    </table>
  );
};

const MovieRow = ({movie}) => {
  const title = movie.forChildren ? movie.title : <span style={{color: 'red'}}>{movie.title}</span>;
  return (
    <tr>
      <td>{title}</td>
      <td>{movie.genre}</td>
    </tr>
  );
};

export default FilterableMovieTable;