CREATE TABLE Users
(
	Id INT PRIMARY KEY IDENTITY,
	Username NVARCHAR(50) NOT NULL,
	UserPassword NVARCHAR(MAX) NOT NULL,
	UserRole NVARCHAR(10) DEFAULT 'user',
)
--RELATED TABLES
CREATE TABLE Movie (
    Id INT IDENTITY PRIMARY KEY,
    Title VARCHAR(255),
    PublishedTime NVARCHAR(50),
    MovieDescription VARCHAR(MAX),
    OriginalTitle VARCHAR(255),
    MovieLength INT,
    YearMade INT,
    Genre VARCHAR(255),
    PicturePath VARCHAR(255)
)

ALTER TABLE Movie
ALTER COLUMN PublishedTime NVARCHAR(50)

CREATE TABLE Director (
    Id INT IDENTITY PRIMARY KEY,
    FullName VARCHAR(255),
    MovieId INT,
    FOREIGN KEY (MovieId) REFERENCES Movie(Id)
)

CREATE TABLE Actor (
    Id INT IDENTITY PRIMARY KEY,
    FullName VARCHAR(255),
    MovieId INT,
    FOREIGN KEY (MovieId) REFERENCES Movie(Id)
)

CREATE TABLE WholeMovie (
    MovieId INT PRIMARY KEY,
    FOREIGN KEY (MovieId) REFERENCES Movie(Id)
)

CREATE TABLE WholeMovieActor (
    WholeMovieId INT,
    ActorId INT,
    PRIMARY KEY (WholeMovieId, ActorId),
    FOREIGN KEY (WholeMovieId) REFERENCES WholeMovie(MovieId),
    FOREIGN KEY (ActorId) REFERENCES Actor(Id)
)

CREATE TABLE WholeMovieDirector (
    WholeMovieId INT,
    DirectorId INT,
    PRIMARY KEY (WholeMovieId, DirectorId),
    FOREIGN KEY (WholeMovieId) REFERENCES WholeMovie(MovieId),
    FOREIGN KEY (DirectorId) REFERENCES Director(Id)
)
--RELATED TABLES

--TESTING
SELECT * FROM Users
SELECT * FROM Movie

SELECT * FROM Actor
SELECT * FROM Director

SELECT * FROM WholeMovie
SELECT * FROM WholeMovieActor
SELECT * FROM WholeMovieDirector

INSERT INTO Actor VALUES ('ante', 15)
INSERT INTO Director VALUES ('severina', 9)

DELETE FROM WholeMovieDirector
DELETE FROM WholeMovieActor
DELETE FROM WholeMovie

GO
--TESTING



--USER PROCEDURES

CREATE PROC selectUsers
AS
	BEGIN
		SELECT * FROM Users
	END
GO

CREATE OR ALTER PROCEDURE findUser
    @Username VARCHAR(50)
AS
	BEGIN    
		SELECT
			ID AS 'ID',
			Username AS 'Username',
			UserPassword AS 'UserPassword',
			UserRole AS 'UserRole'
		FROM
			Users
		WHERE
			Username = @Username;
   	END
GO

CREATE OR ALTER PROCEDURE createUser
    @Username NVARCHAR(50),
    @UserPassword NVARCHAR(MAX),
    @ID INT OUTPUT
AS
	BEGIN
		IF EXISTS (SELECT 1 FROM Users WHERE Username = @Username)
			BEGIN
				SET @ID = -1; 
				RETURN; 
			END

		INSERT INTO Users (Username, UserPassword)
		VALUES (@Username, @UserPassword)

		SET @ID = SCOPE_IDENTITY()
		SELECT @ID;
	END
GO

CREATE OR ALTER PROCEDURE setAdmin
    @Username NVARCHAR(50)
AS
	BEGIN
		UPDATE Users
		SET UserRole = 'admin'
		WHERE Username = @Username;
	END
GO

--MOVIE PROCEDURES

CREATE OR ALTER PROCEDURE selectMovies
AS
	BEGIN
		SELECT Id, Title, OriginalTitle, PublishedTime, MovieDescription, MovieLength, YearMade, Genre, PicturePath
		FROM Movie
	END
