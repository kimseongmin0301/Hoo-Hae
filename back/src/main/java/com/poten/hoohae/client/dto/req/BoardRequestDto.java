package com.poten.hoohae.client.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {
    private String subject;
    private String body;
    private List<MultipartFile> image;
    private String type;
    private Long age;
    private Long category;
}
