package com.example.demo.dto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {
    private String username;
    private String password;
    private String email;
    private String rol;
    private String name;
}
