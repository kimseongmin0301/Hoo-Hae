package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.DateFormat;
import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.File;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.repository.*;
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
import java.time.LocalDateTime;
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
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final ScrapRepository scrapRepository;

    private final S3Service s3Service;

    public List<BoardResponseDto> getBoardList(int page, Long age, String category) {
        Page<Board> board;
        Pageable pageable = PageRequest.of(Paging.getPage(page, this.totalBoardCnt(age)) - 1, 5, Sort.by("createdAt").descending());
        if (age == null) {
            if(category == null || category.equals(""))
                board = boardRepository.findAll(pageable);
            else
                board = boardRepository.findByCategory(pageable, category);
        } else {
            if(category == null || category.equals(""))
                board = boardRepository.findAllByAge(pageable, age);
            else
                board = boardRepository.findAllByAgeAndCategory(pageable, age, category);
        }

        return board.getContent().stream()
                .map(b -> BoardResponseDto.builder()
                        .id(b.getId())
                        .subject(b.getSubject())
                        .body(b.getBody())
                        .vote(voteRepository.countVoteByBoardId(b.getId()))
                        .commentCnt(commentRepository.countCommentByBoardId(b.getId()))
                        .thumbnail(b.getThumbnail())
                        .userId(b.getUserId())
                        .age(b.getAge())
                        .isAdopte(b.getAdoptionId() != null)
                        .nickname(b.getNickname())
                        .category(b.getCategory())
                        .type(b.getType())
                        .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    public long totalBoardCnt(Long age) {
        if(age == null)
            return boardRepository.count();
        return boardRepository.countBoardsByAge(age);
    }

    @Transactional
    public Long saveBoard(BoardRequestDto dto, String userId) throws IOException {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();

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
                .userId(user.getUserId())
                .nickname(user.getNickname())
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
                .nickname(board.getNickname())
                .age(board.getAge())
                .isAdopte(board.getAdoptionId() != null ? true : false)
                .commentCnt(commentRepository.countCommentByBoardId(id))
                .images(fileRepository.findByName(id))
                .createdAt(DateFormat.yyyyMMdd(board.getCreatedAt()))
                .userId(board.getUserId())
                .type(board.getType())
                .category(board.getCategory())
                .thumbnail(board.getThumbnail())
                .isBookmark(scrapRepository.findByBoardId(board.getId()) != null ? true : false)
                .isVoted(voteRepository.findByNickname(board.getId(), "board") != null ? true : false)
                .build()
        ).orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
    }

    @Transactional
    public Long updateBoard(Long id, BoardRequestDto reqDto, String email) throws IOException {
        Optional<User> user = userRepository.findByEmail(email);
        Board board = boardRepository.findById(user.get().getId()).orElseThrow(() -> new RuntimeException());

        if(!board.getUserId().equals(user.get().getUserId())) {
            throw new RuntimeException("수정권한없음");
        }

        List<MultipartFile> images = reqDto.getImage();
        String thumbnailUrl = "";
        List<Map<String, String>> imageUrls = new ArrayList<>();

        if(images == null) {

        } else if (images.size() > 3) {
            throw new IllegalArgumentException("이미지 수가 3개를 초과합니다.");
        } else {
            imageUrls = s3Service.uploadFiles(images);
            thumbnailUrl = imageUrls.isEmpty() ? null : imageUrls.get(0).get("link");
        }

        Board updatedBoard = Board.builder()
                .id(board.getId())
                .nickname(board.getNickname())
                .subject(reqDto.getSubject())
                .body(reqDto.getBody())
                .vote(board.getVote())
                .adoptionId(board.getAdoptionId())
                .thumbnail(board.getThumbnail())
                .age(board.getAge())
                .category(board.getCategory())
                .type(board.getType())
                .userId(board.getUserId())
                .createdAt(board.getCreatedAt())
                .build();
        boardRepository.save(updatedBoard);

        return id;
    }

    @Transactional
    public Long deleteBoard(Long id, String userId) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException());
        if(!board.getUserId().equals(userId)) {
            throw new RuntimeException("삭제권한없음");
        }

        boardRepository.delete(board);
        return id;
    }

    public List<BoardResponseDto> getTop5Boards (Long age) {
        Pageable top5 = PageRequest.of(0, 5);
        List<Object[]> boardList;
        if(age != 0) {
            boardList = boardRepository.findTop5ByAgeWithCommentAndVoteCount(age, top5);
        } else {
            boardList = boardRepository.findTop5ByCommentAndVoteCount(top5);
        }

        return boardList.stream().map(row -> {
            Long id = (Long) row[0];
            String subject = (String) row[1];
            String thumbnail = (String) row[2];
            String body = (String) row[3];
            Long boardAge = (Long) row[4]; // age 필드 추가
            String userId = (String) row[5]; // userId 필드 추가
            String nickname = (String) row[6]; // nickname 필드 추가
            String category = (String) row[7]; // category 필드 추가
            String type = (String) row[8]; // type 필드 추가
            LocalDateTime createdAt = (LocalDateTime) row[9]; // createdAt 필드 추가
            Long commentCnt = (Long) row[10]; // commentCnt 필드
            Long voteCnt = (Long) row[11]; // voteCnt 필드
            Long isAdopte = (Long) row[12];

            return BoardResponseDto.builder()
                    .id(id)
                    .subject(subject)
                    .thumbnail(thumbnail)
                    .body(body)
                    .age(boardAge)
                    .userId(userId)
                    .nickname(nickname)
                    .category(category)
                    .type(type)
                    .createdAt(DateFormat.yyyyMMdd(createdAt))
                    .commentCnt(commentCnt)
                    .vote(voteCnt)
                    .isAdopte(isAdopte != null)
                    .build();
        }).collect(Collectors.toList());
    }

    public Long countByCategory(String category){
        return boardRepository.countBoardsByCategory(category);
    }

    public List<BoardResponseDto> getBoardCategoryList(int page, String category) {
        Pageable pageable = PageRequest.of(Paging.getPage(page, this.countByCategory(category)) - 1, 5, Sort.by("createdAt").descending());
        Page<Board> board = boardRepository.findByCategory(pageable, category);

        return board.getContent().stream()
                .map(b -> BoardResponseDto.builder()
                        .id(b.getId())
                        .subject(b.getSubject())
                        .body(b.getBody())
                        .vote(voteRepository.countVoteByBoardId(b.getId()))
                        .commentCnt(commentRepository.countCommentByBoardId(b.getId()))
                        .thumbnail(b.getThumbnail())
                        .userId(b.getUserId())
                        .age(b.getAge())
                        .isAdopte(b.getAdoptionId() != null)
                        .nickname(b.getNickname())
                        .category(b.getCategory())
                        .type(b.getType())
                        .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }
}
