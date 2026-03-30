package com.reaksa.e_wingshop_api.repository;

import com.reaksa.e_wingshop_api.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    @Query("""
        SELECT b FROM Branch b
        WHERE (6371 * acos(
            cos(radians(:lat)) * cos(radians(b.latitude)) *
            cos(radians(b.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(b.latitude))
        )) < :radiusKm
        ORDER BY (6371 * acos(
            cos(radians(:lat)) * cos(radians(b.latitude)) *
            cos(radians(b.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(b.latitude))
        ))
        """)
    List<Branch> findNearby(double lat, double lng, double radiusKm);
}
