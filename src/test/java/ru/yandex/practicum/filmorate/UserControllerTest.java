package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

	private final UserController uc = new UserController();
	protected User user;

	@BeforeEach
	void createUser() {
		user = User.builder()
				   .login("login")
				   .email("mail@yandex.ru")
				   .name("Sergey")
				   .birthday(LocalDate.of(1990, 5, 5))
				   .build();
	}

	@Test
	public void createUserWithEmptyNameTest() throws ValidationException {
		user.setName("");

		assertEquals("login", uc.createUser(user).getName(), "login и name не совпадают");
		assertEquals(1, user.getId(), "id не сопадают");
	}

	@Test
	public void createUserWithWrongEmailTest() {
		user.setEmail("mailyandex.ru");
		ValidationException e = Assertions.assertThrows((ValidationException.class),
				() -> uc.createUser(user));

		assertEquals("E-mail должен содержать @", e.getMessage());

		user.setEmail("");
		e = Assertions.assertThrows((ValidationException.class), () -> uc.createUser(user));

		assertEquals("E-mail должен быть заполнен", e.getMessage());
	}

	@Test
	public void createUserWithWrongLoginTest() {
		user.setLogin("");
		ValidationException e = Assertions.assertThrows((ValidationException.class),
				() -> uc.createUser(user));

		assertEquals("Логин должен быть заполнен", e.getMessage());

		user.setLogin("New Login");
		e = Assertions.assertThrows((ValidationException.class),
				() -> uc.createUser(user));

		assertEquals("Логин не должен содержать пробелы", e.getMessage());
	}

	@Test
	public void createUserWithWrongBirthdayDateTest() {
		user.setBirthday(LocalDate.of(2024, 12, 31));
		ValidationException e = Assertions.assertThrows((ValidationException.class),
				() -> uc.createUser(user));

		assertEquals("Дата рождения не корректна", e.getMessage());
	}

	@Test
	public void updateUserTest() throws ValidationException {
		User user1 = User.builder()
						 .login("login")
						 .email("mail@yandex.ru")
						 .name("Alex")
						 .birthday(LocalDate.of(1990,5,5))
						 .id(1)
						 .build();
		uc.updateUser(user1);

		assertEquals(1, uc.findAll().size(), "Пользователь добавился, а не обновился");
		assertTrue(uc.findAll().contains(user1), "User не обновился");
		assertFalse(uc.findAll().contains(user), "User не обновился");
	}
}