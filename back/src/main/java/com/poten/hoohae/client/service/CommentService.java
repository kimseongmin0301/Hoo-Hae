package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.AlarmEnum;
import com.poten.hoohae.client.common.DateFormat;
import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Alarm;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.dto.req.CommentRequestDto;
import com.poten.hoohae.client.dto.res.CommentResponseDto;
import com.poten.hoohae.client.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final VoteRepository voteRepository;
    private final AlarmRepository alarmRepository;

    public long getCommentCnt(long boardId) {
        return commentRepository.countCommentByBoardId(boardId);
    }

    public List<CommentResponseDto> getCommentByBoard(Long boardId, int page, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        Pageable pageable = PageRequest.of(Paging.getPage(page, this.getCommentCnt(boardId)) - 1, 5, Sort.by("createdAt").ascending());
        Page<Comment> comment = commentRepository.findByBoardId(pageable, boardId);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.get();
        String img = imageRepository.findByImage(user.getCharacterId());

        return comment.getContent().stream()
                .map(c -> {
                    User commentUser = userRepository.findByUserId(c.getUserId()).get();

                    return CommentResponseDto.builder()
                            .boardId(c.getBoardId())
                            .userId(c.getUserId())
                            .id(c.getId())
                            .nickname(c.getNickname())
                            .age(c.getAge())
                            .vote(c.getVote())
                            .body(c.getBody())
                            .createdAt(DateFormat.yyyyMMdd(c.getCreatedAt()))
                            .isWriter(c.getUserId().equals(user.getUserId()) ? true : false)
                            .isAdopted(Objects.equals(board.getAdoptionId(), c.getId()) ? true : false)
                            .isVoted(voteRepository.findByCommentNickname(c.getId(), "comment", user.getUserId()) != null ? true : false)
                            .img(imageRepository.findByImage(commentUser.getCharacterId()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long saveComment(CommentRequestDto dto, String userId) {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();

        Comment comment = Comment.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .age(user.getAge())
                .boardId(dto.getBoardId())
                .vote(0L)
                .body(dto.getBody())
                .build();

        long result = commentRepository.save(comment).getId();
        Optional<Board> optionalBoard = boardRepository.findById(dto.getBoardId());
        Board board = optionalBoard.get();
        if(!user.getUserId().equals(board.getUserId())) {

            Alarm alarm = Alarm.builder()
                    .userId(board.getUserId())
                    .body(dto.getBody())
                    .nickname(user.getNickname())
                    .type("comment")
                    .msg(String.valueOf(AlarmEnum.COMMENT))
                    .commentId(result)
                    .boardId(dto.getBoardId())
                    .age(user.getAge())
                    .build();
            alarmRepository.save(alarm);
        }

        return result;
    }

    @Transactional
    public Long updateComment(Long id, CommentRequestDto dto, String userId) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException());
        Optional<User> optionalUser = userRepository.findByEmail(userId);

        if(!comment.getUserId().equals(optionalUser.get().getUserId())) {
            throw new RuntimeException("수정권한없음");
        }

        Comment updatedComment = Comment.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .userId(comment.getUserId())
                .nickname(comment.getNickname())
                .age(comment.getAge())
                .vote(comment.getVote())
                .createdAt(comment.getCreatedAt())
                .adoptStatus(comment.getAdoptStatus())
                .body(dto.getBody())
                .build();
        commentRepository.save(updatedComment);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long deleteComment(Long id, String userId) {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        commentRepository.deleteById(id);
        return id;
    }
}
