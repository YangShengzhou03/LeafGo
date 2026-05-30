package com.leafgo.repository;

import com.leafgo.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    Optional<Favorite> findByUserIdAndJobId(Long userId, Long jobId);
    
    List<Favorite> findByUserId(Long userId);
    
    @Modifying
    @Transactional
    void deleteByUserIdAndJobId(Long userId, Long jobId);
    
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
