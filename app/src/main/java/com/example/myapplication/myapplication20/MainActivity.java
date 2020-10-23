package com.example.myapplication.myapplication20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar pb = null;
    Button btnAsyncTask = null;
    Button btnHandler = null;
    MonHandler handler = new MonHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = findViewById(R.id.pb_demo);
        btnAsyncTask = findViewById(R.id.btn_avec_asynctask);
        btnHandler = findViewById(R.id.btn_avec_handler);
    }

    public void onClickGo(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<=10; i++){
                    pb.setProgress(i);
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void onClickSansThread(View view) {
        for(int i = 0; i<= 10; i++){
            pb.setProgress(i);
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void onClickAvecThread(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i <= 10; i++){
                    pb.setProgress(i);
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void onClickAvecAsyncTask(View view) {
        new Worker().execute();
    }

    public void onClickAvecHandler(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msgGo = new Message();
                msgGo.what = 1;
                handler.sendMessage(msgGo);
                for (int i = 0; i <= 10; i++) {
                    Message msgEnCours = new Message();
                    msgEnCours.what = 2;
                    msgEnCours.arg1 = i;
                    handler.sendMessage(msgEnCours);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msgEnd = new Message();
                msgEnd.what = 3;
                handler.sendMessage(msgEnd);
            }
        }).start();

    }

    class Worker extends AsyncTask<Void, Integer, String>{

        @Override
        protected String doInBackground(Void... voids) {
            for(int i = 0; i <= 10; i++){
                publishProgress(i);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "fin";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnAsyncTask.setEnabled(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            btnAsyncTask.setEnabled(true);
        }
    }

    class MonHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1: btnHandler.setEnabled(false); break;
                case 2: pb.setProgress(msg.arg1); break;
                case 3: btnHandler.setEnabled(true); break;
            }
        }
    }
}