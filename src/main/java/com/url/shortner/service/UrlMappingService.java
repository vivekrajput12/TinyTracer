package com.url.shortner.service;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.url.shortner.constants.GroupBy;
import com.url.shortner.dtos.requestDto.ClickEventAnalysisReq;
import com.url.shortner.dtos.responseDto.ClickEventDto;
import com.url.shortner.dtos.responseDto.UrlMappingDto;
import com.url.shortner.models.ClickEvent;
import com.url.shortner.models.UrlMapping;
import com.url.shortner.models.User;
import com.url.shortner.repository.ClickEventRepo;
import com.url.shortner.repository.UrlMappingRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UrlMappingService {
    private final UrlMappingRepo urlMappingRepo;
    private final String applicationName;
    private final ClickEventRepo clickEventRepo;

    public UrlMappingService(UrlMappingRepo urlMappingRepo, @Value("${applicationName}") String applicationName , ClickEventRepo clickEventRepo) {
        this.urlMappingRepo = urlMappingRepo;
        this.applicationName = applicationName;
        this.clickEventRepo = clickEventRepo;
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

    public List<ClickEventDto> getUrlAnalysisService(ClickEventAnalysisReq clickEventAnalysisReq) {
        long urlId = clickEventAnalysisReq.getUrlId();
        DateTimeFormatter formater = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String sDate = clickEventAnalysisReq.getStartDate();
        String eDate = clickEventAnalysisReq.getEndDate();
        GroupBy groupBy = clickEventAnalysisReq.getGroupBy();
        System.out.println("group By " + groupBy);
        if (groupBy == null){
            groupBy = GroupBy.SECOND;
        }
        LocalDateTime endDate = (eDate != null && !eDate.isBlank())
                ? LocalDateTime.parse(eDate, formater)
                : LocalDateTime.now();
        LocalDateTime startDate = (sDate != null && !sDate.isBlank())
                ? LocalDateTime.parse(sDate, formater)
                : LocalDateTime.now().minusDays(7);

        List<ClickEvent> clickEventsList = clickEventRepo.findByUrlIdAndClickDateBetween(urlId , startDate , endDate);
        Map<Object , Long> groupedData = switch (groupBy) {
            case SECOND -> groupByChronoUnit(clickEventsList , ChronoUnit.SECONDS);
            case MINUTE_1 -> groupByMintues(clickEventsList , 1);
            case MINUTE_5 -> groupByMintues(clickEventsList , 5);
            case MINUTE_10 -> groupByMintues(clickEventsList , 10);
            case HOUR -> groupByChronoUnit(clickEventsList , ChronoUnit.HOURS);
            case DAY -> groupByChronoUnit(clickEventsList , ChronoUnit.DAYS);
            case WEEK -> groupByChronoUnit(clickEventsList , ChronoUnit.WEEKS);
        };
        List <ClickEventDto> result = groupedData.entrySet().stream()
                        .map(entry-> {
                            ClickEventDto clickEventDto = new ClickEventDto();
                            clickEventDto.setClickDate((LocalDateTime) entry.getKey());
                            clickEventDto.setCount(entry.getValue());
                            return clickEventDto;
                        }).collect(Collectors.toList());
        return result;
    }

    public Map<LocalDate, Long> getTotalClicksOfUserService(long uid , String sDate , String eDate) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate endDate = (sDate != null && !eDate.isBlank())
                ? LocalDate.parse(eDate, formater)
                : LocalDate.now();
        LocalDate startDate = (sDate != null && !sDate.isBlank())
                ? LocalDate.parse(sDate, formater)
                : LocalDate.now().minusDays(7);
        List <Long> urlIds = urlMappingRepo.findAllUrlIdsByUserUid(uid);
        Map<LocalDate , Long> data = clickEventRepo.findByUrlIdInAndClickDateBetween(urlIds , startDate.atStartOfDay() , endDate.plusDays(1).atStartOfDay()).stream()
                .collect(Collectors.groupingBy(click->click.getClickDate().toLocalDate() , Collectors.counting()));
        return data;
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlDetails = urlMappingRepo.findByShortUrl(shortUrl);
        if(urlDetails != null){
            urlDetails.setClickCount(urlDetails.getClickCount() + 1);
            urlMappingRepo.save(urlDetails);
            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlDetails);
            clickEventRepo.save(clickEvent);
        }
        return urlDetails;
    }
    private Map<Object , Long> groupByChronoUnit(List<ClickEvent>event , ChronoUnit unit){
        return event.stream()
                .collect(Collectors.groupingBy(
                        e->e.getClickDate().truncatedTo(unit),
                        Collectors.counting()
                        )
                );
    }
    private Map<Object , Long> groupByMintues(List<ClickEvent>event , int interval){
        System.out.println("checking " + interval);
        return event.stream()
                .collect(Collectors.groupingBy(e->{
                         LocalDateTime date = e.getClickDate();
                         int minutes = (date.getMinute()/interval)*interval;
                         return date.withMinute(minutes).withSecond(0).withNano(0);
                        } , Collectors.counting()
                        )
                );
    }
}