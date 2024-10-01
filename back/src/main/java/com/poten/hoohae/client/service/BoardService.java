package com.poten.hoohae.client.service;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

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
                        .nickname(b.getNickanme())
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

    public Long saveBoard(BoardRequestDto dto) {
        Board board = Board.builder()
                .subject(dto.getSubject())
                .body(dto.getBody())

                .build();

        return 1L;
    }
}
