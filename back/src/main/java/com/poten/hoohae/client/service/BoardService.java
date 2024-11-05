package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.DateFormat;
import com.poten.hoohae.client.domain.*;
import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.repository.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
    private final JPAQueryFactory queryFactory;
    private final ImageRepository imageRepository;
    private final QuestionService questionService;
    LocalDate today = LocalDate.now();

    public List<BoardResponseDto> getBoardList(int page, Long age, String category, String sort, String email) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;
        Optional<User> optionalClient = userRepository.findByEmail(email);
        User clientUser = optionalClient.get();

        Pageable pageable = PageRequest.of(page - 1, 5);

        List<Board> boardList = queryFactory
                .selectFrom(board)
                .leftJoin(comment).on(comment.boardId.eq(board.id))
                .where(applyFilters(age, category, sort))
                .groupBy(board.id)
                .orderBy(getSortOrder(sort, board, comment).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BoardResponseDto> responseDtos = boardList.stream()
                .map(b -> {
                    long commentCnt = commentRepository.countCommentByBoardId(b.getId());
                    long voteCnt = b.getVote();
                    Optional<User> optionalUser = userRepository.findByUserId(b.getUserId());
                    User user = optionalUser.get();
                    String img = imageRepository.findByImage(user.getCharacterId());

                    return BoardResponseDto.builder()
                            .id(b.getId())
                            .subject(b.getSubject())
                            .body(b.getBody())
                            .vote(voteCnt)
                            .commentCnt(commentCnt)
                            .thumbnail(b.getThumbnail())
                            .userId(b.getUserId())
                            .age(b.getAge())
                            .isVoted(!voteRepository.findByBoardIdAndUserId(b.getId(), clientUser.getUserId()).isEmpty())
                            .isAdopted(b.getAdoptionId() != null)
                            .nickname(b.getNickname())
                            .category(b.getCategory())
                            .isBookmark(scrapRepository.findByBoardId(clientUser.getUserId(), b.getId()) != null ? true : false)
                            .type(b.getType())
                            .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
                            .img(img)
                            .question(questionService.getTodayQuestion(today).getBody())
                            .isQuestion(b.getQuestion().equals("Y") ? true : false)
                            .build();
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    // 정렬 조건을 설정하는 메소드
    private List<OrderSpecifier<?>> getSortOrder(String sort, QBoard board, QComment comment) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // 주어진 sort가 유효한 경우
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "vote":
                    orderSpecifiers.add(board.vote.desc());
                    break;
                case "commentCnt":
                    orderSpecifiers.add(comment.count().desc());
                    break;
                default:
                    break;
            }
        }

        // createdAt을 기본적으로 내림차순으로 정렬 추가
        orderSpecifiers.add(board.createdAt.desc());

        return orderSpecifiers;
    }

    private BooleanExpression applyFilters(Long age, String category, String sort) {
        QBoard board = QBoard.board;
        BooleanExpression predicate = board.isNotNull(); // 기본 조건

        if (age != null) {
            predicate = predicate.and(board.age.eq(age));
        }

        if (category != null && !category.isEmpty()) {
            predicate = predicate.and(board.category.eq(category));
        }

        if(sort != null && sort.equals("adopted")) {
            predicate = predicate.and(board.adoptionId.isNotNull());
        }

        return predicate;
    }

    private BooleanExpression searchApplyFilters(Long age, String category, String sort, String query) {
        QBoard board = QBoard.board;
        BooleanExpression predicate = board.isNotNull(); // 기본 조건

        if (age != null) {
            predicate = predicate.and(board.age.eq(age));
        }

        if (category != null && !category.isEmpty()) {
            predicate = predicate.and(board.category.eq(category));
        }

        if(sort != null && sort.equals("adopted")) {
            predicate = predicate.and(board.adoptionId.isNotNull());
        }

        if (query != null && !query.isEmpty()) {
            predicate = predicate.and(
                    board.subject.likeIgnoreCase("%" + query + "%")
                            .or(board.body.likeIgnoreCase("%" + query + "%"))
            );
        }

        return predicate;
    }

    public List<BoardResponseDto> getSearchList(int page, Long age, String category, String sort, String query, String email) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;
        Optional<User> optionalClient = userRepository.findByEmail(email);
        User clientUser = optionalClient.get();

        Pageable pageable = PageRequest.of(page - 1, 5);

        List<Board> boardList = queryFactory
                .selectFrom(board)
                .leftJoin(comment).on(comment.boardId.eq(board.id))
                .where(searchApplyFilters(age, category, sort, query))
                .groupBy(board.id)
                .orderBy(getSortOrder(sort, board, comment).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BoardResponseDto> responseDtos = boardList.stream()
                .map(b -> {
                    long commentCnt = commentRepository.countCommentByBoardId(b.getId());
                    long voteCnt = b.getVote();
                    Optional<User> optionalUser = userRepository.findByUserId(b.getUserId());
                    User user = optionalUser.get();
                    String img = imageRepository.findByImage(user.getCharacterId());

                    return BoardResponseDto.builder()
                            .id(b.getId())
                            .subject(b.getSubject())
                            .body(b.getBody())
                            .vote(voteCnt)
                            .commentCnt(commentCnt)
                            .thumbnail(b.getThumbnail())
                            .userId(b.getUserId())
                            .age(b.getAge())
                            .isVoted(!voteRepository.findByBoardIdAndUserId(b.getId(), clientUser.getUserId()).isEmpty())
                            .isAdopted(b.getAdoptionId() != null)
                            .nickname(b.getNickname())
                            .category(b.getCategory())
                            .isBookmark(scrapRepository.findByBoardId(clientUser.getUserId(), b.getId()) != null ? true : false)
                            .type(b.getType())
                            .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
                            .img(img)
                            .question(questionService.getTodayQuestion(today).getBody())
                            .isQuestion(b.getQuestion().equals("Y") ? true : false)
                            .build();
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    public long getSearchListCount(Long age, String category, String sort, String query) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        long count = queryFactory
                .select(board.id.countDistinct())
                .from(board)
                .leftJoin(comment).on(comment.boardId.eq(board.id))
                .where(searchApplyFilters(age, category, sort, query))
                .groupBy(board.id)
                .fetch()
                .size();

        return count;
    }

    public List<BoardResponseDto> getMyScrapList(int page, String category, String email) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;
        QScrap scrap = QScrap.scrap;
        Optional<User> optionalClient = userRepository.findByEmail(email);
        User clientUser = optionalClient.get();

        Pageable pageable = PageRequest.of(page - 1, 5);

        List<Board> boardList = queryFactory
                .selectFrom(board)
                .leftJoin(scrap).on(scrap.boardId.eq(board.id)) // 변경된 부분
                .where(myApplyScrapFilters(category, clientUser.getUserId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BoardResponseDto> responseDtos = boardList.stream()
                .map(b -> {
                    long commentCnt = commentRepository.countCommentByBoardId(b.getId());
                    long voteCnt = b.getVote();
                    Optional<User> optionalUser = userRepository.findByUserId(b.getUserId());
                    User user = optionalUser.get();
                    String img = imageRepository.findByImage(user.getCharacterId());
                    String voteId = voteRepository.findByUserId(b.getUserId(), b.getId());
                    Boolean vote = (voteId != null && voteId.equals(b.getUserId())) ? true : false;
                    return BoardResponseDto.builder()
                            .id(b.getId())
                            .subject(b.getSubject())
                            .body(b.getBody())
                            .vote(voteCnt)
                            .commentCnt(commentCnt)
                            .thumbnail(b.getThumbnail())
                            .userId(b.getUserId())
                            .age(b.getAge())
                            .isVoted(!voteRepository.findByBoardIdAndUserId(b.getId(), clientUser.getUserId()).isEmpty())
                            .isAdopted(b.getAdoptionId() != null)
                            .nickname(b.getNickname())
                            .category(b.getCategory())
                            .type(b.getType())
                            .isBookmark(scrapRepository.findByBoardId(clientUser.getUserId(), b.getId()) != null ? true : false)
                            .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
                            .img(img)
                            .question(questionService.getTodayQuestion(today).getBody())
                            .isQuestion(b.getQuestion().equals("Y") ? true : false)
                            .build();
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    public List<BoardResponseDto> getMyBoardList(int page, String category, Boolean isAdoptedFilter, String email) {
        Optional<User> optionalClient = userRepository.findByEmail(email);
        User clientUser = optionalClient.get();
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        Pageable pageable = PageRequest.of(page - 1, 5);

        List<Board> boardList = queryFactory
                .selectFrom(board)
                .leftJoin(comment).on(comment.boardId.eq(board.id))
                .where(myApplyFilters(category, clientUser.getUserId())
                        .and(isAdoptedFilter != null ? (isAdoptedFilter ? board.adoptionId.isNotNull() : board.adoptionId.isNull()) : null))
                .groupBy(board.id)
                .orderBy(getMyPageSortOrder(board).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BoardResponseDto> responseDtos = boardList.stream()
                .map(b -> {
                    long commentCnt = commentRepository.countCommentByBoardId(b.getId());
                    long voteCnt = b.getVote();
                    Optional<User> optionalUser = userRepository.findByUserId(b.getUserId());
                    User user = optionalUser.get();
                    String img = imageRepository.findByImage(user.getCharacterId());
                    String voteId = voteRepository.findByUserId(b.getUserId(), b.getId());
                    return BoardResponseDto.builder()
                            .id(b.getId())
                            .subject(b.getSubject())
                            .body(b.getBody())
                            .vote(voteCnt)
                            .commentCnt(commentCnt)
                            .thumbnail(b.getThumbnail())
                            .userId(b.getUserId())
                            .age(b.getAge())
                            .isVoted(!voteRepository.findByBoardIdAndUserId(b.getId(), clientUser.getUserId()).isEmpty())
                            .isAdopted(b.getAdoptionId() != null)  // isAdopte 설정
                            .isBookmark(scrapRepository.findByBoardId(clientUser.getUserId(), b.getId()) != null ? true : false)
                            .nickname(b.getNickname())
                            .category(b.getCategory())
                            .type(b.getType())
                            .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
                            .img(img)
                            .question(questionService.getTodayQuestion(today).getBody())
                            .isQuestion(b.getQuestion().equals("Y") ? true : false)
                            .build();
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    public List<BoardResponseDto> getReportList(int page, String category, String email) {
        Optional<User> optionalClient = userRepository.findByEmail(email);
        User clientUser = optionalClient.get();
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        Pageable pageable = PageRequest.of(page - 1, 5);

        List<Board> boardList = queryFactory
                .selectFrom(board)
                .leftJoin(comment).on(comment.boardId.eq(board.id))
                .where(myApplyFilters(category, clientUser.getUserId()))
                .groupBy(board.id)
                .orderBy(getMyPageSortOrder(board).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BoardResponseDto> responseDtos = boardList.stream()
                .map(b -> {
                    long commentCnt = commentRepository.countCommentByBoardId(b.getId());
                    long voteCnt = b.getVote();
                    Optional<User> optionalUser = userRepository.findByUserId(b.getUserId());
                    User user = optionalUser.get();
                    String img = imageRepository.findByImage(user.getCharacterId());
                    return BoardResponseDto.builder()
                            .id(b.getId())
                            .subject(b.getSubject())
                            .body(b.getBody())
                            .vote(voteCnt)
                            .commentCnt(commentCnt)
                            .thumbnail(b.getThumbnail())
                            .userId(b.getUserId())
                            .age(b.getAge())
                            .isVoted(!voteRepository.findByBoardIdAndUserId(b.getId(), clientUser.getUserId()).isEmpty())
                            .isAdopted(b.getAdoptionId() != null)  // isAdopte 설정
                            .isBookmark(scrapRepository.findByBoardId(clientUser.getUserId(), b.getId()) != null ? true : false)
                            .nickname(b.getNickname())
                            .category(b.getCategory())
                            .type(b.getType())
                            .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
                            .img(img)
                            .question(questionService.getTodayQuestion(today).getBody())
                            .isQuestion(b.getQuestion().equals("Y") ? true : false)
                            .build();
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    private List<OrderSpecifier<?>> getMyPageSortOrder(QBoard board) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        orderSpecifiers.add(board.createdAt.desc());

        return orderSpecifiers;
    }

    private BooleanExpression myApplyFilters(String category, String userId) {
        QBoard board = QBoard.board;
        BooleanExpression predicate = board.isNotNull(); // 기본 조건

        if (category != null && !category.isEmpty()) {
            predicate = predicate.and(board.category.eq(category));
        }

        if (userId != null && !userId.isEmpty()) {
            predicate = predicate.and(board.userId.eq(userId));
        }

        return predicate;
    }

    private BooleanExpression myApplyScrapFilters(String category, String userId) {
        QBoard board = QBoard.board;
        QScrap scrap = QScrap.scrap;
        BooleanExpression predicate = board.isNotNull();

        predicate = predicate.and(scrap.boardId.eq(board.id)).and(scrap.userId.eq(userId));

        if (category != null && !category.isEmpty()) {
            predicate = predicate.and(board.category.eq(category));
        }

        return predicate;
    }


    public long myTotalBoardCnt(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        return boardRepository.countByUserId(user.getUserId());
    }

    public long myBoardListCnt(String category, Boolean isAdoptedFilter, String email) {
        Optional<User> optionalClient = userRepository.findByEmail(email);
        User clientUser = optionalClient.get();
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        List<Board> boardList = queryFactory
                .selectFrom(board)
                .leftJoin(comment).on(comment.boardId.eq(board.id))
                .where(myApplyFilters(category, clientUser.getUserId())
                        .and(isAdoptedFilter != null ? (isAdoptedFilter ? board.adoptionId.isNotNull() : board.adoptionId.isNull()) : null))
                .groupBy(board.id)
                .orderBy(getMyPageSortOrder(board).toArray(new OrderSpecifier[0]))
                .fetch();

        return boardList.size();
    }

    public long myBoardReportCnt(String email, String category) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        return boardRepository.countByCategoryAndUserId(category, user.getUserId());
    }

    public long myScrapCnt(String email, String category) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        return scrapRepository.countByUserIdAndCategory(user.getUserId(), category);
    }

    public long totalBoardCnt(Long age, String sort) {
        // sort가 null인 경우 기본 카운트를 반환
        if (sort == null) {
            return age == null ? boardRepository.count() : boardRepository.countBoardsByAge(age);
        }

        switch (sort) {
            case "adopted":
                return boardRepository.countBoardsByAdopted();
            default:
                return age == null ? boardRepository.count() : boardRepository.countBoardsByAge(age);
        }
    }

    public long totalSearchCnt(Long age, String sort, String category, String query) {
        // sort가 null인 경우 기본 카운트를 반환
        if (sort == null) {
            return age == null ? boardRepository.count() : boardRepository.countBoardsByAge(age);
        }

        switch (sort) {
            case "adopted":
                return boardRepository.countBoardsByAdopted();
            default:
                return age == null ? boardRepository.count() : boardRepository.countBoardsByAge(age);
        }
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
                .question(dto.getIsQuestion() ? "Y" : "N")
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

    @Transactional
    public Long updateBoard(BoardRequestDto dto, String userId) throws IOException {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();

        fileRepository.deleteByBoardId(dto.getId());

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

        Optional<Board> optionalBoard = boardRepository.findById(dto.getId());
        Board board = optionalBoard.get();

        // 게시글 데이터 저장
        Board updateBoard = Board.builder()
                .id(board.getId())
                .userId(board.getUserId())
                .nickname(board.getNickname())
                .subject(dto.getSubject())
                .body(dto.getBody())
                .thumbnail(thumbnailUrl) // 썸네일 설정
                .vote(board.getVote()) // 초기 투표 수
                .age(board.getAge())
                .category(board.getCategory())
                .type(board.getType())
                .question(board.getQuestion())
                .createdAt(board.getCreatedAt())
                .adoptionId(board.getAdoptionId())
                .build();
        Long id = boardRepository.save(updateBoard).getId();

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

    public BoardResponseDto getBoard(Long id, String email) {
        Optional<User> optionalClient = userRepository.findByEmail(email);
        User clientUser = optionalClient.get();
        Optional<Board> boardOptional = boardRepository.findById(id);
        Optional<User> optionalUser = userRepository.findByUserId(boardOptional.get().getUserId());
        User user = optionalUser.get();
        String img = imageRepository.findByImage(user.getCharacterId());

        return boardOptional.map(board -> BoardResponseDto.builder()
                .id(board.getId())
                .subject(board.getSubject())
                .body(board.getBody())
                .nickname(board.getNickname())
                .age(board.getAge())
                .isAdopted(board.getAdoptionId() != null ? true : false)
                .commentCnt(commentRepository.countCommentByBoardId(id))
                .images(fileRepository.findByName(id))
                .createdAt(DateFormat.yyyyMMdd(board.getCreatedAt()))
                .userId(board.getUserId())
                .type(board.getType())
                .category(board.getCategory())
                .thumbnail(board.getThumbnail())
                .vote(voteRepository.countVoteByBoardId(board.getId()))
                .isBookmark(scrapRepository.findByBoardId(clientUser.getUserId(), board.getId()) != null ? true : false)
                .isVoted(!voteRepository.findByBoardIdAndUserId(board.getId(), clientUser.getUserId()).isEmpty())
                .img(img)
                .question(questionService.getTodayQuestion(today).getBody())
                .isQuestion(board.getQuestion().equals("Y") ? true : false)
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
    public Long deleteBoard(Long id, String email) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException());
        Optional<User> user = userRepository.findByEmail(email);

        if(!board.getUserId().equals(user.get().getUserId())) {
            throw new RuntimeException("삭제권한없음");
        }

        boardRepository.delete(board);
        return id;
    }

    public List<BoardResponseDto> getTop5Boards (Long age, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
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
            Long isAdopte = (Long) row[10];
            Long commentCnt = (Long) row[11]; // commentCnt 필드
            Long voteCnt = (Long) row[12]; // voteCnt 필드

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
                    .isVoted(voteRepository.findByNickname(id, "board", user.getUserId()) != null ? true : false)
                    .vote(voteCnt)
                    .isAdopted(isAdopte != null)
                    .build();
        }).collect(Collectors.toList());
    }

    public Long countByCategory(String category){
        return boardRepository.countBoardsByCategory(category);
    }

//    public List<BoardResponseDto> getBoardCategoryList(int page, String category) {
//        Pageable pageable = PageRequest.of(Paging.getPage(page, this.countByCategory(category)) - 1, 5, Sort.by("createdAt").descending());
//        Page<BoardResponseDto> board = boardRepository.findByCategory(pageable, category);
//
//        return board.getContent().stream()
//                .map(b -> BoardResponseDto.builder()
//                        .id(b.getId())
//                        .subject(b.getSubject())
//                        .body(b.getBody())
//                        .vote(voteRepository.countVoteByBoardId(b.getId()))
//                        .commentCnt(commentRepository.countCommentByBoardId(b.getId()))
//                        .thumbnail(b.getThumbnail())
//                        .userId(b.getUserId())
//                        .age(b.getAge())
//                        .isAdopted(b.getAdoptionId() != null)
//                        .nickname(b.getNickname())
//                        .category(b.getCategory())
//                        .type(b.getType())
//                        .createdAt(DateFormat.yyyyMMdd(b.getCreatedAt()))
//                        .build())
//                .collect(Collectors.toList());
//    }
}
