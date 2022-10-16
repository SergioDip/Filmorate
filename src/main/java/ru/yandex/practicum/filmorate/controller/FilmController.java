package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@Data
public class FilmController {
	private final FilmService filmService;

	@Autowired
	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping("/films")
	public Collection<Film> findAll() {
		return filmService.findAll();
	}

	@PostMapping("/films")
	public Film createFilm(@RequestBody @Valid Film film) throws ValidationException {
		return filmService.addFilm(film);
	}

	@PutMapping("/films")
	public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
		return filmService.updateFilm(film);
	}

	@GetMapping("/films/{id}")
	public Film getFilm(@PathVariable long id) {
		return filmService.getFilmById(id);
	}

	@PutMapping("/films/{id}/like/{userId}")
	public Film addLike(@PathVariable long id, @PathVariable long userId) {
		return filmService.addLike(id, userId);
	}

	@DeleteMapping("/films/{id}/like/{userId}")
	public Film deleteLike(@PathVariable long id, @PathVariable long userId) throws ValidationException {
		return filmService.deleteLike(id, userId);
	}

	@GetMapping("/films/popular")
	public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive int countFilms) {
		return filmService.getPopularFilm(countFilms);
	}

}