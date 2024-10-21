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

    @CachePut(value = "todayQuestion", key = "#today", condition = "#today != null")
    public QuestionResponseDto setTodayQuestion(LocalDate today) {
        // count가 가장 적은 질문을 랜덤하게 가져옴
        Optional<Question> randomQuestionOptional = questionRepository.findRandomWithMinCount();
        if (randomQuestionOptional.isPresent()) {
            Question selectedQuestion = randomQuestionOptional.get();
            // count 증가
            questionRepository.incrementCount(selectedQuestion.getId());
            return QuestionResponseDto.builder()
                    .body(selectedQuestion.getBody())
                    .category(selectedQuestion.getCategory())
                    .build();
        } else {
            throw new RuntimeException("No questions available");
        }
    }

    @Cacheable(value = "todayQuestion", key = "#today", condition = "#today != null")
    public QuestionResponseDto getTodayQuestion(LocalDate today) {
        return setTodayQuestion(today);
    }

}
