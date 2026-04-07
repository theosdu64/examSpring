package com.example.examspring.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="UserName obligatoire")
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "Email Obligatoire")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NotBlank(message="Mdp obligatoire")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Contact> contacts;
}
