package com.poten.hoohae.client.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResponseDto {
    private String body;
    private String category;
}
