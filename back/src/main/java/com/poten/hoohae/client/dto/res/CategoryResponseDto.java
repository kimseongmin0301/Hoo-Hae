package com.poten.hoohae.client.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDto {
    private String name;
    private String image;
}
