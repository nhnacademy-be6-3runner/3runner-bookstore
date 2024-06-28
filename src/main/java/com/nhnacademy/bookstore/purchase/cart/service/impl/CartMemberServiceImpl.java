package com.nhnacademy.bookstore.purchase.cart.service.impl;

import com.nhnacademy.bookstore.entity.cart.Cart;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.purchase.cart.exception.AlreadyExistsCartException;
import com.nhnacademy.bookstore.purchase.cart.exception.NotExistsMemberException;
import com.nhnacademy.bookstore.purchase.cart.repository.CartRepository;
import com.nhnacademy.bookstore.purchase.cart.service.CartMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class CartMemberServiceImpl implements CartMemberService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createCart(long userId){
        if(cartRepository.existsById(userId)){
            throw new AlreadyExistsCartException();
        }
        Cart cart = new Cart(memberRepository.findById(userId).orElseThrow(NotExistsMemberException::new));
        cartRepository.save(cart);
        return cart.getId();
    }

}
