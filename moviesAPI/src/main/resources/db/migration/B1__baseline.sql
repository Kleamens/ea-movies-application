
create table public.movie
(
    id            uuid not null
        primary key,
    title         varchar(255),
    release_year  date,
    last_modified timestamp,
    overview      text,
    meta_score    integer,
    certificate   varchar(255),
    runtime       integer,
    genre         varchar(255),
    imdb_rating   double precision,
    revenue       double precision,
    director_id   uuid
        constraint fk_movie_director
            references public.director
);

alter table public.movie
    owner to postgres;

create table public.director
(
    id        uuid not null
        primary key,
    name      varchar(255)
        unique,
    net_worth double precision
);

alter table public.director
    owner to postgres;
