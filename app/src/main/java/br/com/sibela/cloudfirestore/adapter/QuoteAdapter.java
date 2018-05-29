package br.com.sibela.cloudfirestore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.sibela.cloudfirestore.R;
import br.com.sibela.cloudfirestore.model.Quote;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {

    private List<Quote> quotes;
    private Callback callback;

    public QuoteAdapter(List<Quote> quotes, Callback callback) {
        this.quotes = quotes;
        this.callback = callback;
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        final Quote quote = quotes.get(position);
        holder.authorText.setText(quote.getAuthor());
        holder.phraseText.setText(quote.getPhrase());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.askToAlter(quote);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.askToDelete(quote);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return quotes == null ? 0 : quotes.size();
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView phraseText;
        TextView authorText;

        QuoteViewHolder(View itemView) {
            super(itemView);
            phraseText = itemView.findViewById(R.id.phrase_text);
            authorText = itemView.findViewById(R.id.author_text);
        }
    }

    public interface Callback {
        void askToAlter(Quote quote);

        void askToDelete(Quote quote);
    }
}