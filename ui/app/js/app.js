import FilterableMovieTable from './component/FilterableMovieTable.react';
import SearchActions from './action/SearchActions';
import ReactDOM from 'react-dom';
import $ from 'jQuery';

ReactDOM.render(<FilterableMovieTable />, document.getElementById('demo'));
$.getJSON('/api/movies').done(movies => SearchActions.loadMovies(movies));
