package com.abbas.cinebook.service;

import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.ShowDTO;
import com.abbas.cinebook.dto.ShowResponseDTO;
import com.abbas.cinebook.entity.Movie;
import com.abbas.cinebook.entity.Screen;
import com.abbas.cinebook.entity.Show;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.MovieRepository;
import com.abbas.cinebook.repository.ScreenRepository;
import com.abbas.cinebook.repository.ShowRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ShowService {
    
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    public ShowResponseDTO createShow(ShowDTO dto) {


        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        
        Screen screen = screenRepository.findById(dto.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not Found"));

        Show show = new Show();
        show.setShowTime(dto.getShowTime());
        show.setScreen(screen);
        show.setMovie(movie);

        Show saved = showRepository.save(show);

        ShowResponseDTO response = new ShowResponseDTO();
        response.setId(saved.getId());
        response.setShowTime(saved.getShowTime());
        response.setMovieTitle(saved.getMovie().getTitle());
        response.setScreenNumber(saved.getScreen().getScreenNumber());

        return response;
    }

}
