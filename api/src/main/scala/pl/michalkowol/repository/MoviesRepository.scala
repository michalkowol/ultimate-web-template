package pl.michalkowol.repository

import anorm.SqlParser._
import anorm._
import com.paypal.cascade.common.option._
import pl.michalkowol.common.db._
import pl.michalkowol.db.Movie
import pl.michalkowol.db.anorm.{MPAAFilmRatings, Genres, Movies}
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

class MoviesRepository(db: Database)(implicit ec: ExecutionContext) {

  private val parser = Movies.title ~ str("mpaa_film_rate") ~ str("genre") ~ Movies.id.? map flatten map Movie.tupled

  def all: Future[Seq[Movie]] = db.withAsyncConnection { implicit c =>
    // view used
    val sql = SQL"select id, title, genre, mpaa_film_rate from movies_flatten"
    sql.as(parser.*)
  }

  def byId(id: Long): Future[Movie] = db.withAsyncConnection { implicit c =>
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
  private def createGenre(name: String): Future[(Long, String)] = db.withAsyncConnection { implicit c =>
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

  private def createRating(name: String): Future[(Long, String)] = db.withAsyncConnection { implicit c =>
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
  private def createMovie(title: String, genreId: Long, ratingId: Long): Future[Long] = db.withAsyncConnection { implicit c =>
    val sql = SQL"insert into movies (title, mpaa_film_rate_id, genre_id) values ($title, $ratingId, $genreId)"
    sql.executeInsert(SqlParser.scalar[Long].single)
  }

  /*
    From stored procedures (function) we can return:
    1) Cursor (auto-commit must be off)
    CREATE OR REPLACE FUNCTION show_cities(ref refcursor) RETURNS refcursor AS $$
    BEGIN
      OPEN ref FOR SELECT city, state FROM cities;
      RETURN ref;
    END;
    $$ LANGUAGE plpgsql;

    BEGIN;
      SELECT show_cities2('cities_cur');
      FETCH ALL IN "cities_cur";
    COMMIT;

    2.1) Set of records:
    CREATE OR REPLACE FUNCTION get_countries(country_code OUT text, country_name OUT text) RETURNS setof record AS $$
    BEGIN
      SELECT country_code, country_name FROM country_codes
    END;
    $$ LANGUAGE plpgsql;

    2.2) Set of records:
    CREATE OR REPLACE FUNCTION getcustomers() RETURNS SETOF customers AS $$
    BEGIN
      SELECT * FROM customers;
    END;
    $$ LANGUAGE plpgsql;

    3.1) Table
    CREATE OR REPLACE FUNCTION get_countries()
    RETURNS TABLE(
      country_code text,
      country_name text
    ) AS $$
    BEGIN
      SELECT country_code, country_name FROM country_codes
    END;
    $$ LANGUAGE plpgsql;

    3.2) Table
    CREATE OR REPLACE FUNCTION get_object_fields(name text) RETURNS mytable AS $$
    DECLARE f1 INT;
    DECLARE f2 INT;
    ...
    DECLARE f8 INT;
    DECLARE retval mytable;
      BEGIN
      -- fetch fields f1, f2 and f3 from table t1
      -- fetch fields f4, f5 from table t2
      -- fetch fields f6, f7 and f8 from table t3
      retval := (f1, f2, ..., f8);
      RETURN retval;
    END;
    $$ LANGUAGE plpgsql;

    4.1) Record
    CREATE FUNCTION test_ret(a TEXT, b TEXT) RETURNS RECORD AS $$
    DECLARE
      ret RECORD;
    BEGIN
      IF LENGTH(a) < LENGTH(b) THEN
        SELECT TRUE, a || b, 'a shorter than b' INTO ret;
      ELSE
        SELECT FALSE, b || a, NULL INTO ret;
      END IF;
      RETURN ret;
    END;
    $$ LANGUAGE plpgsql;

    4.2) Record
    CREATE FUNCTION test_ret(a TEXT, b TEXT) RETURNS RECORD AS $$
    DECLARE
      ret RECORD;
    BEGIN
      -- Note the CASTING being done for the 2nd and 3rd elements of the RECORD
      IF LENGTH(a) < LENGTH(b) THEN
        ret := (TRUE, (a || b)::TEXT, 'a shorter than b'::TEXT);
      ELSE
        ret := (FALSE, (b || a)::TEXT, NULL::TEXT);
      END IF;
      RETURN ret;
    END;
    $$ LANGUAGE plpgsql;

    5) Type
  */
  def createWithManyQueries(movie: Movie): Future[Movie] = {
    // it should be a transaction!
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

  def create(movie: Movie): Future[Movie] = db.withAsyncConnection { implicit c =>
    val sql = SQL"select insert_movie(${movie.title}, ${movie.genre}, ${movie.mpaaFilmRate})"
    val movieId = sql.as(SqlParser.scalar[Long].single)
    movie.copy(id = movieId.some)
  }
}
