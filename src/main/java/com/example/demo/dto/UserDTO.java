package com.example.demo.dto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String idcode;
    private String username;
    private String email;
//    private String password;
    private String rol;
    private String name;
}
