package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Data
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public Collection<User> findAll() {
		return userService.findAll();
	}

	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable("id") long id) {
		return userService.getUserById(id);
	}

	@PostMapping("/users")
	public User createUser(@RequestBody @Valid User user) {
		return userService.createUser(user);
	}

	@PutMapping("/users")
	public User updateUser(@RequestBody @Valid User user) {
		return userService.updateUser(user);
	}

	@PutMapping("/users/{id}/friends/{friendId}")
	public User addFriends(@PathVariable long id, @PathVariable long friendId) {
		return userService.addFriend(id, friendId);
	}

	@DeleteMapping("/users/{id}/friends/{friendId}")
	public User deleteUser(@PathVariable long id, @PathVariable long friendId) {
		return userService.deleteFriend(id, friendId);
	}

	@GetMapping("/users/{id}/friends")
	public List<User> getAllFriends(@PathVariable long id) {
		return userService.getFriends(id);
	}

	@GetMapping("GET /users/{id}/friends/common/{otherId}")
	public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
		return userService.getCommonFriends(id, otherId);
	}

}
