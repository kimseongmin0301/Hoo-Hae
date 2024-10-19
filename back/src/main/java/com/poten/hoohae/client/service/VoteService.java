package com.poten.hoohae.client.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.AlramEnum;
import com.poten.hoohae.client.domain.Alram;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.domain.Vote;
import com.poten.hoohae.client.dto.req.VoteRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.repository.AlramRepository;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import com.poten.hoohae.client.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final AlramRepository alramRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Long getVoteCnt(Long id) {

        return voteRepository.countVoteByBoardId(id);
    }

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

                Optional<Board> board = boardRepository.findById(id);
                Alram alram = Alram.builder()
                        .userId(user.getUserId())
                        .body(board.get().getBody())
                        .nickname(user.getNickname())
                        .type("like")
                        .msg(String.valueOf(AlramEnum.LIKE))
                        .commentId(null)
                        .boardId(id)
                        .build();
                alramRepository.save(alram);
            } else {
                voteRepository.deleteById(optionalVote.get().getId());
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
                Optional<Comment> comment = commentRepository.findById(id);

                Alram alram = Alram.builder()
                        .userId(user.getUserId())
                        .body(comment.get().getBody())
                        .nickname(user.getNickname())
                        .type("like")
                        .msg(String.valueOf(AlramEnum.LIKE))
                        .commentId(id)
                        .boardId(comment.get().getBoardId())
                        .build();
                alramRepository.save(alram);
            } else {
                voteRepository.deleteById(optionalVote.get().getId());
            }
            return voteRepository.countVoteByCommentId(id);
        }
    }
}
