package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.dto.req.CommentRequestDto;
import com.poten.hoohae.client.dto.res.CommentResponseDto;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import com.poten.hoohae.client.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;

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
                .map(c -> CommentResponseDto.builder()
                        .boardId(c.getBoardId())
                        .userId(c.getUserId())
                        .id(c.getId())
                        .nickname(c.getNickname())
                        .age(c.getAge())
                        .vote(c.getVote())
                        .createdAt(c.getCreatedAt())
                        .isWriter(c.getUserId().equals(user.getUserId()) ? true : false)
                        .isAdopted(board.getAdoptionId() == c.getId() ? true : false)
                        .img(img)
                        .build())
                .collect(Collectors.toList());
    }

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

        return commentRepository.save(comment).getId();
    }

    public Long updateComment(Long id, CommentRequestDto dto, String userId) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException());

        if(!comment.getUserId().equals(userId)) {
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

    public Long deleteComment(Long id, String userId) {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        commentRepository.deleteById(id);
        return id;
    }
}
