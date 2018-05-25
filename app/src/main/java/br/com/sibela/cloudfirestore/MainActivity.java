package br.com.sibela.cloudfirestore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.quote_input)
    EditText quoteInput;

    @BindView(R.id.person_name_input)
    EditText personName;

    @BindView(R.id.quote_text)
    TextView quoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.save_button)
    public void save(View view) {

    }
}