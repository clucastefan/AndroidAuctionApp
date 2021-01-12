package com.example.auctionapp.asyncTask;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.Callable;

public class RunnableTask<R> implements Runnable {
    private final Handler handler;
    //este trimis din firul principal (activitate/fragment)
    private final Callable<R> asyncOperation;
    //este trimis din firul principal (activitate/fragment)
    private final Callback<R> mainThreadOperation;

    //aceasta clasa implementeaza Runnable pentru a o considera un Thread
    //este de tip template pentru ca nu stim tipul rezultatului pe care operatia asincrona il va returna
    public RunnableTask(Handler handler, Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        this.handler = handler;
        this.asyncOperation = asyncOperation;
        this.mainThreadOperation = mainThreadOperation;
    }

    @Override
    public void run() {
        try {
            //apelam declansarea operatiei asincrone, care se realizeaza pe acest thread.
            //in exemplu din seminarul 7 call() realizeaza executia metodei cu acelasi nume din clasa HttpManager
            final R result = asyncOperation.call();
            //rezultatul obtinut mai sus este trimis catre handler prin intermediul unui obiect
            //de tipul clasei HandlerMessage. Scopul acest apel de post este de a notifica Handler-ul
            //ca dorim sa trimitem rezultatul R result in bucata de cod referita de mainThreadOperation (care este implementata in MainActivity)
            handler.post(new HandlerMessage<>(mainThreadOperation, result));
        } catch (Exception e) {
            Log.i("RunnableTask", "failed call runnable " + e.getMessage());
        }
    }
}
