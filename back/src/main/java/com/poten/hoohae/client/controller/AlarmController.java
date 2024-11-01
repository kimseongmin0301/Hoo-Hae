package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Alarm;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.service.AlarmService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping("/list")
    public ResponseEntity<?> getAlarmList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Authentication authentication
    ) {
        long totalItemCnt = alarmService.getAlarmCnt(authentication.getName());
        PagingDto dto = PagingDto.builder()
                .hasPage(Paging.hasPage(page, totalItemCnt))
                .totalCnt(totalItemCnt)
                .data(alarmService.getAlarmList(page, authentication.getName()))
                .build();

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteAlarm(@PathVariable("id") Long id) {

        return ResponseEntity.ok(alarmService.deleteAlarm(id));
    }

    @PutMapping("/click/{id}")
    public ResponseEntity<Long> clickStatus(@PathVariable("id") Long id) {

        return ResponseEntity.ok(alarmService.onClickAlarm(id));
    }
}