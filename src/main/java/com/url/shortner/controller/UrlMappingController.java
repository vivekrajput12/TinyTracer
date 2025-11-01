package com.url.shortner.controller;

import com.url.shortner.constants.CommonConstant;
import com.url.shortner.dtos.ApiResponse;
import com.url.shortner.dtos.requestDto.ClickEventAnalysisReq;
import com.url.shortner.dtos.requestDto.MappingRequest;
import com.url.shortner.dtos.responseDto.ClickEventDto;
import com.url.shortner.dtos.responseDto.UrlMappingDto;
import com.url.shortner.models.User;
import com.url.shortner.service.UrlMappingService;
import com.url.shortner.service.UserDetailsImpl;
import com.url.shortner.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UrlMappingController {
        private UrlMappingService urlMappingService;
        private UserService userService;
        @PostMapping("/mapUrl")
        @PreAuthorize("hasAuthority('admin')")
        public ResponseEntity<ApiResponse<?>> mapUrl(@Valid @RequestBody MappingRequest request , Principal principal){
            try {
                String originalUrl = request.getOriginalUrl();
                System.out.println("principal "+ principal);
                User user = userService.findUserByUid(request.getUid());
                UrlMappingDto urlMappingDto = urlMappingService.createShortUrl(originalUrl , user);
                ApiResponse <UrlMappingDto> apiResponse = new ApiResponse<>(0 , CommonConstant.REQUEST_SUCCESSFUL , urlMappingDto);
                return ResponseEntity.ok(apiResponse);
            } catch (Exception e) {
                String failureMsg = (e.getMessage() != null && !e.getMessage().isBlank())
                        ? e.getMessage()
                        : CommonConstant.REQUEST_FAILED;
                ApiResponse <String> apiResponse = new ApiResponse<>(-1 , failureMsg);
                return ResponseEntity.ok(apiResponse);
            }
        }
    @GetMapping("/getAllUrlsForUser")
    public ResponseEntity<ApiResponse<?>> getAllUrlForUser(Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            long uid = userDetails.getUid();
            List<UrlMappingDto> urls = urlMappingService.getAllUrlForUser(uid);
            return ResponseEntity.ok(new ApiResponse<>(0 , CommonConstant.REQUEST_SUCCESSFUL , urls));
        } catch (Exception e) {
            String failureMsg = (e.getMessage() != null && !e.getMessage().isBlank())
                    ? e.getMessage()
                    : CommonConstant.REQUEST_FAILED;
            return ResponseEntity.ok(new ApiResponse<>(-1 , failureMsg));
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<List<ClickEventDto>>> getUrlAnalysis(ClickEventAnalysisReq clickEventAnalysisReq) {
        try {
            List<ClickEventDto> urls = urlMappingService.getUrlAnalysisService(clickEventAnalysisReq);
            return ResponseEntity.ok(new ApiResponse<>(0 , CommonConstant.REQUEST_SUCCESSFUL , urls));
        } catch (Exception e) {
            String failureMsg = (e.getMessage() != null && !e.getMessage().isBlank())
                    ? e.getMessage()
                    : CommonConstant.REQUEST_FAILED;
            return ResponseEntity.ok(new ApiResponse<>(-1 , failureMsg));
        }
    }

    @GetMapping("/getTotalClicks")
    public ResponseEntity<ApiResponse<Map<LocalDate , Long>>> getTotalClicksOfUser(
            Authentication authentication   ,@RequestParam(required = false) String startDate ,@RequestParam(required = false) String endDate) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long uid = userDetails.getUid();
        Map<LocalDate , Long> result = urlMappingService.getTotalClicksOfUserService(uid , startDate , endDate);
        return ResponseEntity.ok(new ApiResponse<>(0 , CommonConstant.REQUEST_SUCCESSFUL , result));
    }
}
