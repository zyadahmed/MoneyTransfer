package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.FavouriteDTO;
import com.example.moneytransferapi.dto.ResponseAccountDto;
import com.example.moneytransferapi.dto.ResponseFavouriteDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

public interface IFavouriteService {
    FavouriteDTO addFavourite(FavouriteDTO favouriteDTO, HttpServletRequest request);

    ResponseFavouriteDTO deleteFavourite(Long accountId,HttpServletRequest request);

    List<FavouriteDTO> getFavorites(int userId,HttpServletRequest request);
}
