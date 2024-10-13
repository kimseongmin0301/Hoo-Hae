package com.poten.hoohae.client.dto.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScrapDto {
    private String image;
    private String name;
    private Long count;
}
