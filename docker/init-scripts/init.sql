-- Create the table to store puzzles
CREATE TABLE puzzles (
    puzzle_id VARCHAR(10) PRIMARY KEY,
    fen TEXT NOT NULL,
    moves TEXT NOT NULL,
    rating INTEGER,
    rating_deviation INTEGER,
    popularity INTEGER,
    nb_plays INTEGER,
    themes TEXT,
    game_url TEXT,
    opening_tags TEXT
);

