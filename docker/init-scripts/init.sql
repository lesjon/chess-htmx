SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE TYPE public.user_role AS ENUM (
    'USER',
    'ADMIN'
);

ALTER TYPE public.user_role OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE public.books (
    id bigint NOT NULL,
    name character varying NOT NULL
);

ALTER TABLE public.books OWNER TO postgres;

CREATE SEQUENCE public.books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.books_id_seq OWNER TO postgres;

ALTER SEQUENCE public.books_id_seq OWNED BY public.books.id;

CREATE TABLE public.puzzle_attempts (
    id bigint NOT NULL,
    collection_id bigint NOT NULL,
    puzzle_id character varying NOT NULL
);

ALTER TABLE public.puzzle_attempts OWNER TO postgres;

CREATE SEQUENCE public.puzzle_attempts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.puzzle_attempts_id_seq OWNER TO postgres;

ALTER SEQUENCE public.puzzle_attempts_id_seq OWNED BY public.puzzle_attempts.id;

CREATE TABLE public.puzzle_collections (
    id bigint NOT NULL,
    user_id bigint
);

ALTER TABLE public.puzzle_collections OWNER TO postgres;

CREATE SEQUENCE public.puzzle_collections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.puzzle_collections_id_seq OWNER TO postgres;

ALTER SEQUENCE public.puzzle_collections_id_seq OWNED BY public.puzzle_collections.id;

CREATE TABLE public.puzzle_metadatas (
    id bigint NOT NULL,
    puzzle_id character varying NOT NULL,
    active boolean DEFAULT true NOT NULL,
    book_id bigint,
    additional_moves character varying,
    likes text[],
    dislikes text[]
);

ALTER TABLE public.puzzle_metadatas OWNER TO postgres;

CREATE SEQUENCE public.puzzle_metadatas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.puzzle_metadatas_id_seq OWNER TO postgres;

ALTER SEQUENCE public.puzzle_metadatas_id_seq OWNED BY public.puzzle_metadatas.id;

CREATE TABLE public.puzzles (
    puzzle_id character varying(10) NOT NULL,
    fen text NOT NULL,
    moves text NOT NULL,
    rating integer,
    rating_deviation integer,
    popularity integer,
    nb_plays integer,
    themes text,
    game_url text,
    opening_tags text
);

ALTER TABLE public.puzzles OWNER TO postgres;

CREATE TABLE public.users (
    id bigint NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    rating integer NOT NULL,
    roles integer[] NOT NULL
);

ALTER TABLE public.users OWNER TO postgres;

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.user_id_seq OWNER TO postgres;

ALTER SEQUENCE public.user_id_seq OWNED BY public.users.id;

ALTER TABLE ONLY public.books ALTER COLUMN id SET DEFAULT nextval('public.books_id_seq'::regclass);

ALTER TABLE ONLY public.puzzle_attempts ALTER COLUMN id SET DEFAULT nextval('public.puzzle_attempts_id_seq'::regclass);

ALTER TABLE ONLY public.puzzle_collections ALTER COLUMN id SET DEFAULT nextval('public.puzzle_collections_id_seq'::regclass);

ALTER TABLE ONLY public.puzzle_metadatas ALTER COLUMN id SET DEFAULT nextval('public.puzzle_metadatas_id_seq'::regclass);

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.puzzle_attempts
    ADD CONSTRAINT puzzle_attempts_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.puzzle_collections
    ADD CONSTRAINT puzzle_collections_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.puzzle_metadatas
    ADD CONSTRAINT puzzle_metadatas_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.puzzles
    ADD CONSTRAINT puzzles_pkey PRIMARY KEY (puzzle_id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

CREATE INDEX puzzles_rating_idx ON public.puzzles USING btree (rating) WITH (deduplicate_items='true');

ALTER TABLE ONLY public.puzzle_attempts
    ADD CONSTRAINT attempt_collection_fk FOREIGN KEY (collection_id) REFERENCES public.puzzle_collections(id);

ALTER TABLE ONLY public.puzzle_attempts
    ADD CONSTRAINT attempts_puzzle_fk FOREIGN KEY (puzzle_id) REFERENCES public.puzzles(puzzle_id);

ALTER TABLE ONLY public.puzzle_collections
    ADD CONSTRAINT chess_collection_user_fk FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.puzzle_metadatas
    ADD CONSTRAINT metadata_puzzle_fk FOREIGN KEY (puzzle_id) REFERENCES public.puzzles(puzzle_id);

ALTER TABLE ONLY public.puzzle_metadatas
    ADD CONSTRAINT puzzle_metadatas_book_fkey FOREIGN KEY (book_id) REFERENCES public.books(id) NOT VALID;

