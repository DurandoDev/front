package com.medilabosolutions.front.model;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
@Table(name = "role")
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
