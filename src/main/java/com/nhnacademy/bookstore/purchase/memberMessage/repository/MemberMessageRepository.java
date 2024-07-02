package com.nhnacademy.bookstore.purchase.memberMessage.repository;

import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 맴버메시지 저장소 인터페이스.
 *
 * @author 김병우
 */
public interface MemberMessageRepository extends JpaRepository<MemberMessage, Long> {
}
