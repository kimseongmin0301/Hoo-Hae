package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoardId(@PathVariable(name = "id") Long id) {
        BoardResponseDto dto = new BoardResponseDto();
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<Long> saveBoard(@ModelAttribute BoardRequestDto dto) throws IOException {
        try {
            Long savedBoardId = boardService.saveBoard(dto);
            return ResponseEntity.ok(savedBoardId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable(name = "id") long id){
        BoardResponseDto dto = new BoardResponseDto();
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public Long deleteBoard(@PathVariable(name = "id") long id) {
        return 1L;
    }
}