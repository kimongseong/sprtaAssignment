package com.sparta.msa_exam.product.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory
    ) {
        /*
        설정 구성을 먼저 진행한다
        redis를 이용해서 spring 내장된 Cache를 사용할때
        관련 설정들을 모아둔 클래스
         */
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // null을 캐싱하는지
                .disableCachingNullValues() //
                // 캐시 유지시간 설정
                .entryTtl(Duration.ofSeconds(60))
                //캐시를 구분하는 방법 (접두사 설정)
                .computePrefixWith(CacheKeyPrefix.simple())
                //벨류를 어떻게 직렬화 / 역직렬화 할건지 설정
                // key 도 설정 할 수 있지만 어차피 문자열로 진행할거기때문에 여기선 설정 안함
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
                );


        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();

    }

}
