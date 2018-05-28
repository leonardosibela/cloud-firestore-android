package br.com.sibela.cloudfirestore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;

public class QuoteDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = QuoteDialog.class.getSimpleName();
    private Callback callback;

    @BindView(R.id.quote_phrase_input)
    EditText phraseInput;

    @BindView(R.id.quote_author_input)
    EditText authorInput;

    @BindView(R.id.create_button)
    TextView createButton;

    @BindView(R.id.cancel_button)
    TextView cancelButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_quote, container, false);
        phraseInput = view.findViewById(R.id.quote_phrase_input);
        authorInput = view.findViewById(R.id.quote_author_input);
        createButton = view.findViewById(R.id.create_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(this);
        createButton.setOnClickListener(this);

        getDialog().setTitle("New quote");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + Callback.class.getName());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_button: {
                String phrase = phraseInput.getText().toString();
                String author = this.authorInput.getText().toString();

                if (!phrase.equals("")) {
                    callback.saveQuote(author, phrase);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.cancel_button: {
                getDialog().dismiss();
                break;
            }
        }
    }

    public static QuoteDialog getInstance() {
        return new QuoteDialog();
    }

    interface Callback {
        void saveQuote(String author, String phrase);
    }
}