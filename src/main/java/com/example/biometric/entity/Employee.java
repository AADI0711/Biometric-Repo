package com.example.biometric.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
@Entity
public class Employee {
		
	@Id
	@GeneratedValue(strategy=  GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable= false)
	private String name;
	
	@Column(name="fingerprint_hashcode", nullable = false, unique = true)
    private String fingerprintHash;

}
