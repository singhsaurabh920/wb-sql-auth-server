package org.wb.auth.entity;

import javax.persistence.*;

@MappedSuperclass
public class BaseIdEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}