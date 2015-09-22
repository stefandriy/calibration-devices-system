package com.softserve.edu.entity.user;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USER_ROLE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class UserRole {
    @Id
    @GeneratedValue
    private Integer id;
    private String role;

    @ManyToMany
    @JoinTable(name = "USER_USER_ROLE", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "username"))
    private Set<User> users;
}
