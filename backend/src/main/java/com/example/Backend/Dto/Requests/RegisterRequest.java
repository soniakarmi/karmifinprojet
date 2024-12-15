package com.example.Backend.Dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class RegisterRequest {

	private String nom ;
	private String prenom;
	private String password;
	private String email;
	private String telephone;
	private String adresse ;
	private String photo;
	private String role;



}
