package com.poten.hoohae.client.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.AlarmEnum;
import com.poten.hoohae.client.domain.Alarm;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.domain.Vote;
import com.poten.hoohae.client.dto.req.VoteRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.repository.AlarmRepository;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import com.poten.hoohae.client.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Long getVoteCnt(Long id) {

        return voteRepository.countVoteByBoardId(id);
    }

    @Transactional
    public Long updateVote(Long id, String location, String userId) {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();

        if (location.equals("board")) {
            Optional<Vote> optionalVote = voteRepository.findByBoardIdAndUserId(id, user.getUserId());
            Vote vote = Vote.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .location(location)
                    .boardId(id)
                    .build();
            if(optionalVote.isEmpty()) {
                voteRepository.save(vote);

                Optional<Board> boardOptional = boardRepository.findById(id);
                Board board = boardOptional.get();

                board = Board.builder()
                        .id(board.getId())
                        .userId(board.getUserId())
                        .nickname(board.getNickname())
                        .subject(board.getSubject())
                        .body(board.getBody())
                        .thumbnail(board.getThumbnail()) // 썸네일 설정
                        .vote(board.getVote() + 1) // 초기 투표 수
                        .age(board.getAge())
                        .category(board.getCategory())
                        .createdAt(board.getCreatedAt())
                        .type(board.getType())
                        .question(board.getQuestion())
                        .build();

                boardRepository.save(board);

                if(!user.getUserId().equals(board.getUserId())) {
                    Alarm alarm = Alarm.builder()
                            .userId(user.getUserId())
                            .body(board.getBody())
                            .nickname(user.getNickname())
                            .type("like")
                            .msg(String.valueOf(AlarmEnum.LIKE))
                            .commentId(null)
                            .boardId(id)
                            .age(user.getAge())
                            .build();
                    alarmRepository.save(alarm);
                }
            } else {
                voteRepository.deleteById(optionalVote.get().getId());

                Optional<Board> boardOptional = boardRepository.findById(id);
                Board board = boardOptional.get();

                board = Board.builder()
                        .id(board.getId())
                        .userId(board.getUserId())
                        .nickname(board.getNickname())
                        .subject(board.getSubject())
                        .body(board.getBody())
                        .thumbnail(board.getThumbnail()) // 썸네일 설정
                        .vote(board.getVote() - 1) // 초기 투표 수
                        .age(board.getAge())
                        .category(board.getCategory())
                        .createdAt(board.getCreatedAt())
                        .type(board.getType())
                        .question(board.getQuestion())
                        .build();

                boardRepository.save(board);
            }
            return voteRepository.countVoteByBoardId(id);
        } else {
            Optional<Vote> optionalVote = voteRepository.findByCommentIdAndUserId(id, user.getUserId());
            Vote vote = Vote.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .location(location)
                    .commentId(id)
                    .build();
            if(optionalVote.isEmpty()) {
                voteRepository.save(vote);
                Optional<Comment> optionalComment = commentRepository.findById(id);
                Comment comment = optionalComment.get();

                comment = Comment.builder()
                        .id(comment.getId())
                        .userId(comment.getUserId())
                        .nickname(comment.getNickname())
                        .age(comment.getAge())
                        .boardId(comment.getBoardId())
                        .vote(comment.getVote() + 1)
                        .body(comment.getBody())
                        .createdAt(comment.getCreatedAt())
                        .build();

                commentRepository.save(comment);
                if(!user.getUserId().equals(comment.getUserId())) {
                    Alarm alarm = Alarm.builder()
                            .userId(user.getUserId())
                            .body(comment.getBody())
                            .nickname(user.getNickname())
                            .type("like")
                            .msg(String.valueOf(AlarmEnum.LIKE))
                            .commentId(id)
                            .boardId(comment.getBoardId())
                            .age(user.getAge())
                            .build();
                    alarmRepository.save(alarm);
                }
            } else {
                voteRepository.deleteById(optionalVote.get().getId());

                Optional<Comment> optionalComment = commentRepository.findById(id);
                Comment comment = optionalComment.get();

                comment = Comment.builder()
                        .id(comment.getId())
                        .userId(comment.getUserId())
                        .nickname(comment.getNickname())
                        .age(comment.getAge())
                        .boardId(comment.getBoardId())
                        .vote(comment.getVote() - 1)
                        .createdAt(comment.getCreatedAt())
                        .body(comment.getBody())
                        .build();
                commentRepository.save(comment);
            }
            return voteRepository.countVoteByCommentId(id);
        }
    }
}
