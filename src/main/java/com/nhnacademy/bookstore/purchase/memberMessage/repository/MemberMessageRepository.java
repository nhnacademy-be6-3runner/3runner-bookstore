package com.nhnacademy.bookstore.purchase.memberMessage.repository;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 맴버메시지 저장소 인터페이스.
 *
 * @author 김병우
 */
public interface MemberMessageRepository extends JpaRepository<MemberMessage, Long> {

    List<MemberMessage> findByMember(Member member);

    @Query("SELECT COUNT(m) FROM MemberMessage m WHERE m.viewAt IS NULL and m.member = :member")
    long countByViewAtIsNull(@Param("member") Member member);
}
