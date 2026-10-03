package com.abbas.cinebook.service;

import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.MovieRequestDTO;
import com.abbas.cinebook.dto.MovieResponseDTO;
import com.abbas.cinebook.entity.Movie;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.repository.MovieRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class MovieService {
    
    private final MovieRepository movieRepository;

    public MovieResponseDTO createMovie(MovieRequestDTO movieRequestDTO) {

        boolean isMovieExists = 
                movieRepository.existsByTitleAndDuration(movieRequestDTO.getTitle(), movieRequestDTO.getDuration());

        if(isMovieExists) throw new ConflictException("Movie With The Same Title And Duration Already Exists");

        Movie movie = new Movie();

        movie.setTitle(movieRequestDTO.getTitle());
        movie.setDuration(movieRequestDTO.getDuration());

        Movie savedMovie = movieRepository.save(movie);

        MovieResponseDTO movieResponseDTO = new MovieResponseDTO();

        movieResponseDTO.setMovieId(savedMovie.getId());
        movieResponseDTO.setTitle(savedMovie.getTitle());
        movieResponseDTO.setDuration(savedMovie.getDuration());

        return movieResponseDTO;
 
    }
}
