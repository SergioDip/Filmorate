package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

	Collection<Film> findAll();

	Film addFilm(Film film) throws ValidationException;

	Film updateFilm(Film film) throws ValidationException;

	Film getFilmById(long id);

}
