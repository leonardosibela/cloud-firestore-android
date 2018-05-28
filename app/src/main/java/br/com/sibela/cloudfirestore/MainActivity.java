package br.com.sibela.cloudfirestore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.sibela.cloudfirestore.model.Quote;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements QuoteDialog.Callback {

    private final DocumentReference userRef;

    public MainActivity() {
        String uid = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseFirestore.getInstance().collection("users").document(uid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.add_quote_fab)
    public void save(View view) {
        QuoteDialog quoteDialog = QuoteDialog.getInstance();
        quoteDialog.show(getSupportFragmentManager(), QuoteDialog.TAG);
    }

    @Override
    public void saveQuote(String author, String phrase) {
        DocumentReference newQuoteRef = userRef.collection("quotes").document();

        Quote quote = new Quote();
        quote.setAuthor(author);
        quote.setPhrase(phrase);
        quote.setQuoteId(newQuoteRef.getId());
        quote.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        newQuoteRef.set(quote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}