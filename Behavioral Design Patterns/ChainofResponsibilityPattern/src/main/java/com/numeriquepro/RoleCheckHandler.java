package com.numeriquepro;

public class RoleCheckHandler extends Handler {
    @Override
    public boolean handle(String username, String password) {
        if (username.equals("admin")) {
            System.out.println("Admin connecté");
        }
        return next != null && next.handle(username, password);
    }
}
