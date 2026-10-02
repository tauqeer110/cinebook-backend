package com.abbas.cinebook.service;

import java.util.ArrayList;
import java.util.List;

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

        ShowResponseDTO responseDTO = createResponseDTO(saved);
        
        return responseDTO;
    }

    public List<ShowResponseDTO> getShows() {

        List<Show> shows = showRepository.findAll();

        List<ShowResponseDTO> showResponseDTOs = new ArrayList<>();

        for (Show show : shows){
            ShowResponseDTO responseDTO = createResponseDTO(show);
            showResponseDTOs.add(responseDTO);
        }

        return showResponseDTOs;
    }

    public ShowResponseDTO getShow(int showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show Does Not Exists With This Id"));

        return createResponseDTO(show);
    }

    public ShowResponseDTO createResponseDTO(Show show) {

        ShowResponseDTO showResponseDTO = new ShowResponseDTO();

        showResponseDTO.setId(show.getId());
        showResponseDTO.setShowTime(show.getShowTime());
        showResponseDTO.setMovieTitle(show.getMovie().getTitle());
        showResponseDTO.setScreenNumber(show.getScreen().getScreenNumber());

        return showResponseDTO;

    }

}
