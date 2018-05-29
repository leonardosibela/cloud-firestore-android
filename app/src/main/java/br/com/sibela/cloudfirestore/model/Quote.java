package br.com.sibela.cloudfirestore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Quote implements Parcelable {

    private String phrase;
    private String author;
    private @ServerTimestamp Date timestamp;
    private String quoteId;
    private String userId;

    public Quote() {
    }

    protected Quote(Parcel in) {
        phrase = in.readString();
        author = in.readString();
        quoteId = in.readString();
        userId = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String noteId) {
        this.quoteId = noteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phrase);
        dest.writeString(author);
        dest.writeString(quoteId);
        dest.writeString(userId);
    }
}
