package com.spacmanager.spaccore.repository;

import com.spacmanager.spaccore.entity.Spac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpacRepository extends JpaRepository<Spac, Long> {
}