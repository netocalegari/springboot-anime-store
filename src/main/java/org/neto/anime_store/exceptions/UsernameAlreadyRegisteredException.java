package org.neto.anime_store.exceptions;

public class UsernameAlreadyRegisteredException extends RuntimeException {
    public UsernameAlreadyRegisteredException(String message) {
        super(message);
    }
}
