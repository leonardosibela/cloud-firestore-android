package br.com.sibela.cloudfirestore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import br.com.sibela.cloudfirestore.presenter.LoginPresenter;
import br.com.sibela.cloudfirestore.task.LoginTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginTask.View {

    @BindView(R.id.email_input)
    EditText emailInput;

    @BindView(R.id.password_input)
    EditText passwordInput;

    private LoginTask.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getWindow().setNavigationBarColor(getColor(R.color.colorPrimaryVeryDark));
        presenter = new LoginPresenter(this);
    }

    @OnClick(R.id.login_button)
    void login() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        presenter.login(email, password);
    }

    @OnClick({R.id.register_label, R.id.no_account_yet_label})
    void goToRegisterScreen() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onLoginFailed() {
        Toast.makeText(this, R.string.error_login_in, Toast.LENGTH_LONG).show();
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