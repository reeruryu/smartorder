package com.example.smartorder.entity;

import static com.example.smartorder.common.error.ErrorCode.NOT_ENOUGH_BALANCE;

import com.example.smartorder.common.exception.CustomException;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
@Entity
public class ConvPay extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Member member;

	long balance;

	public static ConvPay createConvPay(Member member, long initialBalance) {
		ConvPay convPay = new ConvPay();
		convPay.setMember(member);
		convPay.setBalance(initialBalance);
		return convPay;
	}

	public void addMoney(long amount) {
		this.balance += amount;
	}

	public void minusMoney(long amount) {

		if (this.balance - amount < 0) {
			throw new CustomException(NOT_ENOUGH_BALANCE);
		}

		this.balance -= amount;

	}
}
