package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.Alram;
import com.poten.hoohae.client.dto.res.AlramResponseDto;
import com.poten.hoohae.client.repository.AlramRepository;
import com.poten.hoohae.client.repository.BoardRepository;
import com.poten.hoohae.client.repository.CommentRepository;
import com.poten.hoohae.client.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlramService {
    private final AlramRepository alramRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public List<AlramResponseDto> getAlramList(int page, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        Pageable pageable = PageRequest.of(page - 1, 5);

        Page<Alram> alrams = alramRepository.findAllByUserIdOrderByIdAsc(pageable, user.getUserId());

        List<AlramResponseDto> dto = alrams.getContent().stream()
                .map(a -> AlramResponseDto.builder()
                        .id(a.getId())
                        .nickname(a.getNickname())
                        .body(a.getBody())
                        .msg(a.getMsg())
                        .image(a.getType().equals("adopt") ? imageRepository.findByImage(3L)
                                : a.getType().equals("like") ? imageRepository.findByImage(6L)
                                : imageRepository.findByImage(9L))
                        .isAlive(a.getType().equals("comment") ? (commentRepository.findById(a.getCommentId()).isPresent() && boardRepository.findById(a.getBoardId()).isPresent())
                                : a.getType().equals("like") ? (commentRepository.findById(a.getCommentId()).isPresent() && boardRepository.findById(a.getBoardId()).isPresent())
                                : (commentRepository.findById(a.getCommentId()).isPresent() && boardRepository.findById(a.getBoardId()).isPresent()))
                        .build())
                .collect(Collectors.toList());

        return dto;
    }

    public long getAlramCnt(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        return alramRepository.countByUserId(user.getUserId());
    }

    @Transactional
    public long deleteAlram(Long id) {
        alramRepository.deleteById(id);
        return id;
    }
}
