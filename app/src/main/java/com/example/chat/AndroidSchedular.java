package com.example.chat;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AndroidSchedular extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        startJob(params);
        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void startJob(JobParameters params) {
        Log.d("JOB Schedular","turn data base on ");
         FirebaseDatabase.getInstance().goOnline();
    }

}