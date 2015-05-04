package com.alexstyl.touchcontrol.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.BuildConfig;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.entity.AppInfo;
import com.alexstyl.touchcontrol.ui.activity.PhoneticCallingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that translated text to actions.
 * <p>Created by alexstyl on 12/03/15.</p>
 */
final public class SpeechRecognition {


    private static final String TAG = "SpeechRecognition";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static SpeechRecognition sInstance;

    private static final int INDEX_COMMAND = 0;
    private static final int INDEX_DATA_FIRST = 1;
    private static final int INDEX_DATA_SECOND = 2;


    public static class Actions {
        private static final String PHONETIC_CALL = "Call";
        private static final String PHONETIC_OPEN = "Open";
    }


    public static SpeechRecognition getInstance() {
        if (sInstance == null) {
            sInstance = new SpeechRecognition();
        }
        return sInstance;
    }

    /**
     * @param context The context to use
     * @param parts   The parts to recognise
     * @return
     */
    public Intent recognise(Context context, String[] parts) {
        DeLog.d(TAG, "Start recognizing: " + parts.toString());
        if (parts.length <= 1) {
            return null;
        }
        String command = parts[INDEX_COMMAND];
        if (Actions.PHONETIC_CALL.equalsIgnoreCase(command)) {
            // call number or contact

            String number = parts[INDEX_DATA_FIRST];
            boolean isNumber = PhoneNumberUtils.isWellFormedSmsAddress(number);
            if (isNumber) {
                return callNumberIntent(concatArray(parts, ""));
            } else {

                Intent i = new Intent(context, PhoneticCallingActivity.class);
                i.putExtra(PhoneticCallingActivity.EXTRA_NAME, concatArray(parts, " "));
                return i;
                // call person
            }

        } else if (Actions.PHONETIC_OPEN.equalsIgnoreCase(command)) {
            // open app
            String appname = concatArray(parts, " ");
            AppInfo app = getInstalledApp(context, appname);
            if (app != null) {
                return context.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
            } else {
                Toast.makeText(context, context.getString(R.string.no_app_found, appname), Toast.LENGTH_SHORT).show();
            }


        }
        return null;
    }

    private String concatArray(String[] parts, String s) {
        StringBuilder whoToCall = new StringBuilder();
        for (int i = INDEX_DATA_FIRST; i < parts.length; i++) {
            if (whoToCall.length() != 0) {
                whoToCall.append(s);
            }
            whoToCall.append(parts[i]);
        }
        return whoToCall.toString();
    }

    public static AppInfo getInstalledApp(Context context, String appname) {

        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }
            String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            if (appname.equalsIgnoreCase(appName)) {
                AppInfo newInfo = new AppInfo();
                newInfo.setAppName(appName);
                newInfo.setPackageName(p.packageName);
                newInfo.setVersionName(p.versionName);
                newInfo.setVersionCode(p.versionCode);
                newInfo.setIconDrawable(p.applicationInfo.loadIcon(context.getPackageManager()));
                return newInfo;
            }
        }
        return null;
    }

    public static List<AppInfo> getAllInstalledApps(Context context, boolean system) {

        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        List<AppInfo> apps = new ArrayList<>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (!system && (p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }


            String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            AppInfo newInfo = new AppInfo();
            newInfo.setAppName(appName);
            newInfo.setPackageName(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            newInfo.setIconDrawable(p.applicationInfo.loadIcon(context.getPackageManager()));
            apps.add(newInfo);

        }

        return apps;
    }

    public static AppInfo getInstalledPackage(Context context, String appPackage) {
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
//            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                continue;
//            }

            String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            if (p.packageName.equals(appPackage)) {
                AppInfo newInfo = new AppInfo();
                newInfo.setPackageName(p.packageName);
                newInfo.setAppName(appName);
                newInfo.setVersionName(p.versionName);
                newInfo.setVersionCode(p.versionCode);
                newInfo.setIconDrawable(p.applicationInfo.loadIcon(context.getPackageManager()));
                return newInfo;
            }
        }
        return null;
    }


    private Intent callNumberIntent(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        return intent;
    }
}
