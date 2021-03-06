package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.helpers.SharedPrefHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

public class TTSManager implements TextToSpeech.OnInitListener {
    /**
     * ID for when no text is spoken
     */
    private static final String UTTERANCE_ID_NONE = "-1";
    private final static String TAG = TTSManager.class.getSimpleName();
    /**
     * Pitch when we have focus
     */
    private static final float FOCUS_PITCH = 1.0f;
    /**
     * Pitch when we should duck audio for another app
     */
    private static final float DUCK_PITCH = 0.5f;
    @SuppressLint("StaticFieldLeak")
    private static TTSManager INSTANCE;
    private final TextToSpeech textToSpeech;
    private final Application application;

    private final Application.ActivityLifecycleCallbacks callbacks;
    private final LinkedHashMap<String, String> samples = new LinkedHashMap<>();
    private final ArrayList<String> unwantedPhrases = new ArrayList<>();
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    textToSpeech.setPitch(FOCUS_PITCH);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    textToSpeech.setPitch(DUCK_PITCH);
                    break;
            }
        }
    };
    private Activity activity = null;
    private boolean initialized = false;
    private boolean muted = false;
    private String playOnInit = null;
    private int queueMode = TextToSpeech.QUEUE_FLUSH;
    private HashMap<String, Runnable> onStartRunnables = new HashMap<>();
    private HashMap<String, Runnable> onDoneRunnables = new HashMap<>();
    private HashMap<String, Runnable> onErrorRunnables = new HashMap<>();

    private TTSManager(final Application application) {
        this.application = application;
        this.textToSpeech = new TextToSpeech(application, this);
        UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                detectAndRun(utteranceId, onStartRunnables);
            }

            @Override
            public void onDone(String utteranceId) {
                if (detectAndRun(utteranceId, onDoneRunnables)) {
                    // because either onDone or onError will be called for an utteranceId, cleanup other
                    if (onErrorRunnables.containsKey(utteranceId)) {
                        onErrorRunnables.remove(utteranceId);
                    }
                }
            }

            @Override
            public void onError(String utteranceId) {
                if (detectAndRun(utteranceId, onErrorRunnables)) {
                    // because either onDone or onError will be called for an utteranceId, cleanup other
                    if (onDoneRunnables.containsKey(utteranceId)) {
                        onDoneRunnables.remove(utteranceId);
                    }
                }
            }
        };
        this.textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
        this.callbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "onActivityCreated: ");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, "onActivityStarted: ");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(TAG, "onActivityResumed: ");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(TAG, "onActivityPaused: ");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG, "onActivityStopped: ");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(TAG, "onActivitySaveInstanceState: ");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "onActivityDestroyed: ");
                if (TTSManager.this.activity == activity) {
                    shutdown();
                }
            }
        };
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    public static TTSManager getInstance(Application application) {
        if (INSTANCE == null)
            INSTANCE = new TTSManager(application);
        return INSTANCE;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            initialized = true;
            if (playOnInit != null) {
                playInternal(playOnInit, UTTERANCE_ID_NONE);
            }
        } else {
            Log.e(TAG, "Initialization failed.");
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        enableVolumeControl(this.activity);
    }

    public Application.ActivityLifecycleCallbacks getCallbacks() {
        return callbacks;
    }

    public void play(CharSequence text) {
        try {
            int i = SharedPrefHelper.readInteger(application.getApplicationContext(),"sound");
            if (i>0)
                return;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        play(text.toString(), null, null, null);
    }

    public void playAndOnStart(String text, Runnable onStart) {
        play(text, onStart, null, null);
    }

    public void playAndOnDone(String text, Runnable onDone) {
        play(text, null, onDone, null);
    }

    public void playAndOnError(String text, Runnable onError) {
        play(text, null, null, onError);
    }

    private void play(String text, Runnable onStart, Runnable onDone, Runnable onError) {



        if (doesNotContainUnwantedPhrase(text)) {
            text = applyRemixes(text);
            if (initialized) {
                String utteranceId = String.valueOf(SystemClock.currentThreadTimeMillis());
                if (onStart != null) {
                    onStartRunnables.put(utteranceId, onStart);
                }
                if (onDone != null) {
                    onDoneRunnables.put(utteranceId, onDone);
                }
                if (onError != null) {
                    onErrorRunnables.put(utteranceId, onError);
                }
                playInternal(text, utteranceId);
            } else {
                playOnInit = text;
            }
        }
    }

    public void stop() {
        textToSpeech.stop();
        Log.d(TAG, "stop: ");
    }

    private String applyRemixes(String text) {
        for (String key : samples.keySet()) {
            if (text.contains(key)) {
                text = text.replace(key, samples.get(key));
            }
        }

        return text;
    }

    private void playInternal(String text, String utteranceId) {
        if (muted) {
            return;
        }
        Log.d(TAG, "Playing: \"" + text + "\"");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, queueMode, null, utteranceId);
        } else {
            final HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
            textToSpeech.speak(text, queueMode, params);
        }
    }

    public void dontPlayIfContains(String text) {
        unwantedPhrases.add(text);
    }

    private boolean doesNotContainUnwantedPhrase(String text) {
        for (String invalid : unwantedPhrases) {
            if (text.contains(invalid)) {
                return false;
            }
        }
        return true;
    }

    public void mute() {
        muted = true;
        if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
    }

    public void unmute() {
        muted = false;
    }

    public boolean isMuted() {
        return muted;
    }

    public void remix(String original, String remix) {
        samples.put(original, remix);
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    public void requestAudioFocus() {
        final AudioManager am = (AudioManager) application.getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
    }

    public void abandonAudioFocus() {
        AudioManager am = (AudioManager) application.getSystemService(Context.AUDIO_SERVICE);
        assert am != null;
        am.abandonAudioFocus(audioFocusChangeListener);
    }

    private void enableVolumeControl(Activity activity) {
        if (activity != null) {
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    public void disableVolumeControl(Activity activity) {
        if (activity != null) {
            activity.setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }
    }

    public void setQueueMode(int queueMode) {
        this.queueMode = queueMode;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Set<Locale> getAvailableLanguages() {
        return textToSpeech.getAvailableLanguages();
    }

    public void setLanguage(Locale locale) {
        textToSpeech.setLanguage(locale);
    }

    private void shutdown() {
        Log.d(TAG, "shutdown: ");
        textToSpeech.shutdown();
        application.unregisterActivityLifecycleCallbacks(callbacks);
    }

    private boolean detectAndRun(String utteranceId, HashMap<String, Runnable> hashMap) {
        if (hashMap.containsKey(utteranceId)) {
            Runnable runnable = hashMap.get(utteranceId);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(runnable);
            hashMap.remove(utteranceId);
            return true;
        } else {
            return false;
        }
    }
}