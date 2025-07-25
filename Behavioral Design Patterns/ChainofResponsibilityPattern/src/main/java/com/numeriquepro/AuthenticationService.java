package com.numeriquepro;

public class AuthenticationService {
    private Handler handler;

    public AuthenticationService(Handler handler) {
        this.handler = handler;
    }

    public boolean login(String username, String password) {
        return handler.handle(username, password);
    }
}