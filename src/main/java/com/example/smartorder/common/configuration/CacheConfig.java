package com.example.smartorder.common.configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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

		return RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(redisConnectionFactory) // 커넥션 설정 정보
			.cacheDefaults(defaultConf())
			.withInitialCacheConfigurations(confMap())
			.build();
	}

	@Bean // 레디스와 연결하기 위한 팩토리 설정
	public RedisConnectionFactory redisConnectionFactory() {
		// 싱글 인스턴스 서버
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
		conf.setHostName(this.host);
		conf.setPort(this.port);

		return new LettuceConnectionFactory(conf);
	}

	private RedisCacheConfiguration defaultConf() {
		RedisCacheConfiguration conf
			= RedisCacheConfiguration.defaultCacheConfig() // 직렬화 해주기
			.serializeKeysWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer()))
			.entryTtl(Duration.ofMinutes(1)); // withInitialCacheConfigurations 에서 관리되지 않는 cacheName 이라면 모두 cacheDefaults 설정대로 동작
		return conf;
	}

	private Map<String, RedisCacheConfiguration> confMap() {
		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
		cacheConfigurations.put("location", defaultConf().entryTtl(Duration.ofMinutes(30L)));
		return cacheConfigurations;
	}

}
