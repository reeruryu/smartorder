package com.example.smartorder.dto;

import com.example.smartorder.entity.TransactionConvPay;
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
public class TransactionConvPayDto {

	Long id;
	long amount;

	TransactionType transactionType;
	LocalDateTime transactionDt;

	public static TransactionConvPayDto of(TransactionConvPay transactionConvPay) {
		return TransactionConvPayDto.builder()
			.id(transactionConvPay.getId())
			.amount(transactionConvPay.getAmount())
			.transactionType(transactionConvPay.getTransactionType())
			.transactionDt(transactionConvPay.getRegDt())
			.build();
	}
}
