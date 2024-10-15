package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.dto.req.MyPageRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.service.BoardService;
import com.poten.hoohae.client.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final BoardService boardService;

    @GetMapping()
    public ResponseEntity<MyPageRequestDto> getMyPageData(Authentication authentication) {

        return ResponseEntity.ok(myPageService.getMyPage(authentication.getName()));
    }

    @GetMapping("/board/list")
    public ResponseEntity<PagingDto> getMyPostsList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "category", required = false) String category,
            Authentication authentication) {

        long totalItemCnt = boardService.myTotalBoardCnt(authentication.getName());
        PagingDto pagingDto = PagingDto.builder()
                .hasPage(Paging.hasPage(page, totalItemCnt))
                .data(boardService.getMyBoardList(page, category))
                .build();

        return ResponseEntity.ok(pagingDto);
    }

    @GetMapping("/scrap/list")
    public ResponseEntity<PagingDto> getMyPageScrapList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "category") String category,
            Authentication authentication) {

        long totalItemCnt = boardService.myScrapCnt(authentication.getName());
        PagingDto pagingDto = PagingDto.builder()
                .hasPage(Paging.hasPage(page, totalItemCnt))
                .data(boardService.getMyScrapList(page, category))
                .build();

        return ResponseEntity.ok(pagingDto);
    }
}
