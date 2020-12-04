package com.udacity.jwdnd.course1.cloudstorage.services.exception;

public class CredentialsExistException extends Throwable {
    public CredentialsExistException(String url, String username) {
        super("Credentials for URL " + url + " and username " + username + " already exist!");
    }
}
