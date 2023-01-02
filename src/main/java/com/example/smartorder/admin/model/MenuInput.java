package com.example.smartorder.admin.model;

import com.example.smartorder.category.entity.Category;
import com.example.smartorder.member.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public class MenuInput extends BaseEntity {

	private Long id;

	private long categoryId;

	private String menuName;

	private long menuPrice;
	private int sortValue;

//	private String imagePath;

	private String description;

	private String idList;

}
