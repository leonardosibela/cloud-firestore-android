package br.com.sibela.cloudfirestore.presenter;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.sibela.cloudfirestore.task.RegisterTask;

public class RegisterPresenter implements RegisterTask.Presenter {

    private RegisterTask.View view;
    private FirebaseAuth firebaseAuth;

    public RegisterPresenter(RegisterTask.View view) {
        this.view = view;
    }

    @Override
    public void registrer(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.displayUnfilledInputsMessage();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.displayInvalidMailMessage();
            return;
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    view.onRegisterSuccess();
                } else {
                    view.onRegisterFailed();
                }
            }
        });
    }
}