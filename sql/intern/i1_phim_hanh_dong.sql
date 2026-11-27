SELECT title, release_date
FROM movies
WHERE genre = 'ACTION'
ORDER BY release_date DESC, title ASC;
