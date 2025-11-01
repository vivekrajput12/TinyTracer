package com.url.shortner.controller;

import com.url.shortner.dtos.ApiResponse;
import com.url.shortner.models.UrlMapping;
import com.url.shortner.service.UrlMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RedirectHandler {

    private UrlMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String shortUrl){
        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
        if(urlMapping != null ){
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.add("Location" , urlMapping.getOriginalUrl());
            return ResponseEntity.status(302).headers(httpHeader).build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
