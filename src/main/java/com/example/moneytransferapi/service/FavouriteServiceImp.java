package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.AccountDTO;
import com.example.moneytransferapi.dto.FavouriteDTO;
import com.example.moneytransferapi.entity.Account;
import com.example.moneytransferapi.entity.Favourite;
import com.example.moneytransferapi.entity.FavouriteId;
import com.example.moneytransferapi.exception.FavouriteAlreadyExist;
import com.example.moneytransferapi.exception.FavouriteNotFound;
import com.example.moneytransferapi.exception.UnauthorizedAccessException;
import com.example.moneytransferapi.repositorie.AccountRepository;
import com.example.moneytransferapi.repositorie.FavouriteRepository;
import com.example.moneytransferapi.repositorie.UserRepository;
import com.example.moneytransferapi.utilitys.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImp implements IFavouriteService{

    private final FavouriteRepository favouriteRepository;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final JwtUtil jwtUtil;

    public FavouriteDTO addFavourite(FavouriteDTO favouriteDTO, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        int userIdToken = jwtUtil.extractUserId(token);
        Optional<Favourite> favouriteOpt = favouriteRepository.findByUserIdAndAccountId
                (favouriteDTO.getUserId(), favouriteDTO.getAccountId());
        if(userIdToken != favouriteDTO.getUserId()){
            throw new UnauthorizedAccessException("Wrong Resources");
        }
        Favourite favourite = favouriteOpt.get();
        favourite.setAccountId(favouriteDTO.getAccountId());
        favourite.setName(favouriteDTO.getName());
        favouriteRepository.save(favourite);

        favouriteDTO.setUserId(favourite.getUserId());
        favouriteDTO.setAccountId(favourite.getAccountId());
        favouriteDTO.setName(favourite.getName());

        return favouriteDTO;
    }

    @Override
    public FavouriteDTO deleteFavourite(Long accountId) {
        return null;
    }

    @Override
    public List<FavouriteDTO> getFavorites(HttpServletRequest request) {
        return List.of();
    }

//    public FavouriteDTO deleteFavourite(FavouriteDTO favouriteDTO, HttpServletRequest request) {
//        if(!favouriteRepository.existsById(new FavouriteId(1, 2))){
//            throw new FavouriteNotFound("Favourite Not Found");
//        }
//        favouriteRepository.deleteById(new FavouriteId(1, 1));
//    }

//    public List<FavouriteDTO> getFavorites(HttpServletRequest request) {
//        String token = jwtUtil.getTokenFromRequest(request);
//        int currentFavourite = jwtUtil.extractUserId(token);
//        List<Favourite> favourites = favouriteRepository.findByUserId(currentFavourite);
//        return favourites.stream().map(fav -> mapper.map(fav,FavouriteDTO.class)).toList();
//    }
}
