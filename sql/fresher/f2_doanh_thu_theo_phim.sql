SELECT m.title,
       COALESCE(SUM(b.quantity), 0) AS so_ve,
       COALESCE(SUM(b.total_amount), 0) AS doanh_thu
FROM movies m
LEFT JOIN showtimes s ON m.id = s.movie_id
LEFT JOIN bookings b ON s.id = b.showtime_id AND b.status = 'CONFIRMED'
GROUP BY m.id, m.title
ORDER BY doanh_thu DESC, m.title ASC;
