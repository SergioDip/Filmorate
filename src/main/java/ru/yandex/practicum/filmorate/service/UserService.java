package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
	private final UserStorage userStorage;

	@Autowired
	public UserService(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public Collection<User> findAll() {
		return userStorage.findAll();
	}

	public User createUser(User user) {
		return userStorage.createUser(user);
	}

	public User updateUser(User user) {
		return userStorage.updateUser(user);
	}

	public User getUserById(long id) {
		return userStorage.getUserById(id);
	}

	public User addFriend(long userId, long friendId) {
		User user = userStorage.getUserById(userId);
		User friend = userStorage.getUserById(friendId);
		if (user != null) {
			if (friend != null) {
				user.getFriends().add(friendId);
				friend.getFriends().add(userId);
				return user;
			} else {
				UserNotFoundException e = new UserNotFoundException(String.format("Пользователь с id: " +
						"%s не найден", friendId));
				log.debug("Ошибка валидации", e);
				throw e;
			}
		} else {
			UserNotFoundException e = new UserNotFoundException(String.format("Пользователь с id: " +
					"%s не найден", userId));
			log.debug("Ошибка валидации", e);
			throw e;
		}
	}

	public User deleteFriend(long userId, long friendId) {
		User user = userStorage.getUserById(userId);
		User friend = userStorage.getUserById(friendId);
		if (user != null) {
			if (friend != null) {
				user.getFriends().remove(friendId);
				return user;
			} else {
				UserNotFoundException e = new UserNotFoundException(String.format("Пользователь с id: " +
						"%s не найден", friendId));
				log.debug("Ошибка валидации", e);
				throw e;
			}
		} else {
			UserNotFoundException e = new UserNotFoundException(String.format("Пользователь с id: " +
					"%s не найден", userId));
			log.debug("Ошибка валидации", e);
			throw e;
		}
	}

	public List<User> getFriends(long userId) {
		List<User> userFriends = new ArrayList<>();
		User user = userStorage.getUserById(userId);
		if (user != null) {
			Set<Long> userFriendsId = user.getFriends();
			for (long id : userFriendsId) {
				userFriends.add(userStorage.getUserById(id));
			}
			return userFriends;
		} else {
			UserNotFoundException e = new UserNotFoundException(String.format("Пользователь с id: " +
					"%s не найден", userId));
			log.debug("Ошибка валидации", e);
			throw e;
		}
	}

	public List<User> getCommonFriends(long userId, long otherId) {
		User user = userStorage.getUserById(userId);
		User otherUser = userStorage.getUserById(otherId);
		Set<Long> userFriends = user.getFriends();
		Set<Long> otherUserFriends = otherUser.getFriends();
		return userStorage.findAll().stream()
						  .filter(f -> userFriends.contains(f.getId()))
						  .filter(f -> otherUserFriends.contains(f.getId()))
						  .collect(Collectors.toList());
	}

}