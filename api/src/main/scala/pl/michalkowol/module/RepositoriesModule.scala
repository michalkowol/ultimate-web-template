package pl.michalkowol.module

import pl.michalkowol.repository.{MoviesRepository, PeopleRepository, PostsRepository, WeatherRepository}
import scaldi.Module

class RepositoriesModule extends Module {
  bind[PeopleRepository] to injected[PeopleRepository]
  bind[WeatherRepository] to injected[WeatherRepository]
  bind[PostsRepository] to injected[PostsRepository]
  bind[MoviesRepository] to injected[MoviesRepository]
}
