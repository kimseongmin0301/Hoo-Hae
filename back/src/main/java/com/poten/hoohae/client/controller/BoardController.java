package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<PagingDto> getBoardList(@RequestParam(value = "page", defaultValue = "1") int page, Long age) {
        log.info("/api/board/list");

        long totalItemCnt = boardService.totalBoardCnt(age);
        PagingDto pagingDto = PagingDto.builder()
                .totalItems(totalItemCnt)
                .currentPage(page)
                .data(boardService.getBoardList(page, age))
                .build();

        return ResponseEntity.ok(pagingDto);
    }

    @GetMapping("/category/list")
    public ResponseEntity<PagingDto> getCategoryList(@RequestParam(value ="category", defaultValue = "1") Long category, @RequestParam(value = "page", defaultValue = "1") int page) {
        log.info("category list");
        long totalItemCnt = boardService.countByCategory(category);
        PagingDto pagingDto = PagingDto.builder()
                .totalItems(totalItemCnt)
                .currentPage(page)
                .data(boardService.getBoardCategoryList(page, category))
                .build();

        return ResponseEntity.ok(pagingDto);
    }

    @GetMapping("/top5")
    public ResponseEntity<List<BoardResponseDto>> getTop5List(@RequestParam(value = "age", defaultValue = "0") Long age) {

        return ResponseEntity.ok(boardService.getTop5Boards(age));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoardId(@PathVariable(name = "id") Long id) {
        BoardResponseDto dto = boardService.getBoard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<Long> saveBoard(@ModelAttribute BoardRequestDto dto, Authentication authentication) throws IOException {
        try {
            Long savedBoardId = boardService.saveBoard(dto, authentication.getName());
            return ResponseEntity.ok(savedBoardId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateBoard(@PathVariable(name = "id") long id, @ModelAttribute BoardRequestDto dto, Authentication authentication){
        String userId = authentication.getName();
        Long updateId = boardService.updateBoard(id, dto, userId);
        return ResponseEntity.ok(updateId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteBoard(@PathVariable(name = "id") long id, Authentication authentication) {
        String userId = authentication.getName();
        Long deleteId = boardService.deleteBoard(id, userId);
        return ResponseEntity.ok(deleteId);
    }
}