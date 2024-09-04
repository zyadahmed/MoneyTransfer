package com.example.moneytransferapi.repositorie;

import com.example.moneytransferapi.entity.Favourite;
import com.example.moneytransferapi.entity.FavouriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
    Optional<Favourite> findByUserId(Long userId);
//    Boolean existsFavouriteByUserIdAndAccountId(Long accountId);
    Optional<Favourite> findByUserIdAndAccountId(int userId, int accountId);
}
