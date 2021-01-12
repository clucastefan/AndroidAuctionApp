package com.example.auctionapp.asyncTask;

public interface Callback<R> {
    void runResultOnUiThread(R result);
}
