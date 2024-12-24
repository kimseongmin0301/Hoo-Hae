package com.poten.hoohae.client.service;

import com.poten.hoohae.client.domain.Question;
import com.poten.hoohae.client.dto.res.QuestionResponseDto;
import com.poten.hoohae.client.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Cacheable(value = "todayQuestion", key = "#today", unless = "#result == null")
    public synchronized QuestionResponseDto getTodayQuestion(LocalDate today) {
        Optional<Question> randomQuestionOptional = questionRepository.findRandomWithMinCount();
        if (randomQuestionOptional.isPresent()) {
            Question selectedQuestion = randomQuestionOptional.get();
            questionRepository.incrementCount(selectedQuestion.getId());
            return QuestionResponseDto.builder()
                    .body(selectedQuestion.getBody())
                    .category(selectedQuestion.getCategory())
                    .build();
        } else {
            throw new RuntimeException("No questions available");
        }
    }
}
