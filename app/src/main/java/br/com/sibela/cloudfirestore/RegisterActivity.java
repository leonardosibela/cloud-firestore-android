package br.com.sibela.cloudfirestore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.sibela.cloudfirestore.presenter.RegisterPresenter;
import br.com.sibela.cloudfirestore.task.RegisterTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterTask.View {

    @BindView(R.id.email_input)
    EditText emailInput;

    @BindView(R.id.password_input)
    EditText passwordInput;

    private RegisterTask.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        getWindow().setNavigationBarColor(getColor(R.color.colorPrimaryVeryDark));
        presenter = new RegisterPresenter(this);
    }

    @OnClick(R.id.register_button)
    void register() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        presenter.registrer(email, password);
    }

    @OnClick({R.id.login_label, R.id.already_member_label})
    void goToLoginScreen() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public void onRegisterSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRegisterFailed() {
        Toast.makeText(this, R.string.error_registering, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayUnfilledInputsMessage() {
        Toast.makeText(this, R.string.error_unfilled_fields, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayInvalidMailMessage() {
        Toast.makeText(this, R.string.error_inform_email, Toast.LENGTH_LONG).show();
    }
}