package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.AlarmEnum;
import com.poten.hoohae.client.domain.Alarm;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.repository.AlarmRepository;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdoptedService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
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

        if(!user.getUserId().equals(comment.getUserId())) {
            Alarm alarm = Alarm.builder()
                    .userId(comment.getUserId())
                    .body(board.getBody())
                    .nickname(user.getNickname())
                    .type("adopted")
                    .msg(String.valueOf(AlarmEnum.ADOPTED))
                    .commentId(id)
                    .boardId(board.getId())
                    .age(user.getAge())
                    .build();
            alarmRepository.save(alarm);
        }

        commentRepository.save(saveComment);

        return boardRepository.save(saveBoard).getId();
    }
}
