package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.domain.Question;
import com.poten.hoohae.client.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/today")
    public ResponseEntity<Question> getTodayQuestion() {
        LocalDate today = LocalDate.now();
        Question question = questionService.getTodayQuestion(today);
        if (question == null) {
            question = questionService.setTodayQuestion(today);
        }
        return ResponseEntity.ok(question);
    }
}
