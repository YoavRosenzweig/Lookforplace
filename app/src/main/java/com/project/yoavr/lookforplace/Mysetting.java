package com.project.yoavr.lookforplace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by yoavr on 23/04/2017.
 */

public class Mysetting extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mysetting);

//delete favorite list
        Preference delatefavorite= (Preference) findPreference("delete_favorite");
        delatefavorite.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MysqlHelper helper = new MysqlHelper(Mysetting.this);
                helper.getWritableDatabase().delete("myplace", null, null);
                finish();
                return true;
            }
        });
//show result in km or miles
        Preference distance= (Preference) findPreference("distane");
        distance.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Mysetting.this);
                alertDialogBuilder.setTitle("MILES or KM ");
                alertDialogBuilder
                        .setPositiveButton("MILES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Mysetting.this, "MILES", Toast.LENGTH_SHORT).show();
                                MainActivity.KM = false;
                                finish();


                            }
                        })
                        .setNegativeButton("KM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Mysetting.this, "KM", Toast.LENGTH_SHORT).show();
                                MainActivity.KM = true;
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
        Preference exitPref= (Preference) findPreference("finish");
        exitPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                finish();

                return true;
            }
        });
    }
}

