package cinema.controller;

import cinema.model.MovieSession;
import cinema.model.dto.request.MovieSessionRequestDto;
import cinema.model.dto.response.MovieSessionResponseDto;
import cinema.service.MovieSessionService;
import cinema.service.dto.mapping.DtoRequestMapper;
import cinema.service.dto.mapping.DtoResponseMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-sessions")
public class MovieSessionController {
    private final MovieSessionService movieSessionService;
    private final DtoRequestMapper<MovieSessionRequestDto, MovieSession>
            movieSessionDtoRequestMapper;
    private final DtoResponseMapper<MovieSessionResponseDto, MovieSession>
            movieSessionDtoResponseMapper;

    public MovieSessionController(MovieSessionService movieSessionService,
                                  DtoRequestMapper<MovieSessionRequestDto, MovieSession>
                                          dtoRequestMapper,
                                  DtoResponseMapper<MovieSessionResponseDto, MovieSession>
                                          dtoResponseMapper) {
        this.movieSessionService = movieSessionService;
        this.movieSessionDtoRequestMapper = dtoRequestMapper;
        this.movieSessionDtoResponseMapper = dtoResponseMapper;
    }

    @PostMapping
    public MovieSessionResponseDto addMovieSession(@RequestBody MovieSessionRequestDto dto) {
        MovieSession movieSession
                = movieSessionService.add(movieSessionDtoRequestMapper.fromDto(dto));
        return movieSessionDtoResponseMapper.toDto(movieSession);
    }

    @GetMapping("/available")
    public List<MovieSessionResponseDto> getAllAvailableSessions(@RequestParam Long id,
                                                          @RequestParam
                                                          @DateTimeFormat(pattern = "dd.MM.yyyy")
                                                            LocalDate date) {
        return movieSessionService.findAvailableSessions(id, date).stream()
                .map(movieSessionDtoResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody MovieSessionRequestDto movieSessionRequestDto) {
        MovieSession movieSession = movieSessionDtoRequestMapper.fromDto(movieSessionRequestDto);
        movieSession.setId(id);
        movieSessionService.update(movieSession);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieSessionService.remove(id);
    }
}
