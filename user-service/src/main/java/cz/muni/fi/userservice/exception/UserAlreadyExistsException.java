package cz.muni.fi.userservice.exception;

import cz.muni.fi.userservice.model.User;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(User user) {
        super("User with username " + user.getUsername() + " already exists");
    }
}
