package com.url.shortner.repository;

import com.url.shortner.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepo extends JpaRepository<UrlMapping , Long> {
    boolean existsByShortUrl(String shortCode);
    List <UrlMapping> findAllByUserUid(long uid);

    @Query("select u.urlId from UrlMapping u where u.user.uid = :uid")
    List <Long> findAllUrlIdsByUserUid( @Param("uid") long uid);

    UrlMapping findByShortUrl(String shortUrl);
}
