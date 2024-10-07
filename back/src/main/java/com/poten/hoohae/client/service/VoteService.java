package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.Vote;
import com.poten.hoohae.client.dto.req.VoteRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
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

    public Long getVoteCnt(Long id) {

        return voteRepository.countVoteByBoardId(id);
    }

    public Long updateVote(Long id, VoteRequestDto dto, String userId) {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();

        if(dto.getType()) {
            Vote vote = Vote.builder()
                    .userId(userId)
                    .nickname(user.getNickname())
                    .location(dto.getLocation())
                    .build();
            if (dto.getLocation().equals("board")) {
                vote = vote.toBuilder()
                        .boardId(id)
                        .build();

            } else if (dto.getLocation().equals("comment")) {
                vote = vote.toBuilder()
                        .commentId(id)
                        .build();
            }
            voteRepository.save(vote);
        } else {
            Vote vote = voteRepository.findByBoardIdAndUserId(id, userId);
            voteRepository.deleteById(vote.getId());
        }

        return voteRepository.countVoteByBoardId(id);
    }
}
