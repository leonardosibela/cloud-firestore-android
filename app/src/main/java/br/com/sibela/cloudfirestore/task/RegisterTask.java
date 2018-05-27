package br.com.sibela.cloudfirestore.task;

public interface RegisterTask {

    interface View {
        void onRegisterSuccess();

        void onRegisterFailed();

        void displayUnfilledInputsMessage();

        void displayInvalidMailMessage();
    }

    interface Presenter {
        void registrer(String email, String password);
    }
}