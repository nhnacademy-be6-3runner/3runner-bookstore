package com.nhnacademy.bookstore.entity.memberMessage;

import com.nhnacademy.bookstore.entity.coupon.Coupon;
import com.nhnacademy.bookstore.entity.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter@Setter
public class MemberMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @NotNull
    private ZonedDateTime sendAt;

    private ZonedDateTime viewAt;

    @ManyToOne
    private Member member;

    @PrePersist
    public void createdAt(){
        sendAt = ZonedDateTime.now();
    }

    public MemberMessage(String message,Member member) {
        this.message = message;
        this.member = member;
    }
}
