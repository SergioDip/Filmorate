package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private long id;
	@NotNull @Pattern(regexp = "\\S*")
	private String login;
	@NotNull @Email
	private String email;
	private String name;
	@NotNull @PastOrPresent
	private LocalDate birthday;
	private Set<Long> friends = new HashSet<>();
}
