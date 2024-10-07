package com.poten.hoohae.client.service;

import com.poten.hoohae.client.domain.Question;
import com.poten.hoohae.client.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @CachePut(value = "todayQuestion", key = "#today")
    public Question setTodayQuestion(LocalDate today) {
        Optional<Question> randomQuestionOptional = questionRepository.findRandom();
        if (randomQuestionOptional.isPresent()) {
            return randomQuestionOptional.get(); // 랜덤 질문 반환
        } else {
            throw new RuntimeException("No questions available");
        }
    }

    @Cacheable(value = "todayQuestion", key = "#today")
    public Question getTodayQuestion(LocalDate today) {
        return null;
    }

}
