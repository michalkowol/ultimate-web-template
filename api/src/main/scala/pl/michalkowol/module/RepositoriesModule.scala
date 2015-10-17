package pl.michalkowol.module

import pl.michalkowol.db.anorm.AnormPostRepository
import pl.michalkowol.repository.{PeopleRepository, WeatherRepository, PostRepository}
import scaldi.Module

class RepositoriesModule extends Module {
  bind[PeopleRepository] to injected[PeopleRepository]
  bind[WeatherRepository] to injected[WeatherRepository]
  bind[PostRepository] to injected[AnormPostRepository]
}
