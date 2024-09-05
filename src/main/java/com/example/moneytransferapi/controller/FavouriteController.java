package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.FavouriteDTO;
import com.example.moneytransferapi.dto.ResponseFavouriteDTO;
import com.example.moneytransferapi.repositorie.FavouriteRepository;
import com.example.moneytransferapi.service.IFavouriteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Favoutire")
@RequiredArgsConstructor
public class FavouriteController {
    private final IFavouriteService favouriteService;

    @PostMapping("/AddFavourite")
    public FavouriteDTO addFavourite(@RequestBody @Valid FavouriteDTO favouriteDTO, HttpServletRequest request) {
        return favouriteService.addFavourite(favouriteDTO, request);
    }

    @DeleteMapping("/{accountId}")
    public ResponseFavouriteDTO deleteFavorite(@PathVariable @Valid FavouriteDTO favouriteDTO) {
        return favouriteService.deleteFavourite(favouriteDTO);
    }

    @GetMapping("/GetFavoutite")
    public List<FavouriteDTO> getFavorites(@RequestParam FavouriteDTO favouriteDTO) {
        return favouriteService.getFavorites(favouriteDTO);
    }
}
