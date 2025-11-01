package com.url.shortner.repository;

import com.url.shortner.models.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepo extends JpaRepository<ClickEvent , Long> {
    List <ClickEvent> findByUrlMappingUrlIdAndClickDateBetween(long urlId , LocalDateTime startDate ,LocalDateTime endDate);
    List <ClickEvent> findByUrlIdAndClickDateBetween(long urlId, LocalDateTime startDate , LocalDateTime endDate);
    List <ClickEvent> findByUrlIdInAndClickDateBetween(List<Long> urlIds, LocalDateTime startDate , LocalDateTime endDate);
}
