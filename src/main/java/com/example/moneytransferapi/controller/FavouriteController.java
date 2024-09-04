package com.example.moneytransferapi.controller;

import com.example.moneytransferapi.dto.FavouriteDTO;
import com.example.moneytransferapi.repositorie.FavouriteRepository;
import com.example.moneytransferapi.service.IFavouriteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Favoutire")
@RequiredArgsConstructor
public class FavouriteController {
    private final IFavouriteService favouriteService;

    @PostMapping("/AddFavourite")
    public FavouriteDTO addFavourite(@RequestBody @Valid FavouriteDTO favouriteDTO, HttpServletRequest request) {
        return favouriteService.addFavourite(favouriteDTO, request);
    }

    @DeleteMapping("/{favoriteId}")
    public void deleteFavorite(@PathVariable Long favoriteId) {
        favouriteService.deleteFavourite(favoriteId);
    }
//    @GetMapping
//    public List<FavoriteDTO> getFavorites(@RequestParam Long userId) {
//        return favoriteService.getFavorites(userId);
//    }
}
