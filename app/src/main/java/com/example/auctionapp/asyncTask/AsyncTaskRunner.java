package com.example.auctionapp.asyncTask;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTaskRunner {
    //Handler-ul este obiectul care 'vegheaza' asupra firului principal de executie (activitate/fragment)
    //Pastreaza o referinta catre acesta astfel incat sa putem trimite mesaje din alte thread-uri
    private final Handler handler = new Handler(Looper.getMainLooper());
    //Executor este responsabil cu gestionarea thread-urilor, decide momentul oportun de a porni un thread (apelare metoda start)
    private final Executor executor = Executors.newCachedThreadPool();

    public <R> void executeAsync(Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        try {
            //ii specificam Executor-ului ca dorim sa procesam un Thread de tipul clasei RunnableTask
            executor.execute(new RunnableTask<>(handler, asyncOperation, mainThreadOperation));
        } catch (Exception ex) {
            Log.i("AsyncTaskRunner", "failed call executeAsync " + ex.getMessage());
        }
    }
}
