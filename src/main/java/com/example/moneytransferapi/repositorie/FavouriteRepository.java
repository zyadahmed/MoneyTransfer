package com.example.moneytransferapi.repositorie;

import com.example.moneytransferapi.dto.FavouriteDTO;
import com.example.moneytransferapi.entity.Favourite;
import com.example.moneytransferapi.entity.FavouriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
    boolean existsById(FavouriteId id);
    List<Favourite> findByUserId(int userId);
    void deleteById(FavouriteId id);
}
