package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.dto.req.VoteRequestDto;
import com.poten.hoohae.client.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;

    @GetMapping("/get/{boardId}")
    public ResponseEntity<Long> getVoteCnt(@PathVariable(name = "boardId") Long id) {
        Long voteCnt = voteService.getVoteCnt(id);
        return ResponseEntity.ok(voteCnt);
    }

    @PostMapping("/board/{id}")
    public ResponseEntity<Long> updateVote(@PathVariable(name = "id") Long id, Authentication authentication){
        Long voteCnt = voteService.updateVote(id,"board", authentication.getName());
        return ResponseEntity.ok(voteCnt);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<Long> aa(@PathVariable(name = "id") Long id, Authentication authentication) {
        Long voteCnt = voteService.updateVote(id,"comment", authentication.getName());

        return ResponseEntity.ok(voteCnt);
    }
}
