package com.example.smartorder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateStoreOpenTime {

	@Min(0) @Max(23)
	int startHH;
	@Min(0) @Max(59)
	int startmm;

	@Min(0) @Max(23)
	int endHH;
	@Min(0) @Max(59)
	int endmm;

}
