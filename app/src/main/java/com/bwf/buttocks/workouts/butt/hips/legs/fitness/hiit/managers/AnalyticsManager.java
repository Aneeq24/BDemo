package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers;

import android.os.Bundle;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.AppStateManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.Application;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsManager {

    private static AnalyticsManager manager;
    private FirebaseAnalytics firebaseAnalytics;

    private AnalyticsManager() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(Application.getContext());
    }

    public static AnalyticsManager getInstance() {
        if (manager == null) {
            manager = new AnalyticsManager();
        }
        return manager;
    }

    public void sendAnalytics(String actionDetail, String actionName) {
        Bundle bundle = new Bundle();
        bundle.putString(AppStateManager.ACTION_TYPE, actionName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT, (actionDetail));
        firebaseAnalytics.logEvent(actionName, bundle);
    }
}
