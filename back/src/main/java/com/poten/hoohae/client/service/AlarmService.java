package com.poten.hoohae.client.service;

import com.poten.hoohae.auth.domain.User;
import com.poten.hoohae.auth.repository.UserRepository;
import com.poten.hoohae.client.domain.Alarm;
import com.poten.hoohae.client.domain.Image;
import com.poten.hoohae.client.dto.res.AlarmResponseDto;
import com.poten.hoohae.client.repository.AlarmRepository;
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
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public List<AlarmResponseDto> getAlarmList(int page, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Alarm> alarms = alarmRepository.findAllByUserIdOrderByIdAsc(pageable, user.getUserId());

        return alarms.getContent().stream()
                .map(a -> {
                    Long commentId = a.getCommentId();
                    Long boardId = a.getBoardId();
                    boolean commentExists = true;
                    boolean boardExists = true;
                    if (commentId != null) {
                        commentExists = commentRepository.findById(commentId).isPresent();
                    }
                    if(boardId != null) {
                        boardExists = boardRepository.findById(boardId).isPresent();
                    }

                    return AlarmResponseDto.builder()
                            .id(a.getId())
                            .nickname(a.getNickname())
                            .body(a.getBody())
                            .msg(a.getMsg())
                            .image(getImageByType(a.getType()))
                            .isAlive(commentExists && boardExists)
                            .age(a.getAge())
                            .page(getCommentPageNumber(boardId, commentId))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String getImageByType(String type) {
        switch (type) {
            case "adopt":
                return imageRepository.findByImage(3L);
            case "like":
                return imageRepository.findByImage(6L);
            default:
                return imageRepository.findByImage(9L);
        }
    }

    public int getCommentPageNumber(Long boardId, Long commentId) {
        int commentsPerPage = 5;

        // 댓글의 총 개수를 가져오는 로직 (예: 댓글 수 조회)
        long totalComments = commentRepository.countCommentByBoardId(boardId);

        // 해당 댓글이 전체 댓글 중 몇 번째에 있는지 인덱스 조회 (예: 인덱스 조회)
        long commentIndex = commentRepository.findIndexByBoardIdAndCommentId(boardId, commentId);

        // 페이지 번호 계산
        return (int) (commentIndex / commentsPerPage) + 1;
    }

    public long getAlarmCnt(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();

        return alarmRepository.countByUserId(user.getUserId());
    }

    @Transactional
    public long deleteAlarm(Long id) {
        alarmRepository.deleteById(id);
        return id;
    }
}
