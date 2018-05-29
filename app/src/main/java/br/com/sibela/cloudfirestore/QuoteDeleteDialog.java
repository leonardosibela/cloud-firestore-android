package br.com.sibela.cloudfirestore;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import br.com.sibela.cloudfirestore.model.Quote;

public class QuoteDeleteDialog extends AppCompatDialogFragment {

    public static final String TAG = QuoteDeleteDialog.class.getSimpleName();
    public static final String QUOTE_DATA = "quote_data";

    private Callback callback;
    private Quote quote;

    public static QuoteDeleteDialog newInstance(Quote quote) {
        QuoteDeleteDialog fragment = new QuoteDeleteDialog();
        Bundle args = new Bundle();
        args.putParcelable(QUOTE_DATA, quote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Deletar citação");
        builder.setMessage("Tem certeza que deseja deletar essa citação?");

        builder.setPositiveButton(R.string.general_label_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.deleteQuote(QuoteDeleteDialog.this.quote);
            }
        });

        builder.setNegativeButton(R.string.general_label_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.deleteQuote(QuoteDeleteDialog.this.quote);
                        dialog.dismiss();
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
        void deleteQuote(Quote quote);
    }
}