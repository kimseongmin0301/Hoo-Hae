package com.poten.hoohae.client.dto.req;

import com.poten.hoohae.auth.dto.req.UserRequestDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MyPageRequestDto {
    private Long boardCnt;
    private Long adoptedCnt;
    private Long scrapCnt;
    private UserRequestDto user;
    private List<ReportDto> report;
}
