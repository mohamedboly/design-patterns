package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        Handler userCheck = new UserExistsHandler(db);
        Handler passCheck = new PasswordCheckHandler(db);
        Handler roleCheck = new RoleCheckHandler();

        userCheck.setNext(passCheck);
        passCheck.setNext(roleCheck);

        AuthenticationService auth = new AuthenticationService(userCheck);
        auth.login("admin", "1234");
    }
}