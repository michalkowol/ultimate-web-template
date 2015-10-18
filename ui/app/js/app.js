import FilterableMovieTable from './component/FilterableMovieTable.react';
import SearchActions from './action/SearchActions';
import React from 'react';
import $ from 'jQuery';

React.render(<FilterableMovieTable />, document.getElementById('demo'));
$.getJSON('/api/movies').done(movies => SearchActions.loadMovies(movies));
