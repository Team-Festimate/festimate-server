package org.festimate.team.api.common;

import org.festimate.team.global.response.ApiResponse;
import org.festimate.team.global.response.ResponseBuilder;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.exception.FestimateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String healthCheck(){
        return "healtdfadsfkdjsfakd";
    }

    @GetMapping("/health/success")
    public ResponseEntity<ApiResponse<Void>> getsuccess() {
        return ResponseBuilder.ok(null);
    }

    @GetMapping("/health/created")
    public ResponseEntity<ApiResponse<Void>> getCreated() {
        return ResponseBuilder.created(null);
    }

    @GetMapping("/health/fail")
    public ResponseEntity<ApiResponse<Void>> getError() {
        throw new FestimateException(ResponseError.EXPIRED_ACCESS_TOKEN);
    }

}