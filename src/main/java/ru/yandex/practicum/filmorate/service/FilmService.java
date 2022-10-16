package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	@Autowired
	public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
		this.filmStorage = filmStorage;
		this.userStorage = userStorage;
	}

	public Collection<Film> findAll() {
		return filmStorage.findAll();
	}

	public Film addFilm(Film film) throws ValidationException {
		return filmStorage.addFilm(film);
	}

	public Film updateFilm(Film film) throws ValidationException {
		return filmStorage.updateFilm(film);
	}

	public Film getFilmById(long id) {
		return filmStorage.getFilmById(id);
	}

	public Film addLike(long FilmId, long userId) {
		Film film = filmStorage.getFilmById(FilmId);
		if (film != null) {
			User user = userStorage.getUserById(userId);
			if (user != null) {
				film.getLikes().add(userId);
				return film;
			} else {
				throw new UserNotFoundException(String.format("пользователь с id %s не найден", userId));
			}
		} else {
			FilmNotFoundException e = new FilmNotFoundException(String.format("Фильм с id = %s не найден", FilmId));
			log.debug("Ошибка валидации", e);
			throw e;
		}
	}

	public Film deleteLike(long FilmId, long userId) throws ValidationException {
		Film film = filmStorage.getFilmById(FilmId);
		if (film != null) {
			User user = userStorage.getUserById(userId);
			if (user != null) {
				if (film.getLikes().contains(userId)) {
					film.getLikes().remove(userId);
					return film;
				} else {
					throw new ValidationException(String.format("пользователь с id %s не ставил лайк", userId));
				}
			} else {
				throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userId));
			}
		} else {
			throw new FilmNotFoundException(String.format("Фильм с id = %s не найден", FilmId));
		}
	}

	public List<Film> getPopularFilm(int count) {
		Collection<Film> films = filmStorage.findAll();
		return films.stream()
					.sorted(customComparator)
					.limit(count)
					.collect(Collectors.toList());
	}

	Comparator<Film> customComparator = (f1, f2) -> {
		int comp1 = 0;
		int comp2 = 0;
		if (f1.getLikes() != null) {
			comp1 = f1.getLikes().size();
		}
		if (f2.getLikes() != null) {
			comp2 = f2.getLikes().size();
		}
		return comp2 - comp1;
	};

}
