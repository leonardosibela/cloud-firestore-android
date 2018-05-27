package br.com.sibela.cloudfirestore.task;

public interface LoginTask {

    interface View {
        void onLoginSuccess();

        void onLoginFailed();

        void displayUnfilledInputsMessage();

        void displayInvalidMailMessage();
    }

    interface Presenter {
        void login(String email, String password);
    }
}