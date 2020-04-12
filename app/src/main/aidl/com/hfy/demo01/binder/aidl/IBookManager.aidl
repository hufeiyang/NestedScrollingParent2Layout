// IBookManager.aidl
package com.hfy.demo01.binder.aidl;

import  com.hfy.demo01.binder.aidl.Book;

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);
}
