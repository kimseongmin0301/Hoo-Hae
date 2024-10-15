package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Scrap;
import com.poten.hoohae.client.dto.res.ScrapDto;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.ImageRepository;
import com.poten.hoohae.client.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ImageRepository imageRepository;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    private final String[] scrapList = {"진로", "건강", "자기계발", "시간", "연애", "대인관계", "경제", "기타"};
    public List<ScrapDto> findByImageScrap(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<String> imageList = imageRepository.findByImageScrap();  // 이미지 리스트를 가져옴
        List<Object[]> scrapListWithCounts = scrapRepository.countScrapByCategory(user.getUserId());  // 카테고리별 스크랩 카운트 리스트

        // 카테고리별 스크랩 카운트를 Map으로 변환 (category -> count)
        Map<String, Long> scrapCountMap = new HashMap<>();
        for (Object[] scrap : scrapListWithCounts) {
            String category = (String) scrap[0];
            Long count = (Long) scrap[1];
            scrapCountMap.put(category, count);
        }

        List<ScrapDto> result = new ArrayList<>();

        // scrapList의 카테고리 순서대로 처리
        for (int i = 0; i < scrapList.length; i++) {
            String category = scrapList[i];
            Long count = scrapCountMap.getOrDefault(category, 0L);  // 해당 카테고리 스크랩이 없으면 0

            String image = i < imageList.size() ? imageList.get(i) : "";  // 이미지가 없으면 빈 값

            ScrapDto dto = ScrapDto.builder()
                    .image(image)
                    .name(category)
                    .count(count)
                    .build();

            result.add(dto);
        }

        return result;
    }

    public Long scrapBoard(Long id, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        Optional<Board> optionalBoard = boardRepository.findById(id);
        Board board = optionalBoard.get();
        Optional<Scrap> optionalScrap = scrapRepository.findScrapByBoardId(id);

        if(optionalScrap.isEmpty()) {
            Scrap scrap = Scrap.builder()
                    .userId(user.getUserId())
                    .category(board.getCategory())
                    .boardId(id)
                    .build();

            return scrapRepository.save(scrap).getId();
        } else {
            scrapRepository.deleteById(optionalScrap.get().getId());
            return optionalScrap.get().getId();
        }
    }
}
