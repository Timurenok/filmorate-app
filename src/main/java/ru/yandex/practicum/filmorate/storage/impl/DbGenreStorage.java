package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(
                "select * from genres",
                this::mapper);
    }

    @Override
    public Genre getById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select * from genres where id = ?",
                    this::mapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new WrongIdException("No such genre in DB with id = " + id + " was found.");
        }
    }

    @Override
    public List<Genre> getByFilmId(long filmId) {
        return jdbcTemplate.query(
                "select * from genres where id in (select genre_id from film_genre where film_id = ?)",
                this::mapper,
                filmId);
    }

    private Genre mapper(ResultSet resultSet, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(resultSet.getInt("genres.id"));
            genre.setName(resultSet.getString("genres.name"));
            return genre;
    }
}
