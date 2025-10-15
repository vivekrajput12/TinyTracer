package com.url.shortner.service;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.url.shortner.dtos.UrlMappingDto;
import com.url.shortner.models.UrlMapping;
import com.url.shortner.models.User;
import com.url.shortner.repository.UrlMappingRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlMappingService {
    private final UrlMappingRepo urlMappingRepo;
    private final String applicationName;

    public UrlMappingService(UrlMappingRepo urlMappingRepo, @Value("${applicationName}") String applicationName) {
        this.urlMappingRepo = urlMappingRepo;
        this.applicationName = applicationName;
    }
    public UrlMappingDto createShortUrl (String originalUrl , User user){
        String shortUrl = generateRandomCode();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMapping.setUser(user);
        UrlMapping savedObj = urlMappingRepo.save(urlMapping);
        return convertToDto(savedObj);
    }
    private UrlMappingDto convertToDto(UrlMapping urlMapData){
        UrlMappingDto dto = new UrlMappingDto();
        dto.setUid(urlMapData.getUser().getUid());
        dto.setOriginalUrl(urlMapData.getOriginalUrl());
        String completeShortUrl = buildUrl(urlMapData.getShortUrl());
        dto.setShortUrl(completeShortUrl);
        dto.setCreatedAt(urlMapData.getCreatedDate());
        dto.setUrlId(urlMapData.getUrlId());
        return dto;
    }

    private String generateRandomCode() {
        String randomCode;
        do{
            randomCode = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR , NanoIdUtils.DEFAULT_ALPHABET, 8);
        }while (urlMappingRepo.existsByShortUrl(randomCode));
        return randomCode;
    }
    private String buildUrl(String randomCode){
        String newUrl = "https://" + applicationName + "/"+ randomCode;
        return newUrl;
    }

    public List<UrlMappingDto> getAllUrlForUser(long uid){
        return urlMappingRepo.findAllByUserUid(uid)
                .stream()
                .map(ele->convertToDto(ele))
                .toList();
    }
}