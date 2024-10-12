package com.poten.hoohae.client.scheduler;

import com.poten.hoohae.client.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CacheScheduler {
    private final CacheManager cacheManager;
    private final QuestionService questionService;

    // 매일 자정에 실행 (자정 0시 0분 0초)
    @Scheduled(cron = "0 0 0 * * *")
    public void clearTodayQuestionCache() {
        // 캐시 무효화
        cacheManager.getCache("todayQuestion").clear();

        // 새로운 질문을 캐시에 저장 (오늘 날짜 기준)
        questionService.setTodayQuestion(LocalDate.now());
    }
}
