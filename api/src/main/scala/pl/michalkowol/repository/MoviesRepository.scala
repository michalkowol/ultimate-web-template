package pl.michalkowol.repository

import anorm.SqlParser._
import anorm._
import com.paypal.cascade.common.option._
import pl.michalkowol.common.db._
import pl.michalkowol.db.Movie
import pl.michalkowol.db.anorm.{MPAAFilmRatings, Genres, Movies}
import play.api.db.DB

import scala.concurrent.{ExecutionContext, Future}

class MoviesRepository(implicit ec: ExecutionContext) {

  private val parser = Movies.title ~ str("mpaa_film_rate") ~ str("genre") ~ Movies.id.? map flatten map Movie.tupled

  def all: Future[Seq[Movie]] = DB.withAsyncConnection { implicit c =>
    // view used
    val sql = SQL"select id, title, genre, mpaa_film_rate from movies_flatten"
    sql.as(parser.*)
  }

  def byId(id: Long): Future[Movie] = DB.withAsyncConnection { implicit c =>
    val sql = SQL"select id, title, genre, mpaa_film_rate from movies_flatten where id = $id"
    sql.as(parser.single)
  }

  /*
    "upsert" in postgresql (since v9.5)

    insert into genres as g (name) values ('sss')
    on conflict (name) do update set id = g.id
    returning id, name;

    insert into genres as g (name) values ('sss')
    on conflict (name) do update set id = g.id
    returning id;

    insert into genres as g (name) values ('sss2')
    on conflict (name) do nothing
    returning *;

    insert into genres as g (name) values ('sss')
    on conflict (name) do update set id = g.id
    returning *;

    insert into genres as g (name) values ('test')
    on conflict (name) do update set name = g.name || excluded.name
    returning *;
  */
  private def createGenre(name: String): Future[(Long, String)] = DB.withAsyncConnection { implicit c =>
    val sql =
      SQL"""
        with s as (
          select id, name
          from genres
          where name = $name
        ), i as (
          insert into genres (name)
          select $name
          where not exists (select 1 from s)
          returning id, name
        )
        select id, name from i
        union all
        select id, name from s
      """
    sql.as(Genres.genre.single)
  }

  private def createRating(name: String): Future[(Long, String)] = DB.withAsyncConnection { implicit c =>
    val sql =
      SQL"""
        with s as (
          select id, name
          from mpaa_film_ratings
          where name = $name
        ), i as (
          insert into mpaa_film_ratings (name)
          select $name
          where not exists (select 1 from s)
          returning id, name
        )
        select id, name from i
        union all
        select id, name from s
      """
    sql.as(MPAAFilmRatings.rating.single)
  }

  /*
    it will return id, title etc. after insert:
    insert into movies (title, mpaa_film_rate_id, genre_id) values ($title, $ratingId, $genreId) returning (id, title, mpaa_film_rate_id, genre_id)
  */
  private def createMovie(title: String, genreId: Long, ratingId: Long): Future[Long] = DB.withAsyncConnection { implicit c =>
    val sql = SQL"insert into movies (title, mpaa_film_rate_id, genre_id) values ($title, $ratingId, $genreId)"
    sql.executeInsert(SqlParser.scalar[Long].single)
  }

  def create(movie: Movie): Future[Movie] = {
    val genre = createGenre(movie.genre)
    val rating = createRating(movie.mpaaFilmRate)

    for {
      genre <- genre
      rating <- rating
      insertedMovie <- createMovie(movie.title, genre._1, rating._1)
    } yield {
      movie.copy(id = insertedMovie.some)
    }
  }
}
