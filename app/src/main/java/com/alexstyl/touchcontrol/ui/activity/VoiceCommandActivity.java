package com.alexstyl.touchcontrol.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.BuildConfig;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.manager.GestureManager;
import com.alexstyl.touchcontrol.manager.SpeechRecognition;
import com.alexstyl.touchcontrol.ui.BaseActivity;

import java.util.ArrayList;

/**
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class VoiceCommandActivity extends BaseActivity {

    private static final String TAG = "VoiceCommand";
    private static final int REQUEST_SPEECH = 12;
    public static final boolean DEBUG = BuildConfig.DEBUG;
    private SpeechRecognizer speech = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speech = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startListeningForCommand();
    }


    void startListeningForCommand() {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_recognition_title));
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        try {
            startActivityForResult(recognizerIntent, REQUEST_SPEECH);
        } catch (ActivityNotFoundException e) {
            DeLog.log(e);
            Toast.makeText(this, R.string.no_app_for_action, Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DeLog.w(TAG, "onActivityResult()!");
        if (requestCode != REQUEST_SPEECH) {
            return;
        }
        if (resultCode == RESULT_OK) {
            onResults(data.getExtras());
        }
        DeLog.d(TAG, "finishing activity");
        finish();
    }


    public void onResults(Bundle results) {
        Log.i(TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        DeLog.d(TAG, "results: " + matches);


        Intent intent = SpeechRecognition.getInstance().recognise(this, matches.get(0).split(" "));
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            GestureManager.getInstance(this).onCommandRecognised(this);
        } else {
            GestureManager.getInstance(this).onCommandUnrecognised(this);
        }

    }


}
