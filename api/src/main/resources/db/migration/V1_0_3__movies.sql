create table products (
  id serial unique primary key,
  name text,
  price numeric,
  check (price > 0),
  discounted_price numeric,
  check (discounted_price > 0),
  check (price > discounted_price)
);

create table mpaa_film_ratings
(
  id serial unique primary key,
  name varchar(255) unique not null
);

create table genres
(
  id serial unique primary key,
  name varchar(255) unique not null
);

create table movies
(
  id serial unique primary key,
  title varchar(255) not null,
  mpaa_film_rate_id int not null,
  genre_id int not null,
  foreign key(mpaa_film_rate_id) references mpaa_film_ratings(id),
  foreign key(genre_id) references genres(id)
);

create view movies_flatten as
  select movies.id, movies.title, genres.name as genre, mpaa_film_ratings.name as mpaa_film_rate from movies
  join genres on movies.genre_id = genres.id
  join mpaa_film_ratings on movies.mpaa_film_rate_id = mpaa_film_ratings.id