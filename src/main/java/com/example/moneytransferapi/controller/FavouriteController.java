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
@RequestMapping("/api/v1/user/Favourite")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavouriteController {
    private final IFavouriteService favouriteService;

    @PostMapping("/AddFavourite")
    public FavouriteDTO addFavourite(@RequestBody @Valid FavouriteDTO favouriteDTO, HttpServletRequest request) {
        return favouriteService.addFavourite(favouriteDTO, request);
    }

    @DeleteMapping("/{accountId}")
    public ResponseFavouriteDTO deleteFavorite(@PathVariable @Valid Long accountId, HttpServletRequest request) {
        return favouriteService.deleteFavourite(accountId ,request);
    }

    @GetMapping("/GetFavourite/{userId}")
    public List<FavouriteDTO> getFavorites(@PathVariable int userId, HttpServletRequest request) {
        return favouriteService.getFavorites(userId,request);
    }
}
