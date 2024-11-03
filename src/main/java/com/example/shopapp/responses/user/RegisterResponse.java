package com.example.shopapp.responses.user;

import com.example.shopapp.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("user")
	private User user;

}
