package com.poten.hoohae.auth.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    private String token;
    private String role;
}
