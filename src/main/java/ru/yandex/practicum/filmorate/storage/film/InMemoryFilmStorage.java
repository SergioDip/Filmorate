package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

	private final Map<Long, Film> films = new HashMap<>();
	private final LocalDate checkReleaseDate = LocalDate.of(1895,12,28);
	private long id = 0;

	private long generateId() {
		return ++id;
	}

	private void releaseDateCheck(Film film) throws ValidationException {
		if (film.getReleaseDate().isBefore(checkReleaseDate)) {
			log.debug("Валидация не пройдена");
			throw new ValidationException("Не корректная дата релиза");
		}
	}

	public void save(Film film) {
		film.setId(generateId());
		films.put(film.getId(), film);
	}

	@Override
	public Collection<Film> findAll() {
		return films.values();
	}

	@Override
	public Film addFilm(Film film) throws ValidationException {
		releaseDateCheck(film);
		if (films.containsKey(film.getId())) {
			throw new ValidationException("Фильм уже добавлен");
		}
		save(film);
		log.info("Фильм: {} сохранен", film);
		return film;
	}

	@Override
	public Film updateFilm(Film film) throws ValidationException {
		releaseDateCheck(film);
		if (!films.containsKey(film.getId()) || film.getId() < 1) {
			throw new FilmNotFoundException("Фильм с id:" + film.getId() + " не найден");
		}
		films.put(film.getId(), film);
		log.info("Фильм: {} сохранен", film);
		return film;
	}

	@Override
	public Film getFilmById(long id) {
		if (films.containsKey(id)) {
			return films.get(id);
		} else {
			throw new FilmNotFoundException("Фильм с id:" + id + " не найден");
		}
	}

}