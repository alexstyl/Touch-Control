package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.alexstyl.touchcontrol.R;

import java.util.List;

/**
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class VoiceRecognitionAction extends AbstractAction {


    public VoiceRecognitionAction(SpeechRecognizer speed) {
        this.mSpeech = speed;
    }


    /**
     * Returns whether the device is capable of speech recognition
     *
     * @param context The context to use
     */
    public static boolean supportsSpeechRecognition(Context context) {
        List<ResolveInfo> activities =
                context.getPackageManager().queryIntentActivities(
                        new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return !activities.isEmpty();
    }

    private SpeechRecognizer mSpeech;

    @Override
    protected boolean onRunExecuted(Context context) {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(R.string.voice_recognition_title));
//        ((Activity) context).startActivityForResult(intent, OverlayActivity.REQ_VOICE_RECOGNITION);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,context.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        mSpeech.startListening(recognizerIntent);
        return true;
    }

    @Override
    protected String getDataString(Context context) {
        return null;
    }

    @Override
    public String getActionString(Context context) {
        return context.getString(R.string.launches_voice);
    }
}
