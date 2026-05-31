package com.spacmanager.spaccore.repository;

import com.spacmanager.spaccore.entity.Spac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpacRepository extends JpaRepository<Spac, Long> {

    // Useful for finding specific SPACs by ticker
    Optional<Spac> findByTickerSymbol(String tickerSymbol);

    // Useful for filtering by sector for your simulation reports
    List<Spac> findByTargetSector(String targetSector);

    // Useful for fetching all SPACs in a specific lifecycle stage
    List<Spac> findByCurrentStage(Spac.SpacStage currentStage);
}