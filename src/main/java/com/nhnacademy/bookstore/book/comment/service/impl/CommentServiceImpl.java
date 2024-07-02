package com.nhnacademy.bookstore.book.comment.service.impl;

import com.nhnacademy.bookstore.book.comment.controller.exception.CommentNotExistsException;
import com.nhnacademy.bookstore.book.comment.controller.exception.UnauthorizedCommentAccessException;
import com.nhnacademy.bookstore.book.comment.dto.request.CreateCommentRequest;
import com.nhnacademy.bookstore.book.comment.dto.response.CommentResponse;
import com.nhnacademy.bookstore.book.comment.repository.CommentRepository;
import com.nhnacademy.bookstore.book.comment.service.CommentService;
import com.nhnacademy.bookstore.book.review.exception.ReviewNotExistsException;
import com.nhnacademy.bookstore.book.review.repository.ReviewRepository;
import com.nhnacademy.bookstore.entity.comment.Comment;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.review.Review;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리뷰 댓글 기능을 위한 서비스 구현체입니다.
 *
 * @author 김은비
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 댓글 생성 메서드입니다.
     *
     * @param reviewId             리뷰 아이디
     * @param memberId             멤버 아이디
     * @param createCommentRequest 생성 요청 dto
     * @return 생성된 댓글 아이디
     */
    @Override
    public Long createComment(long reviewId, long memberId, CreateCommentRequest createCommentRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotExistsException::new);
        Comment comment = Comment.createComment(createCommentRequest.content(), review, member);
        commentRepository.save(comment);
        return comment.getId();
    }

    /**
     * 댓글 수정 메서드입니다.
     *
     * @param commentId            댓글 아이디
     * @param memberId             멤버 아이디
     * @param createCommentRequest 수정 요청 dto
     * @return 수정된 댓글 아이디
     */
    @Override
    public Long updateComment(long commentId, long memberId, CreateCommentRequest createCommentRequest) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotExistsException::new);
        if (comment.getMember().getId() != memberId) {
            throw new UnauthorizedCommentAccessException();
        }
        comment.setContent(createCommentRequest.content());

        return comment.getId();
    }


    @Override
    public void deleteComment(long commentId, long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotExistsException::new);
        if (comment.getMember().getId() != memberId) {
            throw new UnauthorizedCommentAccessException();
        }
        comment.deletedComment();
    }

    /**
     * 리뷰 아이디로 댓글 전체 조회 메서드입니다.
     *
     * @param reviewId 리뷰 아이디
     * @param pageable 페이지 객체
     * @return 댓글 리스트
     */
    @Override
    public Page<CommentResponse> readAllCommentsByReviewId(Long reviewId, Pageable pageable) {
        return commentRepository.readAllCommentsByReviewId(reviewId, pageable);
    }

    /**
     * 사용자 아이디로 댓글 전체 조회 메서드입니다.
     *
     * @param memberId 멤버 아이디
     * @param pageable 페이지 객체
     * @return 댓글 리스트
     */
    @Override
    public Page<CommentResponse> readAllCommentsByMemberId(Long memberId, Pageable pageable) {
        return commentRepository.readAllCommentByMemberId(memberId, pageable);
    }
}
