package com.example.shopapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "social_accounts")
public class SocialAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "provider", length = 20,nullable = false)
	private String provider;
	
	@Column(name = "provider_id", length = 50,nullable = false)
	private String providerId;
	
	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "eamil", length = 150)
	private String email;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
