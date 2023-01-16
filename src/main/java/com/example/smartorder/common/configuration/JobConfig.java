package com.example.smartorder.common.configuration;

import static com.example.smartorder.type.SaleState.ON_SALE;
import static com.example.smartorder.type.SaleState.SOLDOUT_FOR_ONE_DAY;

import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.repository.StoreMenuRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final StoreMenuRepository storeMenuRepository;

	@Bean
	public Job soldOutForOneDayChangeJob() {
		return jobBuilderFactory.get("soldOutForOneDayChangeJob")
			.start(soldOutForOneDayChangeJobStep())
			.build();
	}

	@Bean
	@JobScope
	public Step soldOutForOneDayChangeJobStep() {
		return stepBuilderFactory.get("soldOutForOneDayChangeJobStep")
			.<StoreMenu, StoreMenu>chunk(10)
			.reader(soldOutForOneDayReader())
			.processor(soldOutForOneDayProcessor())
			.writer(soldOutForOneDayWriter())
			.build();
	}

	@Bean
	@StepScope
	public RepositoryItemReader<StoreMenu> soldOutForOneDayReader() {
		LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
		return new RepositoryItemReaderBuilder<StoreMenu>()
			.repository(storeMenuRepository)
			.methodName("findAllBySaleStateAndSoldOutDtBetween")
			.arguments(SOLDOUT_FOR_ONE_DAY, oneDayAgo, oneDayAgo.minusMinutes(30))
			.pageSize(10)
			.sorts(Collections.singletonMap("soldOutDt", Direction.ASC))
			.name("soldOutForOneDayReader")
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<StoreMenu, StoreMenu> soldOutForOneDayProcessor() {
		return storeMenu -> {
			storeMenu.setSaleState(ON_SALE);
			return storeMenu;
		};
	}

	@Bean
	@StepScope
	public RepositoryItemWriter<StoreMenu> soldOutForOneDayWriter() {
		return new RepositoryItemWriterBuilder<StoreMenu>()
			.repository(storeMenuRepository)
			.build();
	}
}
