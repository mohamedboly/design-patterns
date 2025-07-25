package com.numeriquepro;

public class PasswordCheckHandler extends Handler {
    private Database database;

    public PasswordCheckHandler(Database db) {
        this.database = db;
    }

    @Override
    public boolean handle(String username, String password) {
        if (!database.passwordIsValid(username, password)) {
            System.out.println("Mot de passe incorrect");
            return false;
        }
        return next != null && next.handle(username, password);
    }
}
