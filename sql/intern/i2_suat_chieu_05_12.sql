SELECT m.title, r.name AS room_name, s.start_time, s.available_seats
FROM showtimes s
JOIN movies m ON s.movie_id = m.id
JOIN rooms r ON s.room_id = r.id
WHERE s.start_time >= '2026-12-05 00:00:00'
  AND s.start_time < '2026-12-06 00:00:00'
  AND s.available_seats >= 20
ORDER BY s.start_time ASC, r.name ASC;