GO

CREATE OR ALTER PROCEDURE createMovie
    @Title NVARCHAR(255),
    @PublishedTime NVARCHAR(50),
    @MovieDescription NVARCHAR(MAX),
    @OriginalTitle NVARCHAR(255),
    @MovieLength INT,
    @YearMade INT,
    @Genre NVARCHAR(255),
    @PicturePath NVARCHAR(255),
    @Id INT OUTPUT
AS
	BEGIN
		INSERT INTO Movie (Title, PublishedTime, MovieDescription, OriginalTitle, MovieLength, YearMade, Genre, PicturePath)
		VALUES (@Title, @PublishedTime, @MovieDescription, @OriginalTitle, @MovieLength, @YearMade, @Genre, @PicturePath);

		SET @Id = SCOPE_IDENTITY()
	END
GO

CREATE OR ALTER PROCEDURE updateMovie
    @Id INT,
    @Title VARCHAR(255),
    @PublishedTime NVARCHAR(50),
    @MovieDescription VARCHAR(MAX),
    @OriginalTitle VARCHAR(255),
    @MovieLength INT,
    @YearMade INT,
    @Genre VARCHAR(255),
    @PicturePath VARCHAR(255)
AS
	BEGIN
		UPDATE Movie
		SET Title = @Title,
			PublishedTime = @PublishedTime,
			MovieDescription = @MovieDescription,
			OriginalTitle = @OriginalTitle,
			MovieLength = @MovieLength,
			YearMade = @YearMade,
			Genre = @Genre,
			PicturePath = @PicturePath
		WHERE Id = @Id
	END
GO

CREATE PROCEDURE selectMovie
    @Id INT
AS
	BEGIN
		SELECT Id, Title, OriginalTitle, PublishedTime, MovieDescription, MovieLength, YearMade, Genre, PicturePath
		FROM Movie
		WHERE Id = @Id;
	END
GO

CREATE OR ALTER PROCEDURE deleteMovie
    @Id INT
AS
	BEGIN
		DELETE FROM WholeMovieActor WHERE WholeMovieId IN (SELECT MovieId FROM WholeMovie WHERE MovieId = @Id);
		DELETE FROM WholeMovieDirector WHERE WholeMovieId IN (SELECT MovieId FROM WholeMovie WHERE MovieId = @Id);
		DELETE FROM WholeMovie WHERE MovieId = @Id;
		DELETE FROM Director WHERE MovieId = @Id;
		DELETE FROM Actor WHERE MovieId = @Id;
		DELETE FROM Movie WHERE Id = @Id;
	END
GO

--ACTOR PROCEDURES

CREATE OR ALTER PROCEDURE createActor
	@FullName NVARCHAR(50),
	@MovieId INT,
	@Id INT OUTPUT
AS
	BEGIN
		IF NOT EXISTS (SELECT 1 FROM Actor WHERE FullName = @FullName AND MovieId = @MovieId)
		BEGIN
			INSERT INTO Actor(FullName, MovieId)
			VALUES (@FullName, @MovieId)
		
			SET @Id = SCOPE_IDENTITY()
		END
		ELSE
		BEGIN
			RETURN
		END
	END
GO

CREATE OR ALTER PROCEDURE assignActor
    @WholeMovieId INT,
    @ActorId INT
AS
	BEGIN
		IF NOT EXISTS (SELECT 1 FROM WholeMovie WHERE MovieId = @WholeMovieId)
			BEGIN
				INSERT INTO WholeMovie (MovieId) VALUES (@WholeMovieId);
			END

		IF NOT EXISTS (SELECT 1 FROM WholeMovieActor WHERE WholeMovieId = @WholeMovieId AND ActorId = @ActorId)
			BEGIN
				INSERT INTO WholeMovieActor (WholeMovieId, ActorId)
				VALUES (@WholeMovieId, @ActorId);
			END
		ELSE
			BEGIN
				RETURN
			END
	END
GO

--EXEC assignActor 9, 2
--GO

