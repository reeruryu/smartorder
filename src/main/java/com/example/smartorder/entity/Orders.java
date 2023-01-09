package com.example.smartorder.entity;

import com.example.smartorder.member.entity.Member;
import com.example.smartorder.type.OrderState;
import com.example.smartorder.type.PayState;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Orders extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Member member;

	@ManyToOne
	private Store store;

	@Type(type = "json")
	@Column(columnDefinition = "json", nullable = false)
	private List<Map<String, Object>> orderMenu;

	private long orderPrice;

	@Enumerated(EnumType.STRING)
	private PayState payState;

	@Enumerated(EnumType.STRING)
	private OrderState orderState;

	private boolean orderCancelYn;

	private String orderCancelReason; // 지울지 고민 중

	private LocalDateTime cancelDt; // 새로운 컬럼 추가






}
