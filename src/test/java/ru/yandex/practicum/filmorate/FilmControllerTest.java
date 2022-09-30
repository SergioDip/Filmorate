package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

	private final FilmController fc = new FilmController();
	protected Film film;

	@BeforeEach
	void createFilm() {
		film = Film.builder()
				   .name("The Silence of the Lambs")
				   .description("Psychological thriller about a brutal serial killer")
				   .duration(118)
				   .releaseDate(LocalDate.of(1991,1,30))
				   .build();
	}

	@Test
	public void createFilmTest() throws ValidationException {
		assertEquals(1, fc.createFilm(film).getId(), "id не совпадает");
		assertEquals(1, fc.findAll().size(), "Количество фильмов не совпадает");
	}

	@Test
	public void createFilmWithWrongNameTest() {
		film.setName("");
		ValidationException e = Assertions.assertThrows((ValidationException.class),
				() -> fc.createFilm(film));

		assertEquals("У фильма должно быть название", e.getMessage());
	}

	@Test
	public void createFilmWithTooLongDescriptionTest() {
		film.setDescription("The psychological thriller The Silence of the Lambs is about a brutal serial killer. " +
				"The plot of the film revolves around the investigation by FBI agents of several murders committed " +
				"by an unknown maniac.");
		ValidationException e = Assertions.assertThrows((ValidationException.class),
				() -> fc.createFilm(film));

		assertEquals("Описание должно быть меньше 200 символов", e.getMessage());
	}

	@Test
	public void createFilmWithWrongReleaseDateTest() {
		film.setReleaseDate(LocalDate.of(1891,1,30));
		ValidationException e = Assertions.assertThrows((ValidationException.class),
				() -> fc.createFilm(film));

		assertEquals("Не корректная дата релиза", e.getMessage());
	}

	@Test
	public void createFilmWithWrongDurationTest() {
		film.setDuration(-60);
		ValidationException e = Assertions.assertThrows((ValidationException.class),
				() -> fc.createFilm(film));

		assertEquals("Продолжительность меньше 0", e.getMessage());
	}

	@Test
	public void updateFilmTest() throws ValidationException {
		Film film1 = Film.builder()
						 .name("The Silence of the Lambs")
						 .description("Psychological thriller about a brutal serial killer")
						 .duration(60)
						 .releaseDate(LocalDate.of(1991,1,30))
						 .id(1)
						 .build();
		fc.updateFilm(film1);

		assertEquals(1, fc.findAll().size(), "Фильм добавился, а не обновилося");
		assertTrue(fc.findAll().contains(film1), "Фильм обновился не верно");
		assertFalse(fc.findAll().contains(film),"Фильм обновился не верно");
	}
}