package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
	static int id;
	Map<Integer, Film> films = new HashMap<>();

	private int generateId() {
		return ++id;
	}

	@GetMapping
	public Collection<Film> findAll() {
		log.info("Получен Get запрос");
		return films.values();
	}

	@PostMapping
	public Film createFilm(@RequestBody Film film) throws ValidationException {
		log.info("Получен Post Запрос");
		if (film.getName().isBlank() && film.getName().isEmpty()) {
			throw new ValidationException("У фильма должно быть название");
		}
		if (film.getDescription().length() > 200) {
			throw new ValidationException("Описание должно быть меньше 200 символов");
		}
		if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
			throw new ValidationException("Не корректная дата релиза");
		}
		if (film.getDuration() <= 0) {
			throw new ValidationException("Продолжительность меньше 0");
		}
		film.setId(generateId());
		films.put(film.getId(), film);
		return film;
	}

	@PutMapping
	public Film updateFilm(@RequestBody Film film) throws ValidationException {
		log.info("Получен Put запрос");
		if (film.getId() <= 0) {
			throw new ValidationException("Id должно быть больше 0");
		}
		films.put(film.getId(), film);
		return film;
	}

}



