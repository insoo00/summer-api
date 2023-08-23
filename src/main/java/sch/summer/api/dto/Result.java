package sch.summer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String message;

    //==생성 메서드==//
    public static Result createOk() {
        return Result.builder()
                .message("success")
                .build();
    }

    public static Result createFail() {
        return Result.builder()
                .message("fail")
                .build();
    }
}
