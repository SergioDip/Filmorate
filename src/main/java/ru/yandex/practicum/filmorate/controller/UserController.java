package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

	static int id;
	private Map<Integer, User> users = new HashMap<>();

	private int generateId() {
		return ++id;
	}

	private User validationUser(User user) throws ValidationException {
		if (user.getEmail().isEmpty()) {
			throw new ValidationException("E-mail должен быть заполнен");
		}
		if (!user.getEmail().contains("@")) {
			throw new ValidationException("E-mail должен содержать @");
		}
		if (user.getLogin().isEmpty()) {
			throw new ValidationException("Логин должен быть заполнен");
		}
		if (user.getLogin().contains(" ")) {
			throw new ValidationException("Логин не должен содержать пробелы");
		}
		if (user.getBirthday().isAfter(LocalDate.now())) {
			throw new ValidationException("Дата рождения не корректна");
		}
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		return user;
	}

	@GetMapping
	public Collection<User> findAll() {
		log.info("Получен Get Запрос");
		return users.values();
	}

	@PostMapping
	public User createUser(@RequestBody User user) throws ValidationException {
		log.info("Получен Post Запрос");
		User approveUser = validationUser(user);
		user.setId(generateId());
		users.put(user.getId(), user);
		return user;
	}

	@PutMapping
	public User updateUser(@RequestBody User user) throws ValidationException {
		log.info("Получен Put Запрос");
		if (user.getId() <= 0) {
			throw new ValidationException("Id должно быть больше 0");
		}
		User approveUser = validationUser(user);
		users.put(approveUser.getId(), approveUser);
		return approveUser;
	}
}
