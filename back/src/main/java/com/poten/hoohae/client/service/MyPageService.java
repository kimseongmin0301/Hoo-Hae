package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.dto.req.UserRequestDto;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.dto.req.MyPageRequestDto;
import com.poten.hoohae.client.dto.req.ReportDto;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final BoardRepository boardRepository;

    public MyPageRequestDto getMyPage(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        Long boardCnt = boardRepository.countByUserId(user.getUserId());
        Long adoptedCnt = boardRepository.countByUserIdAndAdoptionIdIsNotNull(user.getUserId());
        Long scrapCnt = scrapRepository.countByUserId(user.getUserId());

        return MyPageRequestDto.builder()
                .boardCnt(boardCnt)
                .adoptedCnt(adoptedCnt)
                .scrapCnt(scrapCnt)
                .user(UserRequestDto.builder()
                        .nickname(user.getNickname())
                        .age(user.getAge())
                        .characterId(user.getCharacterId())
                        .build())
                .report(getCategoryReportByUserId(user.getUserId()))
                .build();
    }

    public List<ReportDto> getCategoryReportByUserId(String userId) {
        Pageable topThree = PageRequest.of(0, 3);
        List<Object[]> results = boardRepository.countByUserIdGroupByCategoryTop3(userId, topThree);

        // Object[] 결과를 ReportDto 리스트로 변환
        List<ReportDto> reportDtoList = results.stream()
                .map(result -> ReportDto.builder()
                        .category((String) result[0])  // 카테고리
                        .count((Long) result[1])       // 카운트
                        .build())
                .collect(Collectors.toList());

        return reportDtoList;
    }
}
