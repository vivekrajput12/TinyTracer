package com.url.shortner.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.util.Optional;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private Optional<T> data;
    public ApiResponse(Integer status , String message, T data){
            this.status = status;
            this.message = message;
            this.data = Optional.ofNullable(data);
    }
    public ApiResponse(Integer status , String message){
        this.status = status;
        this.message = message;
        this.data = Optional.empty();
    }
}