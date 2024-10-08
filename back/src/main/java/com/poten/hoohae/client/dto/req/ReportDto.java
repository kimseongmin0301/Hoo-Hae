package com.poten.hoohae.client.dto.req;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReportDto {
    private String category;
    private Long count;
}