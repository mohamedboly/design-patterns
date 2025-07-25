package com.numeriquepro;

public class UserExistsHandler extends Handler {
    private Database database;

    public UserExistsHandler(Database db) {
        this.database = db;
    }

    @Override
    public boolean handle(String username, String password) {
        if (!database.userExists(username)) {
            System.out.println("Utilisateur non trouvé");
            return false;
        }
        return next != null && next.handle(username, password);
    }
}
