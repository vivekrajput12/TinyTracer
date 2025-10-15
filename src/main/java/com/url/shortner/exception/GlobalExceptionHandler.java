package com.url.shortner.exception;

import com.url.shortner.constants.CommonConstant;
import com.url.shortner.dtos.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex){
            List<String> errors =  ex.getBindingResult()
                                    .getAllErrors()
                                    .stream()
                                    .map(error-> {
                                        if(error instanceof FieldError fieldError){
                                            return fieldError.getDefaultMessage();
                                        }else {
                                            return error.getDefaultMessage();
                                        }
                                    }).collect(Collectors.toList());
            ApiResponse <List<String>> apiResponse = new ApiResponse<>(-1 , CommonConstant.REQUEST_FAILED , errors);
            return ResponseEntity.ok(apiResponse);
        }
}
