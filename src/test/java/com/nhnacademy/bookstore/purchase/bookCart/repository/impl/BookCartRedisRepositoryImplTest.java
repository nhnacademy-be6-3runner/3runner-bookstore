package com.nhnacademy.bookstore.purchase.bookCart.repository.impl;

import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadBookCartGuestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookCartRedisRepositoryImplTest {
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private BookCartRedisRepositoryImpl bookCartRedisRepository;

    @Test
    void create() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        String hashName = "cart";
        Long id = 1L;
        ReadBookCartGuestResponse response = new ReadBookCartGuestResponse(1L,  1L,11, null,"test", 2);

        bookCartRedisRepository.create(hashName, id, response);

        verify(hashOperations, times(1)).put(hashName + ":", id.toString(), response);
    }

    @Test
    void update() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        String hashName = "cart";
        Long id = 1L;
        ReadBookCartGuestResponse existingResponse = new ReadBookCartGuestResponse(1L,  1L,11, null,"test", 2);

        when(hashOperations.get(hashName+ ":", id.toString())).thenReturn(existingResponse);

        bookCartRedisRepository.update(hashName, id, 3);

        ReadBookCartGuestResponse expectedResponse = new ReadBookCartGuestResponse(1L,  1L,11, null,"test", 3);
        verify(hashOperations, times(1)).put(hashName + ":", id.toString(), expectedResponse);
    }

    @Test
    void delete() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        String hashName = "cart";
        Long id = 1L;

        bookCartRedisRepository.delete(hashName, id);

        verify(hashOperations, times(1)).delete(hashName + ":", id.toString());
    }

    @Test
    void readAllHashName() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        String hashName = "cart";
        ReadBookCartGuestResponse response1 = new ReadBookCartGuestResponse(1L,  1L,11, null,"test", 2);
        ReadBookCartGuestResponse response2 = new ReadBookCartGuestResponse(2L,  1L,11, null,"test", 2);

        when(hashOperations.values(hashName + ":")).thenReturn(List.of(response1, response2));

        List<ReadBookCartGuestResponse> result = bookCartRedisRepository.readAllHashName(hashName);

        assertThat(result).containsExactlyInAnyOrder(response1, response2);
      }

    @Test
    void isHit() {
        String hashName = "cart";

        when(redisTemplate.hasKey(hashName + ":")).thenReturn(true);

        boolean result = bookCartRedisRepository.isHit(hashName);

        assertThat(result).isTrue();
    }

    @Test
    void isMiss() {
        String hashName = "cart";

        when(redisTemplate.hasKey(hashName + ":")).thenReturn(false);

        boolean result = bookCartRedisRepository.isMiss(hashName);

        assertThat(result).isTrue();
    }

    @Test
    void loadData() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        String hashName = "cart";
        ReadBookCartGuestResponse response1 = new ReadBookCartGuestResponse(1L,  1L,11, null,"test", 2);
        ReadBookCartGuestResponse response2 = new ReadBookCartGuestResponse(2L,  1L,11, null,"test", 2);

        bookCartRedisRepository.loadData(List.of(response1, response2), hashName);

        verify(hashOperations, times(1)).put(hashName + ":", response1.bookCartId().toString(), response1);
        verify(hashOperations, times(1)).put(hashName + ":", response2.bookCartId().toString(), response2);
    }

    @Test
    void deleteAll() {
        String hashName = "cart";

        bookCartRedisRepository.deleteAll(hashName);

        verify(redisTemplate, times(1)).delete(hashName + ":");
    }
}