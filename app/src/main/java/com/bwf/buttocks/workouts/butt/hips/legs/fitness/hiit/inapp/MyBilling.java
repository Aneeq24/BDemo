package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.inapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.AppStateManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.helpers.SharedPrefHelper;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AnalyticsManager;

import static com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.inapp.IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED;
import static com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.inapp.IabHelper.BILLING_RESPONSE_RESULT_OK;

public class MyBilling {
    // Debug tag, for logging
    private final String TAG = MyBilling.class.getSimpleName();
    private final String SKU_REMOVE_ADS = "com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.noads";
    // (arbitrary) request code for the purchase flow

    private final int RC_REQUEST = 10111;
    private AppCompatActivity activity;

    // The helper object
    private IabHelper mHelper;

    private Boolean isAdsDisabled = false;
    private String payload = "ANY_PAYLOAD_STRING";
    // Listener that's called when we finish querying the items and
    // subscriptions we own
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null)
                return;

            // Is it a failure?
            if (result.isFailure()) {
                //showMessage("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");
            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase removeAdsPurchase = inventory.getPurchase(SKU_REMOVE_ADS);
            isAdsDisabled = removeAdsPurchase != null;
            removeAds();

            Log.d(TAG, "User has " + (isAdsDisabled ? "REMOVED ADS" : "NOT REMOVED ADS"));
            // setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    // Callback for when a purchase is finished
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null)
                return;

            // Item already owned/purchased
            if (result.mResponse == BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                showMessage("Already purchased");
                SharedPrefHelper.writeBoolean(activity, AppStateManager.IS_ADS_DISABLED, true);
            }
//            // Signature verification failed
//            else if (result.mResponse == IABHELPER_VERIFICATION_FAILED) {
//                showMessage("Signature verification failed");
//            } else if (result.isFailure()) {
//                showMessage("Error purchasing: " + result);
//            }
            else if (result.mResponse == BILLING_RESPONSE_RESULT_OK && purchase.getSku().equals(SKU_REMOVE_ADS)) {
                Log.d(TAG, "Purchase successful.");
                SharedPrefHelper.writeBoolean(activity, AppStateManager.IS_ADS_DISABLED, true);
                Log.d(TAG, "onIabPurchaseFinished: " + SharedPrefHelper.readBoolean(activity, AppStateManager.IS_ADS_DISABLED));
            }
        }
    };

    public MyBilling(AppCompatActivity launcher) {
        this.activity = launcher;
    }

    public void onCreate() {

        // Create the helper, passing it our context and the public key to
        // verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8cnAfwhp8En8FkTZhcCHWRLzROpq8h1hy95aIWoglytqMfPcQoBJkZXPptBw3wCZqxhSfVqeoS2fuid+pVIjEvwR6Q76nZTdl5E/fg1AOKinlHcrXpvDkFwo2ZdnBCOkvE2w77ZdiXn0Y0j/9yp4gbFXu8/IqdrumuBRSHSnQb7wkKYaeThpM+Ulf5oNmuuhC537r+q2SycOBHafM4/LxlD2EAlq6iAxD59neq3IPrBlMcmxQvWCZ3yjrWfQVTYoUt4FAEwhfkrHFz+aBRgQY+KxhghilbYcdZJlRspu5HiE4+AqtdBWeZBCMeIqn2acA35GZQ55ir9GL2ezQwFeMwIDAQAB";
        mHelper = new IabHelper(activity, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set
        // this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(result -> {
            Log.d(TAG, "Setup finished.");

            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                // showMessage("Problem setting up in-app billing: " + result);
                return;
            }

            // Have we been disposed off in the meantime? If so, quit.
            if (mHelper == null)
                return;

            // IAB is fully set up. Now, let's get an inventory of stuff we
            // own.
            Log.d(TAG, "Setup successful. Querying inventory.");
            try {
                mHelper.queryInventoryAsync(mGotInventoryListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        });
    }

    // User clicked the "Remove Ads" button.
    public void purchaseRemoveAds() {
        if (activity != null) {
            activity.runOnUiThread(() -> {
                try {
                    mHelper.launchPurchaseFlow(activity, SKU_REMOVE_ADS, RC_REQUEST, mPurchaseFinishedListener, payload);
                    AnalyticsManager.getInstance().sendAnalytics("purchaseRemoveAds_selected", "purchaseRemoveAds");
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }

            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null)
            return;

        // Pass on the activity result to the helper for handling
        if (mHelper.handleActivityResult(requestCode, resultCode, data))
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            Log.d(TAG, "onActivityResult handled by IABUtil.");
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    private boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct.
         * It will be the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase
         * and verifying it here might seem like a good approach, but this will
         * fail in the case where the user purchases an item on one device and
         * then uses your app on a different device, because on the other device
         * you will not have access to the random string you originally
         * generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different
         * between them, so that one user's purchase can't be replayed to
         * another user.
         *
         * 2. The payload must be such that you can verify it even when the app
         * wasn't the one who initiated the purchase flow (so that items
         * purchased by the user on one device work on other devices owned by
         * the user).
         *
         * Using your own server to store and verify developer payloads across
         * app installations is recommended.
         */
        return true;
    }

    private void removeAds() {
        isAdsDisabled = true;
    }

    // We're being destroyed. It's important to dispose of the helper here!

    public void onDestroy() {

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            mHelper = null;
        }
    }

    private void showMessage(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alertDialog(message);
    }

    private void alertDialog(final String message) {
        activity.runOnUiThread(() -> {

            AlertDialog.Builder bld = new AlertDialog.Builder(activity);
            bld.setMessage(message);
            bld.setNeutralButton("OK", null);
            Log.d(TAG, "Showing alertDialog dialog: " + message);
            bld.create().show();
        });
    }

}