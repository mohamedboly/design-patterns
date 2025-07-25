package com.numeriquepro;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, String> users = new HashMap<>();

    public Database() {
        users.put("admin", "1234");
        users.put("user", "pass");
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public boolean passwordIsValid(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

}
