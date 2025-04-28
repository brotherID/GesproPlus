package com.management.pro.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    @Id
    private String idUserAccount;
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @ElementCollection
    @CollectionTable(name = "user_credentials", joinColumns = @JoinColumn(name = "user_id"))
    private List<Credential> credentials = new ArrayList<>();
}
