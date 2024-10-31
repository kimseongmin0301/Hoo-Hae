package com.poten.hoohae.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingDto<T> {
    private Boolean hasPage;
    private Long totalCnt;
    private T data;
}