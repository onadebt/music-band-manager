package cz.muni.fi.userservice.dto;

import cz.muni.fi.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "email is mandatory")
    @Email(message = "email should be valid")
    private String email;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String lastName;

    private Role role;

    private String password;
}
