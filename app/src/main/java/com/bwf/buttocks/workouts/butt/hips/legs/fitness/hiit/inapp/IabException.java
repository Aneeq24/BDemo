package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.inapp;

public class IabException extends Exception {
    private IabResult mResult;

    private IabException(IabResult r) {
        this(r, null);
    }

    IabException(int response, String message) {
        this(new IabResult(response, message));
    }

    private IabException(IabResult r, Exception cause) {
        super(r.getMessage(), cause);
        mResult = r;
    }

    IabException(int response, String message, Exception cause) {
        this(new IabResult(response, message), cause);
    }

    /**
     * Returns the IAB result (error) that this exception signals.
     */
    public IabResult getResult() {
        return mResult;
    }
}