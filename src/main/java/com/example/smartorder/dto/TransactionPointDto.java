package com.example.smartorder.dto;

import com.example.smartorder.entity.TransactionPoint;
import com.example.smartorder.type.TransactionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransactionPointDto {

	Long id;
	long amount;

	TransactionType transactionType;
	LocalDateTime transactionDt;

	public static TransactionPointDto of(TransactionPoint transactionPoint) {
		return TransactionPointDto.builder()
			.id(transactionPoint.getId())
			.amount(transactionPoint.getAmount())
			.transactionType(transactionPoint.getTransactionType())
			.transactionDt(transactionPoint.getRegDt())
			.build();
	}
}
