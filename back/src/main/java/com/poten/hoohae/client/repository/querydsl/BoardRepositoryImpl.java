package com.poten.hoohae.client.repository.querydsl;

import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.dto.res.QBoardResponseDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.poten.hoohae.client.domain.QBoard.board;
import static com.poten.hoohae.client.domain.QComment.comment;
import static com.poten.hoohae.client.domain.QVote.vote;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardResponseDto> findAll(Pageable pageable) {
        List<BoardResponseDto> content = queryFactory
                .select(new QBoardResponseDto(
                        board.id,
                        board.subject,
                        board.body,
                        board.thumbnail,
                        board.userId,
                        board.age,
                        board.category,
                        board.type,
                        board.createdAt,
                        // 댓글 수를 계산
                        JPAExpressions.select(comment.count())
                                .from(comment)
                                .where(comment.boardId.eq(board.id)),
                        // 투표 수를 계산
                        JPAExpressions.select(vote.count())
                                .from(vote)
                                .where(vote.boardId.eq(board.id))
                ))
                .from(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(board.count())
                .from(board)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

//    @Override
//    public Page<BoardResponseDto> findAllByAgeAndCategory(Pageable pageable, long age, String category) {
//        List<BoardResponseDto> content = queryFactory
//                .select(new QBoardResponseDto(
//                        board.id,
//                        board.subject,
//                        board.body,
//                        board.thumbnail,
//                        board.userId,
//                        board.age,
//                        board.category,
//                        board.type,
//                        board.createdAt
//                ))
//                .from(board)
//                .where(board.age.eq(age).and(board.category.eq(category))) // 나이와 카테고리 조건
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = queryFactory
//                .select(board.count())
//                .from(board)
//                .where(board.age.eq(age).and(board.category.eq(category)))
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total);
//    }
}

