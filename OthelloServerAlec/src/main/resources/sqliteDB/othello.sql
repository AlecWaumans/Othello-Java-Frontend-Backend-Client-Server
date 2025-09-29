PRAGMA foreign_keys = OFF;
BEGIN TRANSACTION;

DROP TABLE IF EXISTS SCORE;
DROP TABLE IF EXISTS GAME;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS SEQUENCE;
DROP TABLE IF EXISTS RESET;

CREATE TABLE SEQUENCE (
    name  TEXT PRIMARY KEY,
    value INTEGER
);

CREATE TABLE USER (
    id    INTEGER PRIMARY KEY,
    email TEXT NOT NULL UNIQUE
);

CREATE TABLE GAME (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id   INTEGER NOT NULL,
    timeStart TEXT    NOT NULL,
    FOREIGN KEY(user_id) REFERENCES USER(id)
);

CREATE TABLE SCORE (
    game_id   INTEGER,
    num       INTEGER,
    scoreJ    INTEGER,
    scoreIA   INTEGER,
    gameState TEXT,
    timeS     TEXT,
    PRIMARY KEY(game_id, num),
    FOREIGN KEY(game_id) REFERENCES GAME(id)
);
CREATE TABLE RESET (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    player INTEGER,
    timeStart TEXT    NOT NULL
);

INSERT INTO USER(id, email) VALUES (1, 'demo1@esi.be');
INSERT INTO USER(id, email) VALUES (2, 'demo2@esi.be');

INSERT INTO GAME(id, user_id, timeStart) VALUES (1, 1, '2025-01-05T10:00:00Z');
INSERT INTO GAME(id, user_id, timeStart) VALUES (2, 2, '2025-01-05T10:30:00Z');

INSERT INTO SCORE(game_id, num, scoreJ, scoreIA, gameState, timeS)
VALUES (1, 1, 2, 2, 'RUNNING', '2025-01-05T10:01:00Z');
INSERT INTO SCORE(game_id, num, scoreJ, scoreIA, gameState, timeS)
VALUES (1, 2, 4, 2, 'RUNNING', '2025-01-05T10:02:00Z');
INSERT INTO SCORE(game_id, num, scoreJ, scoreIA, gameState, timeS)
VALUES (1, 3, 6, 2, 'ENDGAME', '2025-01-05T10:15:00Z');

INSERT INTO SCORE(game_id, num, scoreJ, scoreIA, gameState, timeS)
VALUES (2, 1, 2, 4, 'RUNNING', '2025-01-05T10:31:00Z');
INSERT INTO SCORE(game_id, num, scoreJ, scoreIA, gameState, timeS)
VALUES (2, 2, 3, 6, 'ENDGAME', '2025-01-05T10:45:00Z');

INSERT INTO SEQUENCE(name, value) VALUES ('user', 0);
UPDATE SEQUENCE
SET value = COALESCE((SELECT MAX(id) FROM USER), 0)
WHERE name = 'user';

COMMIT;
PRAGMA foreign_keys = ON;
