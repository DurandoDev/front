package com.medilabosolutions.front.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@Table(name = "role")
//@AllArgsConstructor
//@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	public Role(String roleName) {
		this.name = roleName;
	}

	public Role() {

	}
}
