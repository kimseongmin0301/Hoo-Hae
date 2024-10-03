package com.poten.hoohae.client.service;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.domain.File;
import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import com.poten.hoohae.client.repository.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final FileRepository fileRepository;

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
        List<Map<String, String>> imageUrls = new ArrayList<>();

        if(images == null) {

        } else if (images.size() > 3) {
            throw new IllegalArgumentException("이미지 수가 3개를 초과합니다.");
        } else {
            imageUrls = s3Service.uploadFiles(images);
            thumbnailUrl = imageUrls.isEmpty() ? null : imageUrls.get(0).get("link");
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
        Long id = boardRepository.save(board).getId();

        for (Map<String, String> imageUrl : imageUrls) {
            File file = File.builder()
                    .name(imageUrl.get("name"))
                    .orgName(imageUrl.get("orgName"))
                    .boardId(id)
                    .link(imageUrl.get("link"))
                    .build();

            fileRepository.save(file);
        }

        return id;
    }

    public BoardResponseDto getBoard(Long id) {
        Optional<Board> boardOptional = boardRepository.findById(id);

        return boardOptional.map(board -> BoardResponseDto.builder()
                .id(board.getId())
                .subject(board.getSubject())
                .body(board.getBody())
                .vote(board.getVote())
                .nickname(board.getNickname())
                .age(board.getAge())
                .commentCnt(commentRepository.countCommentByBoardId(id))
                .images(fileRepository.findByName(id))
                .createdAt(board.getCreatedAt())
                .build()
        ).orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
    }

    @Transactional
    public Long updateBoard(Long id, BoardRequestDto reqDto, String userId){
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException());
        System.out.println("board.getUserId() = " + board.getUserId());
        System.out.println("userId = " + userId);
        if(!board.getUserId().equals(userId)) {
            throw new RuntimeException("수정권한없음");
        }

        Board updatedBoard = Board.builder()
                .id(board.getId()) // 기존 ID 유지
                .nickname(board.getNickname())
                .subject(reqDto.getSubject()) // 새로운 제목
                .body(reqDto.getBody()) // 새로운 내용
                .vote(board.getVote())
                .adoptionId(board.getAdoptionId())
                .thumbnail(board.getThumbnail())
                .age(board.getAge())
                .category(board.getCategory())
                .type(board.getType())
                .userId(userId) // 기존 작성자 유지
                .createdAt(board.getCreatedAt())
                .build();
        boardRepository.save(updatedBoard);

        return id;
    }

    @Transactional
    public Long deleteBoard(Long id, String userId) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException());
        if(!board.getUserId().equals(userId)) {
            throw new RuntimeException("수정권한없음");
        }

        boardRepository.delete(board);
        return id;
    }
}
