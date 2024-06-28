package com.nhnacademy.bookstore.purchase.bookCart.repository.impl;

import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadBookCartGuestResponse;
import com.nhnacademy.bookstore.purchase.bookCart.repository.BookCartRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 도서장바구니 레디스 저장소 구현체.
 *
 * @author 김병우
 */
@Repository
@RequiredArgsConstructor
public class BookCartRedisRepositoryImpl implements BookCartRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 도서장바구니 레디스 저장.
     *
     * @param hashName 해쉬
     * @param id 해쉬키
     * @param readBookCartGuestResponse 해쉬밸류
     * @return 해쉬키
     */
    @Override
    public Long create(String hashName, Long id, ReadBookCartGuestResponse readBookCartGuestResponse) {
        redisTemplate.opsForHash().put(hashName + ":", id.toString(), readBookCartGuestResponse);
        redisTemplate.expire(hashName + ":", 1, TimeUnit.HOURS);
        return id;
    }

    /**
     * 도서장바구니 레디스 수정.
     *
     * @param hashName 해쉬
     * @param id 해쉬키
     * @param quantity 수정데이터
     * @return 해쉬키
     */
    @Override
    public Long update(String hashName, Long id, int quantity) {
        ReadBookCartGuestResponse response = (ReadBookCartGuestResponse)redisTemplate.opsForHash().get(hashName + ":", id.toString());

        ReadBookCartGuestResponse updatedResponse = ReadBookCartGuestResponse.builder()
                .bookCartId(response.bookCartId())
                .bookId(response.bookId())
                .price(response.price())
                .url(response.url())
                .title(response.title())
                .quantity(quantity)
                .build();

        redisTemplate.opsForHash().put(hashName + ":", id.toString(), updatedResponse);
        redisTemplate.expire(hashName + ":", 1, TimeUnit.HOURS);
        return id;
    }

    /**
     * 도서장바구니 레디스 삭제.
     *
     * @param hashName 해쉬
     * @param id 해쉬키
     * @return 해쉬키
     */
    @Override
    public Long delete(String hashName, Long id) {
        redisTemplate.opsForHash().delete(hashName + ":", id.toString());
        return id;
    }

    /**
     * 도서 장바구니 레디스 해쉬 전체 삭제.
     *
     * @param hashName 해쉬
     */
    @Override
    public void deleteAll(String hashName) {
        redisTemplate.delete(hashName + ":");
    }

    /**
     * 도서장바구니 레디스 목록 읽기.
     *
     * @param hashName 해쉬
     * @return 도서장바구니 응답 dto
     */
    @Override
    public List<ReadBookCartGuestResponse> readAllHashName(String hashName) {
        List<Object> bookCartList = redisTemplate.opsForHash().values(hashName + ":");

        return  bookCartList
                .stream()
                .map(ReadBookCartGuestResponse.class::cast)
                .toList();
    }

    /**
     * 도서장바구니 레디스 데이터가 있는지 확인 메소드.
     *
     * @param hashName 해쉬
     * @return boolean
     */
    @Override
    public boolean isHit(String hashName) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(hashName + ":"));
    }

    /**
     * 도서장바구니 레디스 데이터가 없는지 확인 메소드.
     *
     * @param hashName 해쉬
     * @return boolean
     */
    @Override
    public boolean isMiss(String hashName) {
        return Boolean.FALSE.equals(redisTemplate.hasKey(hashName + ":"));
    }

    /**
     * 도서장바구니 데이터를 레디스에 저장.
     *
     * @param bookCartGuestResponses 도서장바구니 dto
     * @param hashName 해쉬
     */
    @Override
    public void loadData(List<ReadBookCartGuestResponse> bookCartGuestResponses, String hashName) {
        for (ReadBookCartGuestResponse o : bookCartGuestResponses) {
            if (Objects.nonNull(o)) {
                redisTemplate.opsForHash().put(hashName + ":", o.bookCartId().toString(), o);
            }
        }
        redisTemplate.expire(hashName + ":", 1, TimeUnit.HOURS);
    }
}
