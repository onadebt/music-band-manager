package cz.muni.fi.userservice.model;

import cz.muni.fi.shared.enm.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
}
