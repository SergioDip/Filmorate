package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class Film {
	private long id;
	@NotBlank @NotEmpty
	private String name;
	@Size(max = 200)
	private String description;
	@NotNull
	private LocalDate releaseDate;
	@Positive @NotNull
	private int duration;
	private Set<Long> likes;
}
