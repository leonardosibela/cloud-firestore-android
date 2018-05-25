package br.com.sibela.cloudfirestore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String QUOTE_KEY = "quote";
    private static final String AUTHOR_KEY = "author";
    private DocumentReference docRef = FirebaseFirestore.getInstance().document("sampleData/inpiration");

    @BindView(R.id.quote_input)
    EditText quoteInput;

    @BindView(R.id.author_name_input)
    EditText authorName;

    @BindView(R.id.quote_text)
    TextView quoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    String quote = documentSnapshot.getString(QUOTE_KEY);
                    String author = documentSnapshot.getString(AUTHOR_KEY);
                    quoteText.setText("\"" + quote + "\" -- " + author);
                } else if (e != null) {
                    Toast.makeText(MainActivity.this, "Got an exception!\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @OnClick(R.id.save_button)
    public void save(View view) {
        String quote = quoteInput.getText().toString();
        String authorName = this.authorName.getText().toString();

        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(QUOTE_KEY, quote);
        dataToSave.put(AUTHOR_KEY, authorName);

        docRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Falha ao salvar!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}