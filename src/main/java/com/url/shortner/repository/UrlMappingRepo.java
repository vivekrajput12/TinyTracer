package com.url.shortner.repository;

import com.url.shortner.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlMappingRepo extends JpaRepository<UrlMapping , Long> {
    boolean existsByShortUrl(String shortCode);
    List <UrlMapping> findAllByUserUid(long uid);
}
