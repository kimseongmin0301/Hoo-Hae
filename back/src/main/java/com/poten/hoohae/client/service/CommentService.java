package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.common.AlarmEnum;
import com.poten.hoohae.client.common.DateFormat;
import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Alarm;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.domain.Comment;
import com.poten.hoohae.client.domain.File;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final FileRepository fileRepository;
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
                            .commentImage(fileRepository.findByCommentImage(c.getId()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long saveComment(CommentRequestDto dto, String userId) throws IOException {
        Optional<User> userOptional = userRepository.findByEmail(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();

        List<MultipartFile> images = dto.getImage();
        List<Map<String, String>> imageUrls = new ArrayList<>();

        if(images == null) {

        } else if (images.size() > 1) {
            throw new IllegalArgumentException("이미지 수가 1개를 초과합니다.");
        } else {
            imageUrls = s3Service.uploadFiles(images);
        }

        Comment comment = Comment.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .age(user.getAge())
                .boardId(dto.getBoardId())
                .vote(0L)
                .body(dto.getBody())
                .build();

        long id = commentRepository.save(comment).getId();
        Optional<Board> optionalBoard = boardRepository.findById(dto.getBoardId());
        Board board = optionalBoard.get();
        if(!user.getUserId().equals(board.getUserId())) {

            Alarm alarm = Alarm.builder()
                    .userId(board.getUserId())
                    .body(dto.getBody())
                    .nickname(user.getNickname())
                    .type("comment")
                    .msg(String.valueOf(AlarmEnum.COMMENT))
                    .commentId(id)
                    .boardId(dto.getBoardId())
                    .age(user.getAge())
                    .build();
            alarmRepository.save(alarm);
        }

        for (Map<String, String> imageUrl : imageUrls) {
            File file = File.builder()
                    .name(imageUrl.get("name"))
                    .orgName(imageUrl.get("orgName"))
                    .commentId(id)
                    .link(imageUrl.get("link"))
                    .build();

            fileRepository.save(file);
        }

        return id;
    }

    @Transactional
    public Long updateComment(Long id, CommentRequestDto dto, String userId) throws IOException {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        Comment comment = optionalComment.get();

        Optional<User> optionalUser = userRepository.findByEmail(userId);

        if(!comment.getUserId().equals(optionalUser.get().getUserId())) {
            throw new RuntimeException("수정권한없음");
        }

        fileRepository.deleteByCommentId(id);

        List<MultipartFile> images = dto.getImage();
        List<Map<String, String>> imageUrls = new ArrayList<>();

        if(images == null) {

        } else if (images.size() > 1) {
            throw new IllegalArgumentException("이미지 수가 1개를 초과합니다.");
        } else {
            imageUrls = s3Service.uploadFiles(images);
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

        for (Map<String, String> imageUrl : imageUrls) {
            File file = File.builder()
                    .name(imageUrl.get("name"))
                    .orgName(imageUrl.get("orgName"))
                    .boardId(dto.getBoardId())
                    .commentId(id)
                    .link(imageUrl.get("link"))
                    .build();

            fileRepository.save(file);
        }

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
