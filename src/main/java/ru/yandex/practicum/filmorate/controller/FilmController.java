package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private static final String DEFAULT_COUNT = "10";
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Requested all films");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("Requested film {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = DEFAULT_COUNT) int count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) String year) {
        log.info("Requested most popular {} films with genre {} released in {} year",
                count, genreId, year);
        return filmService.getTopFilms(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable int directorId, @RequestParam String sortBy) {
        log.info("Requested films by director {}, sort by {}", directorId, sortBy);
        return filmService.getTopByDirector(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam String by) {
        log.info("Requested films like {}, search in {}", query, by);
        return filmService.searchFilms(query, by);
    }

    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        log.info("Requested creation of film {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping("/{id}/mark/{userId}")
    public Film addMark(@PathVariable long id, @PathVariable long userId, @RequestParam int mark) {
        log.info("Requested addition of mark for film {} from user {} mark = {}", id, userId, mark);
        filmService.addMark(userId, id, mark);
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Requested change of film");
        return filmService.update(film);
    }

    @DeleteMapping("/{id}/mark/{userId}")
    public void deleteMark(@PathVariable long id, @PathVariable long userId) {
        log.info("Requested deletion of like for film {} from user {}", id, userId);
        filmService.deleteMark(userId, id);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        log.info("Requested common films of user {} with his friend {}", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable long id) {
        log.info("Requested deleting film with id = {}", id);
        filmService.deleteFilmById(id);
    }

}
