package com.example.smartorder.common.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Bean // 캐시 적용시켜 사용하기 위해 생성
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration conf
			= RedisCacheConfiguration.defaultCacheConfig() // 직렬화 해주기
			.serializeKeysWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		return RedisCacheManager.RedisCacheManagerBuilder
					.fromConnectionFactory(redisConnectionFactory) // 커넥션 설정 정보
					.cacheDefaults(conf)
					.build();
	}

	@Bean // 레디스와 연결하기 위한 팩토리 설정
	public RedisConnectionFactory redisConnectionFactory() {
		// 싱글 인스턴스 서버
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
		conf.setHostName(this.host);
		conf.setPort(this.port);
//		conf.setPassword(); // 설정 안함
		return new LettuceConnectionFactory(conf);
	}

}