CREATE OR ALTER PROCEDURE selectActors
AS
	BEGIN	
		SELECT * FROM Actor
	END
GO

CREATE OR ALTER PROCEDURE updateActor
    @Id INT,
    @FullName NVARCHAR(50),
    @MovieId INT
AS
	BEGIN
		UPDATE Actor
		SET FullName = @FullName, MovieId = @MovieId
		WHERE Id = @Id
	END
GO

CREATE OR ALTER PROCEDURE deleteActor
    @Id INT
AS
	BEGIN
		DELETE FROM WholeMovieActor
		WHERE ActorId = @Id

		DELETE FROM Actor
		WHERE Id = @Id
	END
GO

CREATE OR ALTER PROCEDURE selectSpecificActors
	@WholeMovieId INT 
AS
	BEGIN
		SELECT *
		FROM WholeMovieActor wma
		INNER JOIN Actor a ON wma.ActorId = a.Id
		WHERE wma.WholeMovieId = @WholeMovieId
	END
GO

--DIRECTOR PROCEDURES

CREATE OR ALTER PROCEDURE selectDirectors
AS
	BEGIN	
		SELECT * FROM Director
	END
GO

CREATE OR ALTER PROCEDURE assignDirector
    @WholeMovieId INT,
    @DirectorId INT
AS
	BEGIN
		IF NOT EXISTS (SELECT 1 FROM WholeMovie WHERE MovieId = @WholeMovieId)
			BEGIN
				INSERT INTO WholeMovie (MovieId) VALUES (@WholeMovieId);
			END

		IF NOT EXISTS (SELECT 1 FROM WholeMovieDirector WHERE WholeMovieId = @WholeMovieId AND DirectorId = @DirectorId)
			BEGIN
				INSERT INTO WholeMovieDirector (WholeMovieId, DirectorId)
				VALUES (@WholeMovieId, @DirectorId);
			END
		ELSE
			BEGIN
				RETURN
			END
	END
GO

CREATE OR ALTER PROCEDURE selectSpecificDirectors
	@WholeMovieId INT 
AS
	BEGIN
		SELECT *
		FROM WholeMovieDirector wmd
		INNER JOIN Director a ON wmd.DirectorId = a.Id
		WHERE wmd.WholeMovieId = @WholeMovieId
	END
GO

CREATE OR ALTER PROCEDURE createDirector
	@FullName NVARCHAR(50),
	@MovieId INT,
	@Id INT OUTPUT
AS
	BEGIN
		IF NOT EXISTS (SELECT 1 FROM Director WHERE FullName = @FullName AND MovieId = @MovieId)
		BEGIN
			INSERT INTO Director (FullName, MovieId)
			VALUES (@FullName, @MovieId)
		
			SET @Id = SCOPE_IDENTITY()
		END
		ELSE
		BEGIN
			RETURN
		END
	END
GO

CREATE OR ALTER PROCEDURE deleteDirector
    @Id INT
AS
	BEGIN
		DELETE FROM WholeMovieDirector
		WHERE DirectorId = @Id

		DELETE FROM Director
		WHERE Id = @Id
	END
GO

CREATE OR ALTER PROCEDURE updateDirector
    @Id INT,
    @FullName NVARCHAR(50),
    @MovieId INT
AS
	BEGIN
		UPDATE Director
		SET FullName = @FullName, MovieId = @MovieId
		WHERE Id = @Id
	END
GO

CREATE OR ALTER PROCEDURE deleteAllData
AS
	BEGIN
		SET NOCOUNT ON;

		DELETE FROM WholeMovieActor;

		DELETE FROM WholeMovieDirector;

		DELETE FROM WholeMovie;

		DELETE FROM Actor;

		DELETE FROM Director;

		DELETE FROM Movie;

		DBCC CHECKIDENT('Actor', RESEED, 0);
		DBCC CHECKIDENT('Director', RESEED, 0);
		DBCC CHECKIDENT('Movie', RESEED, 0);

		SET NOCOUNT OFF;
	END
GO



--SET SPECIFIC USER AS ADMIN
EXEC setAdmin 'anitsha'
GO



