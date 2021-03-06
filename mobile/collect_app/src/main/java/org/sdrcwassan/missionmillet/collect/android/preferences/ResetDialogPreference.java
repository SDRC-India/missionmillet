/*
 * Copyright 2017 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sdrcwassan.missionmillet.collect.android.preferences;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import org.sdrcwassan.missionmillet.collect.android.R;
import org.sdrcwassan.missionmillet.collect.android.activities.LoginActivity;
import org.sdrcwassan.missionmillet.collect.android.utilities.ResetUtility;
import org.sdrcwassan.missionmillet.collect.android.utilities.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ResetDialogPreference extends DialogPreference {
    private CheckBox preferences;
    private CheckBox instances;
    private CheckBox forms;
    private CheckBox layers;
    private CheckBox cache;
    private CheckBox osmDroid;
    private Context context;
    private ProgressDialog progressDialog;
    private boolean goToLoginPage = false;

    public ResetDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.reset_dialog_layout);
        this.context = context;
    }

    @Override
    public void onBindDialogView(View view) {
        preferences = (CheckBox) view.findViewById(R.id.preferences);
        instances = (CheckBox) view.findViewById(R.id.instances);
        forms = (CheckBox) view.findViewById(R.id.forms);
        layers = (CheckBox) view.findViewById(R.id.layers);
        cache = (CheckBox) view.findViewById(R.id.cache);
        osmDroid = (CheckBox) view.findViewById(R.id.osmdroid);
        super.onBindDialogView(view);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            resetSelected();
        }
    }

    private void resetSelected() {
        final List<Integer> resetActions = new ArrayList<>();

        if (preferences.isChecked()) {
            resetActions.add(ResetUtility.ResetAction.RESET_PREFERENCES);
        }
        if (instances.isChecked()) {
            resetActions.add(ResetUtility.ResetAction.RESET_INSTANCES);
        }
        if (forms.isChecked()) {
            resetActions.add(ResetUtility.ResetAction.RESET_FORMS);
        }
        if (layers.isChecked()) {
            resetActions.add(ResetUtility.ResetAction.RESET_LAYERS);
        }
        if (cache.isChecked()) {
            resetActions.add(ResetUtility.ResetAction.RESET_CACHE);
        }
        if (osmDroid.isChecked()) {
            resetActions.add(ResetUtility.ResetAction.RESET_OSM_DROID);
        }
        if (!resetActions.isEmpty()) {
            showProgressDialog();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    List<Integer> failedResetActions = new ResetUtility().reset(getContext(), resetActions);
                    hideProgressDialog();
                    handleResult(resetActions, failedResetActions);
                }
            };
            new Thread(runnable).start();
        } else {
            ToastUtils.showLongToast(R.string.reset_dialog_nothing);
        }
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getContext(),
                context.getString(R.string.please_wait),
                context.getString(R.string.reset_in_progress),
                true);
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private void handleResult(List<Integer> resetActions, List<Integer> failedResetActions) {
        final StringBuilder resultMessage = new StringBuilder();
        for (int action : resetActions) {
            switch (action) {
                case ResetUtility.ResetAction.RESET_PREFERENCES:
                    if (failedResetActions.contains(action)) {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_settings_result),
                                getContext().getString(R.string.error_occured)));
                    } else {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_settings_result),
                                getContext().getString(R.string.success)));
                        goToLoginPage = true;
                    }
                    break;
                case ResetUtility.ResetAction.RESET_INSTANCES:
                    if (failedResetActions.contains(action)) {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_saved_forms_result),
                                getContext().getString(R.string.error_occured)));
                    } else {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_saved_forms_result),
                                getContext().getString(R.string.success)));
                    }
                    break;
                case ResetUtility.ResetAction.RESET_FORMS:
                    if (failedResetActions.contains(action)) {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_blank_forms_result),
                                getContext().getString(R.string.error_occured)));
                    } else {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_blank_forms_result),
                                getContext().getString(R.string.success)));
                    }
                    break;
                case ResetUtility.ResetAction.RESET_CACHE:
                    if (failedResetActions.contains(action)) {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_cache_result),
                                getContext().getString(R.string.error_occured)));
                    } else {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_cache_result),
                                getContext().getString(R.string.success)));
                    }
                    break;
                case ResetUtility.ResetAction.RESET_LAYERS:
                    if (failedResetActions.contains(action)) {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_layers_result),
                                getContext().getString(R.string.error_occured)));
                    } else {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_layers_result),
                                getContext().getString(R.string.success)));
                    }
                    break;
                case ResetUtility.ResetAction.RESET_OSM_DROID:
                    if (failedResetActions.contains(action)) {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_osm_tiles_result),
                                getContext().getString(R.string.error_occured)));
                    } else {
                        resultMessage.append(String.format(getContext().getString(R.string.reset_osm_tiles_result),
                                getContext().getString(R.string.success)));
                    }
                    break;
            }
            if (resetActions.indexOf(action) < resetActions.size() - 1) {
                resultMessage.append("\n\n");
            }
        }
        showResultDialog(String.valueOf(resultMessage));
    }

    SQLiteDatabase db = null;

    private void showResultDialog(final String resultMessage) {
        ((AdminPreferencesActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle(getContext().getString(R.string.reset_app_state_result));
                b.setMessage(resultMessage);
                b.setCancelable(false);
                b.setIcon(android.R.drawable.ic_dialog_info);
                b.setPositiveButton(getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((AdminPreferencesActivity) context).recreate();
                        if (goToLoginPage) {
                            ((AdminPreferencesActivity) context).finish();
                            db = context.openOrCreateDatabase("SDRCCollectDB", MODE_PRIVATE, null);
                            db.execSQL("UPDATE " + context.getString(R.string.user_table_name) + " set IsLoggedIn = 0;");
                            Intent loginIntent = new Intent(context, LoginActivity.class);
                            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(loginIntent);
                        }
                    }
                });
                AlertDialog alertDialog = b.create();
                alertDialog.show();
            }
        });
    }
}