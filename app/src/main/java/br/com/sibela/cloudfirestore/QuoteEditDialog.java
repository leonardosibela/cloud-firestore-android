package br.com.sibela.cloudfirestore;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.sibela.cloudfirestore.model.Quote;

public class QuoteEditDialog extends AppCompatDialogFragment {

    public static final String TAG = QuoteEditDialog.class.getSimpleName();
    public static final String QUOTE_DATA = "quote_data";

    private Callback callback;
    private Quote quote;

    public static QuoteEditDialog newInstance(Quote quote) {
        QuoteEditDialog fragment = new QuoteEditDialog();
        Bundle args = new Bundle();
        args.putParcelable(QUOTE_DATA, quote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.quote_edit_title);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextInputLayout textInputLayout = new TextInputLayout(getContext());
        textInputLayout.setHintTextAppearance(R.style.app_hint_apparance);

        final EditText quotePhraseInput = new EditText(getActivity());
        quotePhraseInput.setHint(R.string.quote_phrase);
        quotePhraseInput.setText(quote.getPhrase());
        quotePhraseInput.setSingleLine();
        quotePhraseInput.setImeOptions(EditorInfo.IME_ACTION_DONE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Math.round(getResources().getDimension(R.dimen.dialog_horizontal_padding)), Math.round(getResources().getDimension(R.dimen.dialog_margin_between_elements)), Math.round(getResources().getDimension(R.dimen.dialog_horizontal_padding)), 0);

        textInputLayout.addView(quotePhraseInput);
        layout.addView(textInputLayout, lp);
        builder.setView(layout);

        builder.setPositiveButton(R.string.general_label_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quotePhrase = quotePhraseInput.getText().toString();
                callback.alterQuotePhrase(QuoteEditDialog.this.quote, quotePhrase);
            }
        });


        builder.setNegativeButton(R.string.general_label_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        quotePhraseInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (quotePhraseInput.getText().toString().isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(getString(R.string.error_field_cant_be_empty_message));
                    } else {
                        String quotePhrase = quotePhraseInput.getText().toString();
                        callback.alterQuotePhrase(QuoteEditDialog.this.quote, quotePhrase);
                        dialog.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quotePhraseInput.getText().toString().isEmpty()) {
                            textInputLayout.setErrorEnabled(true);
                            textInputLayout.setError(getString(R.string.error_field_cant_be_empty_message));
                        } else {
                            String quotePhrase = quotePhraseInput.getText().toString();
                            callback.alterQuotePhrase(QuoteEditDialog.this.quote, quotePhrase);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quote = getArguments().getParcelable(QUOTE_DATA);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callback");
        }
    }

    public interface Callback {
        void alterQuotePhrase(Quote quote, String phrase);
    }
}