package itmediaengineering.duksung.ootd.login.presenter;

public enum LoginState {
    login(true),
    logout(false);

    public final Boolean toLogin;

    LoginState(Boolean toLogin) {
        this.toLogin = toLogin;
    }
}
