package com.hfy.demo01.binder.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    public  int bookId;

    public String name;

    public Book() {

    }

    protected Book(Parcel in) {
        bookId = in.readInt();
        name = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(name);
    }
}
