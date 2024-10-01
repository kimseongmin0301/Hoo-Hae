package com.poten.hoohae.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingDto<T> {
    private Long totalItems;
    private int currentPage;
    private T data;
}