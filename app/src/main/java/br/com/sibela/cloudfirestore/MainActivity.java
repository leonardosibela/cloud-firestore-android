package br.com.sibela.cloudfirestore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import br.com.sibela.cloudfirestore.adapter.QuoteAdapter;
import br.com.sibela.cloudfirestore.model.Quote;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements QuoteDialog.Callback, QuoteEditDialog.Callback, QuoteDeleteDialog.Callback, QuoteAdapter.Callback {

    @BindView(R.id.quotes_recycler)
    RecyclerView quotesRecycler;

    private final CollectionReference userQuotesRef;

    public MainActivity() {
        String uid = FirebaseAuth.getInstance().getUid();
        userQuotesRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("quotes");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userQuotesRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                final List<Quote> quotes = new ArrayList<>();
                queryDocumentSnapshots.getQuery().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Quote quote = document.toObject(Quote.class);
                            quotes.add(quote);
                        }
                        updateQuotes(quotes);
                    }
                });
            }
        });
    }

    private void setupRecyclerView() {
        quotesRecycler.setHasFixedSize(true);
        quotesRecycler.setLayoutManager(new LinearLayoutManager(quotesRecycler.getContext()));
        quotesRecycler.addItemDecoration(new SimpleItemDecoration(getBaseContext(),
                getResources().getConfiguration().orientation));
    }

    private void updateQuotes(List<Quote> quotes) {
        QuoteAdapter quoteAdapter = new QuoteAdapter(quotes, this);
        quotesRecycler.setAdapter(quoteAdapter);
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
        DocumentReference newQuoteRef = userQuotesRef.document();

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

    @Override
    public void askToAlter(Quote quote) {
        QuoteEditDialog dialog = QuoteEditDialog.newInstance(quote);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(dialog, QuoteEditDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void alterQuotePhrase(Quote quote, String phrase) {
        DocumentReference quoteRef = userQuotesRef.document(quote.getQuoteId());
        quoteRef.update("phrase", phrase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Citação alterada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao alterar citação!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void askToDelete(Quote quote) {
        QuoteDeleteDialog dialog = QuoteDeleteDialog.newInstance(quote);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(dialog, QuoteDeleteDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void deleteQuote(Quote quote) {
        userQuotesRef.document(quote.getQuoteId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Citação deletada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao deletar citação!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}