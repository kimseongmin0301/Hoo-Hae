package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.dto.req.CommentRequestDto;
import com.poten.hoohae.client.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list/{boardId}")
    public ResponseEntity<PagingDto> getCommentList(@PathVariable(value = "boardId") Long id, @RequestParam(name = "page", defaultValue = "1") int page, Authentication authentication) {
        log.info("comment list");

        long totalItemCnt = commentService.getCommentCnt(id);
        PagingDto pagingDto = PagingDto.builder()
                .hasPage(Paging.hasPage(page, totalItemCnt))
                .data(commentService.getCommentByBoard(id, page, authentication.getName()))
                .build();

        return ResponseEntity.ok(pagingDto);
    }

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<Long> saveComment(@ModelAttribute CommentRequestDto dto, Authentication authentication) throws IOException {
        log.info("comment save");

        Long id = 0L;
        if(dto.getId() == null) {
            id = commentService.saveComment(dto, authentication.getName());
        } else {
            id = commentService.updateComment(id, dto, authentication.getName());
        }

        return ResponseEntity.ok(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateComment(@PathVariable(name = "id") Long id, @RequestBody CommentRequestDto dto, Authentication authentication) throws IOException {
        log.info("update comment");
        Long commentId = commentService.updateComment(id, dto, authentication.getName());

        return ResponseEntity.ok(commentId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteComment(@PathVariable(name = "id") Long id, Authentication authentication) {
        log.info("delete comment");
        Long commentId = commentService.deleteComment(id, authentication.getName());

        return ResponseEntity.ok(commentId);
    }
}
