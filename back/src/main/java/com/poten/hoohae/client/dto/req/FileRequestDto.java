package com.poten.hoohae.client.dto.req;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileRequestDto {
    private String name;
    private Long boardId;
    private String orgName;
    private String link;
    private LocalDateTime createdAt;
}
