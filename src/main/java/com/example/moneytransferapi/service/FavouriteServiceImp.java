package com.example.moneytransferapi.service;

import com.example.moneytransferapi.dto.FavouriteDTO;
import com.example.moneytransferapi.dto.ResponseFavouriteDTO;
import com.example.moneytransferapi.entity.Favourite;
import com.example.moneytransferapi.entity.FavouriteId;
import com.example.moneytransferapi.exception.FavouriteNotFound;
import com.example.moneytransferapi.exception.UnauthorizedAccessException;
import com.example.moneytransferapi.repositorie.FavouriteRepository;
import com.example.moneytransferapi.utilitys.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImp implements IFavouriteService {

    private final FavouriteRepository favouriteRepository;

    private final ModelMapper mapper;

    private final JwtUtil jwtUtil;

    public FavouriteDTO addFavourite(FavouriteDTO favouriteDTO, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        int userIdToken = jwtUtil.extractUserId(token);
//        Optional<Favourite> favouriteOpt = favouriteRepository.findByUserIdAndAccountId
//                (favouriteDTO.getUserId(), favouriteDTO.getAccountId());

        if (userIdToken != favouriteDTO.getUserId()) {
            throw new UnauthorizedAccessException("Wrong Resources");
        }
        Favourite favourite = new Favourite();
        favourite.setUserId(userIdToken); //
        favourite.setAccountId(favouriteDTO.getAccountId());
        favourite.setName(favouriteDTO.getName());
        favouriteRepository.save(favourite);

        favouriteDTO.setUserId(favourite.getUserId());
        favouriteDTO.setAccountId(favourite.getAccountId());
        favouriteDTO.setName(favourite.getName());

        return favouriteDTO;
    }

    @Override
    public ResponseFavouriteDTO deleteFavourite(FavouriteDTO favouriteDTO) {
        FavouriteId favouriteId = new FavouriteId(favouriteDTO.getUserId(), favouriteDTO.getAccountId());
        if (!favouriteRepository.existsById(favouriteId)) {
            throw new FavouriteNotFound("Favourite Not Found");
        }
        favouriteRepository.deleteById(favouriteId);

        ResponseFavouriteDTO responseFavouriteDTO = new ResponseFavouriteDTO();
        responseFavouriteDTO.setMessage("Deleted Successfully");

        return responseFavouriteDTO;
    }

    @Override
    public List<FavouriteDTO> getFavorites(FavouriteDTO favouriteDTO) {
        return favouriteRepository.findByUserId(favouriteDTO.getUserId())
                .stream()
                .map(favourite -> mapper.map(favourite, FavouriteDTO.class))
                .toList();
    }
}
