package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name="idcode",length = 30,nullable = false)
    private String idcode;
    @Column(name="username",length = 30,nullable = false)
    private String username;
    @Column(name="email",length = 30,nullable = false)
    private String email;
    @Column(name="password",length = 30,nullable = false)
    private String password;
    @Column(name="rol",length = 30,nullable = false)
    private String rol;
    @Column(name="name",length = 30,nullable = false)
    private String name;

//    public User(UserDto userDto) {
//        this.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
//        this.setIdcode(userDto.getIdcode());
//        this.setUsername(userDto.getUsername());
//        this.setEmail(userDto.getEmail());
//        this.setPassword(userDto.getPassword());
//        this.setRol(userDto.getRol());
//        this.setName(userDto.getName());
//
//    }
//
//    public void updateFromDto(UserDto userDto) {
//        this.setUsername(userDto.getUsername());
//        this.setEmail(userDto.getEmail());
//        this.setPassword(userDto.getPassword());
//        this.setRol(userDto.getRol());
//        this.setName(userDto.getName());
//    }
}


