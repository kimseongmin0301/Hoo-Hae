package com.poten.hoohae.client.service;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    private final S3Service s3Service;

    public List<BoardResponseDto> getBoardList(int page, Long age) {
        Page<Board> board;
        Pageable pageable = PageRequest.of(Paging.getPage(page, this.totalBoardCnt(age)) - 1, 5, Sort.by("createdAt").descending());
        if (age == null) {
            board = boardRepository.findAll(pageable);
        } else {
            board = boardRepository.findAllByAge(pageable, age);
        }

        return board.getContent().stream()
                .map(b -> BoardResponseDto.builder()
                        .id(b.getId())
                        .subject(b.getSubject())
                        .body(b.getBody())
                        .vote(b.getVote())
                        .commentCnt(commentRepository.countCommentByBoardId(b.getId()))
                        .thumbnail(1L)
                        .userId(b.getUserId())
                        .age(age)
                        .isAdopte(b.getAdoptionId() != null)
                        .nickname(b.getNickname())
                        .category(b.getCategory())
                        .type(b.getType())
                        .createdAt(b.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public long totalBoardCnt(Long age) {
        if(age == null)
            return boardRepository.count();
        return boardRepository.countBoardsByAge(age);
    }

    @Transactional
    public Long saveBoard(BoardRequestDto dto) throws IOException {
        List<MultipartFile> images = dto.getImage();
        String thumbnailUrl = "";

        if(images == null) {

        } else if (images.size() > 3) {
            throw new IllegalArgumentException("이미지 수가 3개를 초과합니다.");
        } else {
            List<String> imageUrls = s3Service.uploadFiles(images);
            thumbnailUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);
        }

        // 게시글 데이터 저장
        Board board = Board.builder()
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .subject(dto.getSubject())
                .body(dto.getBody())
                .thumbnail(thumbnailUrl) // 썸네일 설정
                .vote(0L) // 초기 투표 수
                .age(dto.getAge())
                .category(dto.getCategory())
                .type(dto.getType())
                .build();

        return boardRepository.save(board).getId(); // 게시글 ID 반환
    }
}
