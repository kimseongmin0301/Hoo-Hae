package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.AlramEnum;
import com.poten.hoohae.client.domain.Alram;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.repository.AlramRepository;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdoptedService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final AlramRepository alramRepository;

    public Long adopted(Long id, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        Optional<Comment> optionalComment = commentRepository.findById(id);
        Comment comment = optionalComment.get();

        Optional<Board> optionalBoard = boardRepository.findById(comment.getBoardId());
        Board board = optionalBoard.get();

        Comment saveComment = Comment.builder()
                .userId(comment.getUserId())
                .nickname(comment.getNickname())
                .body(comment.getBody())
                .vote(comment.getVote())
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .createdAt(comment.getCreatedAt())
                .age(comment.getAge())
                .adoptStatus(1L)
                .build();

        Board saveBoard = Board.builder()
                .adoptionId(comment.getId())
                .age(board.getAge())
                .createdAt(board.getCreatedAt())
                .id(board.getId())
                .vote(board.getVote())
                .body(board.getBody())
                .category(board.getCategory())
                .nickname(board.getNickname())
                .subject(board.getSubject())
                .thumbnail(board.getThumbnail())
                .type(board.getType())
                .userId(board.getUserId())
                .question(board.getQuestion())
                        .build();

        Alram alram = Alram.builder()
                .userId(user.getUserId())
                .body(board.getBody())
                .nickname(user.getNickname())
                .type("adopted")
                .msg(String.valueOf(AlramEnum.ADOPTED))
                .commentId(null)
                .boardId(id)
                .build();

        commentRepository.save(saveComment);
        boardRepository.save(saveBoard);
        return alramRepository.save(alram).getId();
    }
}
