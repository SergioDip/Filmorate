package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

	private final Map<Long, User> users = new HashMap<>();
	private long id = 0;

	private long generateId() {
		return ++id;
	}

	public void save(User user) {
		user.setId(generateId());
		users.put(user.getId(), user);
	}

	@Override
	public Collection<User> findAll() {
		return users.values();
	}

	@Override
	public User createUser(User user) {
		if (user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		save(user);
		log.info("Создан пользователь: {}", user);
		return user;
	}

	@Override
	public User updateUser(User user) {
		if (users.containsKey(user.getId())) {
			users.put(user.getId(), user);
		} else {
			throw new UserNotFoundException("Пользователь не найден");
		}
		log.info("Пользователь: {} обновлен", user);
		return users.get(user.getId());
	}

	@Override
	public User getUserById(long id) {
		if (users.containsKey(id)) {
			return users.get(id);
		} else {
			throw new UserNotFoundException("Пользователь с id" + id + " не найден");
		}
	}
}