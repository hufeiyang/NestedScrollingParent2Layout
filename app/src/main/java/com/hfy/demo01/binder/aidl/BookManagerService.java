package com.hfy.demo01.binder.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hufeiyang
 */
public class BookManagerService extends Service {
    List<Book> mBookList = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.name  = "Android开发艺术探索";
        book.bookId = 100;
        mBookList.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IBookManager.Stub() {
            @Override
            public List<Book> getBookList() throws RemoteException {
                return mBookList;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                mBookList.add(book);
            }
        };
    }
}
