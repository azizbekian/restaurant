package com.quandoo.azizbekian.misc;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.quandoo.azizbekian.QuandooApplication;
import com.quandoo.azizbekian.data.DataManager;

import javax.inject.Inject;

/**
 * A subclass of {@link JobService} which periodically clears local data.
 * <p>
 * Created on Aug 04, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class DbCleanerJobService extends JobService {

    @Inject
    public DataManager mDataManager;

    @Override public void onCreate() {
        super.onCreate();

        mDataManager = QuandooApplication.getComponent().getDataManager();
    }

    @Override public boolean onStartJob(JobParameters jobParameters) {
        mDataManager.clearLocalData();
        return false;
    }

    @Override public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
