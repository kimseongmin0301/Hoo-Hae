package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.service.AlramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alram")
public class AlramController {
    private final AlramService alramService;

    @GetMapping("/list")
    public ResponseEntity<?> getAlramList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Authentication authentication
    ) {
        long totalItemCnt = alramService.getAlramCnt(authentication.getName());
        PagingDto dto = PagingDto.builder()
                .hasPage(Paging.hasPage(page, totalItemCnt))
                .data(alramService.getAlramList(page, authentication.getName()))
                .build();

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteAlram(@PathVariable("id") Long id) {

        return ResponseEntity.ok(alramService.deleteAlram(id));
    }
}
