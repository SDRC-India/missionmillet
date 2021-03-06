/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.sdrcwassan.missionmillet.collect.android.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.sdrcwassan.missionmillet.collect.android.R;
import org.sdrcwassan.missionmillet.collect.android.application.Collect;
import org.sdrcwassan.missionmillet.collect.android.listeners.FormDownloaderListener;
import org.sdrcwassan.missionmillet.collect.android.listeners.FormListDownloaderListener;
import org.sdrcwassan.missionmillet.collect.android.logic.FormDetails;
import org.sdrcwassan.missionmillet.collect.android.preferences.PreferencesActivity;
import org.sdrcwassan.missionmillet.collect.android.tasks.DownloadFormListTask;
import org.sdrcwassan.missionmillet.collect.android.tasks.DownloadFormsTask;
import org.sdrcwassan.missionmillet.collect.android.utilities.CompatibilityUtils;
import org.sdrcwassan.missionmillet.collect.android.utilities.WebUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Responsible for displaying, adding and deleting all the valid forms in the forms directory. One
 * caveat. If the server requires authentication, a dialog will pop up asking when you request the
 * form list. If somehow you manage to wait long enough and then try to download selected forms and
 * your authorization has timed out, it won't again ask for authentication, it will just throw a
 * 401
 * and you'll have to hit 'refresh' where it will ask for credentials again. Technically a server
 * could point at other servers requiring authentication to download the forms, but the current
 * implementation in Collect doesn't allow for that. Mostly this is just because it's a pain in the
 * butt to keep track of which forms we've downloaded and where we're needing to authenticate. I
 * think we do something similar in the instanceuploader task/activity, so should change the
 * implementation eventually.
 *
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class FormDownloadList extends ListActivity implements FormListDownloaderListener,
        FormDownloaderListener {
    private static final String t = "RemoveFileManageList";

    private static final int PROGRESS_DIALOG = 1;
    private static final int AUTH_DIALOG = 2;
    private static final int MENU_PREFERENCES = Menu.FIRST;

    private static final String BUNDLE_TOGGLED_KEY = "toggled";
    private static final String BUNDLE_SELECTED_COUNT = "selectedcount";
    private static final String BUNDLE_FORM_MAP = "formmap";
    private static final String DIALOG_TITLE = "dialogtitle";
    private static final String DIALOG_MSG = "dialogmsg";
    private static final String DIALOG_SHOWING = "dialogshowing";
    private static final String FORMLIST = "formlist";

    public static final String LIST_URL = "listurl";

    private static final String FORMNAME = "formname";
    private static final String FORMDETAIL_KEY = "formdetailkey";
    private static final String FORMID_DISPLAY = "formiddisplay";

    private String mAlertMsg;
    private boolean mAlertShowing = false;
    private String mAlertTitle;

    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private Button mDownloadButton;

    private DownloadFormListTask mDownloadFormListTask;
    private DownloadFormsTask mDownloadFormsTask;
    private Button mToggleButton;
    private Button mRefreshButton;

    private LinkedHashMap<String, FormDetails> mFormNamesAndURLs = new LinkedHashMap<String,FormDetails>();
    private SimpleAdapter mFormListAdapter;
    private ArrayList<LinkedHashMap<String, String>> mFormList;

    private boolean mToggled = false;
    private int mSelectedCount = 0;

    private static final boolean EXIT = true;
    private static final boolean DO_NOT_EXIT = false;
    private boolean mShouldExit;
    private static final String SHOULD_EXIT = "shouldexit";

    //Ratikanta
    SQLiteDatabase db = null;
    private int numberOfTimeFormDownloaded = 0;
    private int numberOfURLPresent = 0;
    public String url;
    private String username;
    private String password;
    List<String> serverURLs;
    private LinkedHashMap<String, FormDetails> totalResult = new LinkedHashMap<String, FormDetails>();
    private ArrayList<String> xFormIdsTemp = new ArrayList<String>();
    private LinkedHashMap<String, FormDetails> forms;

    //Amit
    String dialogMessage="";
    String dialogTitle;
    int noOfTimeCredentialAdded = 0;



    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_file_manage_list);

        //Ratikanta
        db = openOrCreateDatabase("SDRCCollectDB",MODE_PRIVATE,null);

        Cursor resultSet1 = db.rawQuery("Select * from "+ getString(R.string.xform_table_name),null);
        int rows1 =
                resultSet1.getCount();
        forms = new LinkedHashMap<String, FormDetails>();
        if(rows1 > 0){
            while(resultSet1.moveToNext()){
                String formId = resultSet1.getString(0);
                String formURL = resultSet1.getString(1);
                String formUsername = resultSet1.getString(2);
                String formPassword = resultSet1.getString(3);
                FormDetails fd = new FormDetails(null, formURL, null, formId, null, formUsername, formPassword);

                forms.put(formId, fd);
            }
        }
        //Ratikanta end




        setTitle(getString(R.string.app_name) + " > " + getString(R.string.get_forms));
        mAlertMsg = getString(R.string.please_wait);

        // need white background before load
        getListView().setBackgroundColor(Color.WHITE);

        setServerURLs();

        mDownloadButton = (Button) findViewById(R.id.add_button);
        mDownloadButton.setEnabled(selectedItemCount() > 0);
        mDownloadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is callled in downloadSelectedFiles():
                //    Collect.getInstance().getActivityLogger().logAction(this, "downloadSelectedFiles", ...);
                downloadSelectedFiles();
                mToggled = false;
                clearChoices();
            }
        });

        mToggleButton = (Button) findViewById(R.id.toggle_button);
        mToggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // toggle selections of items to all or none
                ListView ls = getListView();
                mToggled = !mToggled;

                Collect.getInstance().getActivityLogger().logAction(this, "toggleFormCheckbox", Boolean.toString(mToggled));

                for (int pos = 0; pos < ls.getCount(); pos++) {
                    ls.setItemChecked(pos, mToggled);
                }

                mDownloadButton.setEnabled(!(selectedItemCount() == 0));
            }
        });

        mRefreshButton = (Button) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().getActivityLogger().logAction(this, "refreshForms", "");

                mToggled = false;

                //we have to start from 0
                numberOfTimeFormDownloaded = 0;
                dialogMessage="";

                if(numberOfURLPresent > 0)
                    setODKCredentialInSP(0);

                showDialog(PROGRESS_DIALOG);
                downloadFormList();
                FormDownloadList.this.getListView().clearChoices();
                clearChoices();
            }


        });

        if (savedInstanceState != null) {
            // If the screen has rotated, the hashmap with the form ids and urls is passed here.
            if (savedInstanceState.containsKey(BUNDLE_FORM_MAP)) {
                mFormNamesAndURLs =
                        (LinkedHashMap<String, FormDetails>) savedInstanceState
                                .getSerializable(BUNDLE_FORM_MAP);
            }

            // indicating whether or not select-all is on or off.
            if (savedInstanceState.containsKey(BUNDLE_TOGGLED_KEY)) {
                mToggled = savedInstanceState.getBoolean(BUNDLE_TOGGLED_KEY);
            }

            // how many items we've selected
            // Android should keep track of this, but broken on rotate...
            if (savedInstanceState.containsKey(BUNDLE_SELECTED_COUNT)) {
                mSelectedCount = savedInstanceState.getInt(BUNDLE_SELECTED_COUNT);
                mDownloadButton.setEnabled(!(mSelectedCount == 0));
            }

            // to restore alert dialog.
            if (savedInstanceState.containsKey(DIALOG_TITLE)) {
                mAlertTitle = savedInstanceState.getString(DIALOG_TITLE);
            }
            if (savedInstanceState.containsKey(DIALOG_MSG)) {
                mAlertMsg = savedInstanceState.getString(DIALOG_MSG);
            }
            if (savedInstanceState.containsKey(DIALOG_SHOWING)) {
                mAlertShowing = savedInstanceState.getBoolean(DIALOG_SHOWING);
            }
            if (savedInstanceState.containsKey(SHOULD_EXIT)) {
                mShouldExit = savedInstanceState.getBoolean(SHOULD_EXIT);
            }
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(FORMLIST)) {
            mFormList =
                    (ArrayList<LinkedHashMap<String, String>>) savedInstanceState.getSerializable(FORMLIST);
        } else {
            mFormList = new ArrayList<LinkedHashMap<String, String>>();
        }

        if (getLastNonConfigurationInstance() instanceof DownloadFormListTask) {
            mDownloadFormListTask = (DownloadFormListTask) getLastNonConfigurationInstance();
            if (mDownloadFormListTask.getStatus() == AsyncTask.Status.FINISHED) {
                try {
                    dismissDialog(PROGRESS_DIALOG);
                } catch (IllegalArgumentException e) {
                    Log.i(t, "Attempting to close a dialog that was not previously opened");
                }
                mDownloadFormsTask = null;
            }
        } else if (getLastNonConfigurationInstance() instanceof DownloadFormsTask) {
            mDownloadFormsTask = (DownloadFormsTask) getLastNonConfigurationInstance();
            if (mDownloadFormsTask.getStatus() == AsyncTask.Status.FINISHED) {
                try {
                    dismissDialog(PROGRESS_DIALOG);
                } catch (IllegalArgumentException e) {
                    Log.i(t, "Attempting to close a dialog that was not previously opened");
                }
                mDownloadFormsTask = null;
            }
        } else if (getLastNonConfigurationInstance() == null) {


            if(numberOfURLPresent > 0)
                setODKCredentialInSP(0);

            // first time, so get the formlist
            //Ratikanta
            //I kept the progress dialog here cause the dialog will start when the activity
            //starts and will end when all the form list get downloaded
            showDialog(PROGRESS_DIALOG);
            downloadFormList();
        }

        String[] data = new String[] {
                FORMNAME, FORMID_DISPLAY, FORMDETAIL_KEY
        };
        int[] view = new int[] {
                R.id.text1, R.id.text2
        };

        mFormListAdapter =
                new SimpleAdapter(this, mFormList, R.layout.two_item_multiple_choice, data, view);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setItemsCanFocus(false);
        setListAdapter(mFormListAdapter);
    }


    /**
     * Will set DGA Server credentials in the shared preference
     * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
     */
    private void setServerURLs() {
        // TODO Auto-generated method stub
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(Collect.getInstance().getBaseContext());

        String urlString = settings.getString(PreferencesActivity.ODK_SERVER_URLS, "");

        String arr [] = urlString.split(PreferencesActivity.URL_SEPARATOR);

        serverURLs = new ArrayList<String>();

        for(String oneURLAuth : arr)
            serverURLs.add(oneURLAuth);

        numberOfURLPresent = serverURLs.size();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Collect.getInstance().getActivityLogger().logOnStart(this);
    }

    @Override
    protected void onStop() {
        Collect.getInstance().getActivityLogger().logOnStop(this);
        super.onStop();
    }

    private void clearChoices() {
        FormDownloadList.this.getListView().clearChoices();
        mDownloadButton.setEnabled(false);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mDownloadButton.setEnabled(!(selectedItemCount() == 0));

        Object o = getListAdapter().getItem(position);
        @SuppressWarnings("unchecked")
        HashMap<String, String> item = (HashMap<String, String>) o;
        FormDetails detail = mFormNamesAndURLs.get(item.get(FORMDETAIL_KEY));

        Collect.getInstance().getActivityLogger().logAction(this, "onListItemClick", detail.downloadUrl);
    }


    /**
     * Starts the download task and shows the progress dialog.
     */
    private void downloadFormList() {
         ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

        if (ni == null || !ni.isConnected()) {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        } else {

            mFormNamesAndURLs = new LinkedHashMap<String, FormDetails>();
            if (mProgressDialog != null) {
                // This is needed because onPrepareDialog() is broken in 1.6.
                mProgressDialog.setMessage("Server: " + url + "\n" + (numberOfTimeFormDownloaded + 1) + " url searched out of  " + numberOfURLPresent);
            }
//	            showDialog(PROGRESS_DIALOG);

            if (mDownloadFormListTask != null &&
                    mDownloadFormListTask.getStatus() != AsyncTask.Status.FINISHED) {
                return; // we are already doing the download!!!
            } else if (mDownloadFormListTask != null) {
                mDownloadFormListTask.setDownloaderListener(null);
                mDownloadFormListTask.cancel(true);
                mDownloadFormListTask = null;
            }

            mDownloadFormListTask = new DownloadFormListTask();
            mDownloadFormListTask.setDownloaderListener(this);
            mDownloadFormListTask.execute();



        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_TOGGLED_KEY, mToggled);
        outState.putInt(BUNDLE_SELECTED_COUNT, selectedItemCount());
        outState.putSerializable(BUNDLE_FORM_MAP, mFormNamesAndURLs);
        outState.putString(DIALOG_TITLE, mAlertTitle);
        outState.putString(DIALOG_MSG, mAlertMsg);
        outState.putBoolean(DIALOG_SHOWING, mAlertShowing);
        outState.putBoolean(SHOULD_EXIT, mShouldExit);
        outState.putSerializable(FORMLIST, mFormList);
    }


    /**
     * returns the number of items currently selected in the list.
     *
     * @return
     */
    private int selectedItemCount() {
        int count = 0;
        SparseBooleanArray sba = getListView().getCheckedItemPositions();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (sba.get(i, false)) {
                count++;
            }
        }
        return count;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Collect.getInstance().getActivityLogger().logAction(this, "onCreateOptionsMenu", "show");
        super.onCreateOptionsMenu(menu);

        CompatibilityUtils.setShowAsAction(
                menu.add(0, MENU_PREFERENCES, 0, R.string.general_preferences)
                        .setIcon(R.drawable.ic_menu_preferences),
                MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PREFERENCES:
                Collect.getInstance().getActivityLogger().logAction(this, "onMenuItemSelected", "MENU_PREFERENCES");
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESS_DIALOG:
                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.PROGRESS_DIALOG", "show");
                mProgressDialog = new ProgressDialog(this);
                DialogInterface.OnClickListener loadingButtonListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.PROGRESS_DIALOG", "OK");
                                dialog.dismiss();
                                // we use the same progress dialog for both
                                // so whatever isn't null is running
                                if (mDownloadFormListTask != null) {
                                    mDownloadFormListTask.setDownloaderListener(null);
                                    mDownloadFormListTask.cancel(true);
                                    mDownloadFormListTask = null;
                                }
                                if (mDownloadFormsTask != null) {
                                    mDownloadFormsTask.setDownloaderListener(null);
                                    mDownloadFormsTask.cancel(true);
                                    mDownloadFormsTask = null;
                                }
                            }
                        };
                mProgressDialog.setTitle(getString(R.string.downloading_data));
                mProgressDialog.setMessage(mAlertMsg);
                mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setButton(getString(R.string.cancel), loadingButtonListener);
                return mProgressDialog;
            case AUTH_DIALOG:
                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.AUTH_DIALOG", "show");
                AlertDialog.Builder b = new AlertDialog.Builder(this);

                LayoutInflater factory = LayoutInflater.from(this);
                final View dialogView = factory.inflate(R.layout.server_auth_dialog, null);

                // Get the server, username, and password from the settings
                SharedPreferences settings =
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String server =
                        settings.getString(PreferencesActivity.KEY_SERVER_URL,
                                getString(R.string.default_server_url));

                String formListUrl = getString(R.string.default_odk_formlist);
                final String url =
                        server + settings.getString(PreferencesActivity.KEY_FORMLIST_URL, formListUrl);
                Log.i(t, "Trying to get formList from: " + url);

                EditText username = (EditText) dialogView.findViewById(R.id.username_edit);
                String storedUsername = settings.getString(PreferencesActivity.KEY_USERNAME, null);
                username.setText(storedUsername);

                EditText password = (EditText) dialogView.findViewById(R.id.password_edit);
                String storedPassword = settings.getString(PreferencesActivity.KEY_PASSWORD, null);
                password.setText(storedPassword);

                b.setTitle(getString(R.string.server_requires_auth));
                b.setMessage(getString(R.string.server_auth_credentials, url));
                b.setView(dialogView);
                b.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.AUTH_DIALOG", "OK");

                        EditText username = (EditText) dialogView.findViewById(R.id.username_edit);
                        EditText password = (EditText) dialogView.findViewById(R.id.password_edit);

                        Uri u = Uri.parse(url);

                        WebUtils.addCredentials(username.getText().toString(), password.getText()
                                .toString(), u.getHost());
                        downloadFormList();
                    }
                });
                b.setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.AUTH_DIALOG", "Cancel");
                                finish();
                            }
                        });

                b.setCancelable(false);


                mAlertShowing = false;
                return b.create();
        }
        return null;
    }


    /**
     * starts the task to download the selected forms, also shows progress dialog
     */
    @SuppressWarnings("unchecked")
    private void downloadSelectedFiles() {
        int totalCount = 0;
        ArrayList<FormDetails> filesToDownload = new ArrayList<FormDetails>();

        SparseBooleanArray sba = getListView().getCheckedItemPositions();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (sba.get(i, false)) {
                HashMap<String, String> item =
                        (HashMap<String, String>) getListAdapter().getItem(i);
                //Ratikanta
                FormDetails fd = mFormNamesAndURLs.get(item.get(FORMDETAIL_KEY));
                fd.downloadUrl = forms.get(fd.formID).downloadUrl;
                if(!(fd.downloadUrl.charAt(fd.downloadUrl.length()-1) == '/'))
                    fd.downloadUrl += "/";
                fd.downloadUrl += "formXml?formId=" + fd.formID;
                fd.username = forms.get(fd.formID).username;
                fd.password = forms.get(fd.formID).password;

                //Ratikanta end

                filesToDownload.add(fd);
            }
        }
        totalCount = filesToDownload.size();

        Collect.getInstance().getActivityLogger().logAction(this, "downloadSelectedFiles", Integer.toString(totalCount));

        if (totalCount > 0) {
            // show dialog box
            showDialog(PROGRESS_DIALOG);

            mDownloadFormsTask = new DownloadFormsTask(getApplicationContext());
            mDownloadFormsTask.setDownloaderListener(this);
            mDownloadFormsTask.execute(filesToDownload);
        } else {
            Toast.makeText(getApplicationContext(), R.string.noselect_error, Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @Override
    public Object onRetainNonConfigurationInstance() {
        if (mDownloadFormsTask != null) {
            return mDownloadFormsTask;
        } else {
            return mDownloadFormListTask;
        }
    }


    @Override
    protected void onDestroy() {
        if (mDownloadFormListTask != null) {
            mDownloadFormListTask.setDownloaderListener(null);
        }
        if (mDownloadFormsTask != null) {
            mDownloadFormsTask.setDownloaderListener(null);
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        if (mDownloadFormListTask != null) {
            mDownloadFormListTask.setDownloaderListener(this);
        }
        if (mDownloadFormsTask != null) {
            mDownloadFormsTask.setDownloaderListener(this);
        }
        if (mAlertShowing) {
            createAlertDialog(mAlertTitle, mAlertMsg, mShouldExit);
        }
        super.onResume();
    }


    @Override
    protected void onPause() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        super.onPause();
    }


    /**
     * Called when the form list has finished downloading. results will either contain a set of
     * <formname, formdetails> tuples, or one tuple of DL.ERROR.MSG and the associated message.
     *
     * This method will keep the results if there will be multiple DGA Servers, finally after
     * downloading all form lists it will show it on the screen.(Ratikanta)
     *
     * @param result
     * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
     */
    public void formListDownloadingComplete(LinkedHashMap<String, FormDetails> result) {

        mDownloadFormListTask.setDownloaderListener(null);
        mDownloadFormListTask = null;

        if (result == null) {
            Log.e(t, "Formlist Downloading returned null.  That shouldn't happen");
            // Just displayes "error occured" to the user, but this should never happen.
            createAlertDialog(getString(R.string.load_remote_form_error),
                    getString(R.string.error_occured), EXIT);
            return;
        }

        if (result.containsKey(DownloadFormListTask.DL_AUTH_REQUIRED)) {

            // need authorization

            if(noOfTimeCredentialAdded==1){
                numberOfTimeFormDownloaded++;
                dialogMessage += getString(R.string.authentication_faild) + " (" + url+")\n";
                dialogTitle = getString(R.string.load_remote_form_error);

                if (numberOfTimeFormDownloaded < numberOfURLPresent){
                    setODKCredentialInSP(numberOfTimeFormDownloaded);
                    //calling the downloadFormList again to download forms from another server
                    downloadFormList();
                }else{
                    numberOfTimeFormDownloaded--;
                    showFormInUI(result);
                    dismissDialog(PROGRESS_DIALOG);
//            		createAlertDialog(dialogTitle, dialogMessage, DO_NOT_EXIT);
                }

            }else if(noOfTimeCredentialAdded==0){
                Uri u = Uri.parse(url);
                noOfTimeCredentialAdded++;
                WebUtils.addCredentials(username, password, u.getHost());
                downloadFormList();
            }else{
                Log.e(t, "Bad no. of time credential added");
            }


        }
        else if (result.containsKey(DownloadFormListTask.DL_ERROR_MSG)) {
            // Download failed
            //dialogMessage =
            //    getString(R.string.list_failed_with_error,
            //        result.get(DownloadFormListTask.DL_ERROR_MSG).errorStr);

            //Ratikanta
            int errorStatus = 0;

            try{
                errorStatus = Integer.parseInt(dialogMessage);
            }catch(Exception e){
                Log.e(t, "Bad request status while downloading formlist.");
            }

            if(errorStatus == 404){
                dialogMessage += getString(R.string.server_not_found)  + " ( " + url+")\n";
            }else if(errorStatus == 408){
                dialogMessage += getString(R.string.request_timeout)+"\n";
            }else if(errorStatus == 0){
                dialogMessage += getString(R.string.server_down) + " ( " + url+")\n";
            }else{
                dialogMessage += "Error status " + errorStatus + " while downloding the form\n";
            }

            dialogTitle = getString(R.string.load_remote_form_error);
//        	createAlertDialog(dialogTitle, dialogMessage, DO_NOT_EXIT);

            //check if it is final or not
            numberOfTimeFormDownloaded++;

            if (numberOfTimeFormDownloaded < numberOfURLPresent){
                setODKCredentialInSP(numberOfTimeFormDownloaded);
                //calling the downloadFormList again to download forms from another server
                downloadFormList();
            }else{
                numberOfTimeFormDownloaded--;
                showFormInUI(result);
                dismissDialog(PROGRESS_DIALOG);
            }
        }
        else {
            showFormInUI(result);
        }
    }


    private void showFormInUI(LinkedHashMap<String, FormDetails> result) {
        // TODO Auto-generated method stub
        // Everything worked. Clear the list and add the results.

        LinkedHashMap<String, FormDetails> mFormNamesAndURLsTemp = new LinkedHashMap<String,FormDetails>();
        //checking for the last formList download from last URL
        if (numberOfTimeFormDownloaded == (numberOfURLPresent - 1)){

            //before showing form list show the errors during fetching the form list
            createAlertDialog(dialogTitle, dialogMessage, DO_NOT_EXIT);
            if (!(result.containsKey(DownloadFormListTask.DL_ERROR_MSG))) {

                for (String key : result.keySet()) {
                    FormDetails fd = result.get(key);

                    totalResult.put(key, fd);
                }
            }
            result.clear();



            for (String key : totalResult.keySet()) {
                FormDetails fd = totalResult.get(key);

                result.put(key, fd);
            }


            totalResult = new LinkedHashMap<String, FormDetails>();



            //remove forms which are not belong to this user.
            ArrayList<String> xFormIds = new ArrayList<String>();

            String programIds = "";
            Cursor cursor = db.rawQuery("Select id from "+ getString(R.string.program_table_name) + " where selected = 1",null);
            if(cursor.moveToFirst()){
                do{
                    programIds += String.valueOf(cursor.getInt(0)) + ", ";
                }while(cursor.moveToNext());
            }

            if(programIds.length()>0)
                programIds = programIds.substring(0, programIds.length() - 2);

            cursor = db.rawQuery("Select xFormId from "+ getString(R.string.program_xform_mapping_table_name) + " where programId IN (" + programIds + ")",null);

            if(cursor.moveToFirst()){
                do{
                    xFormIds.add(cursor.getString(0));
                }while(cursor.moveToNext());
            }
            xFormIdsTemp.addAll(xFormIds);
            //we need the bellow comments later so keeping it, later we will remove it
            //compare and remove from result
            LinkedHashMap<Integer, String> indexes = new LinkedHashMap<Integer, String>();
            int index = 0;

            for(String xFormId: result.keySet()){
//            	System.out.println(xFormId);
                boolean flag = true;
                for(int i = 0;i < xFormIds.size();i++){
                    if(xFormIds.get(i).trim().equals(xFormId)){
                        flag = false;
                        xFormIds.remove(i);
                        i--;
                        break;

                    }
                }
                if(flag)
                    indexes.put(index, xFormId);
                index++;
            }

            //removing the unassigned form
            for(int j :indexes.keySet())
                result.remove(indexes.get(j));



            for (String xFormId: xFormIdsTemp){
                mFormNamesAndURLsTemp.put(xFormId, result.get(xFormId));
            }
            mFormNamesAndURLs = mFormNamesAndURLsTemp;
            mFormList.clear();
            ArrayList<String> ids = new ArrayList<String>(mFormNamesAndURLs.keySet());
            for (int i = 0; i < mFormNamesAndURLsTemp.size(); i++) {
                String formDetailsKey = ids.get(i);
                FormDetails details = mFormNamesAndURLs.get(formDetailsKey);
                LinkedHashMap<String, String> item = new LinkedHashMap<String, String>();
                item.put(FORMNAME, details.formName);
                item.put(FORMID_DISPLAY,
                        ((details.formVersion == null) ? "" : (getString(R.string.version) + " " + details.formVersion + " ")) +
                                "ID: " + details.formID );
                item.put(FORMDETAIL_KEY, formDetailsKey);
                /* Insert the new form according to the requirement as per th order in which the forms are inserted into the database.
                * Author:@subhadarshani*/
                mFormList.add(item);
                // Insert the new form in alphabetical order.
                /*if (mFormList.size() == 0) {
                    mFormList.add(item);
                } else {
                    int j;
                    for (j = 0; j < mFormList.size(); j++) {
                        LinkedHashMap<String, String> compareMe = mFormList.get(j);
                        String name = compareMe.get(FORMNAME);
                        if (name.compareTo(mFormNamesAndURLs.get(ids.get(i)).formName) > 0) {
                            break;
                        }
                    }
                    mFormList.add(j, item);
                }*/
            }
            mFormListAdapter.notifyDataSetChanged();

            //this is the only point where we are closing the dialog
            dismissDialog(PROGRESS_DIALOG);
        }else{
            numberOfTimeFormDownloaded++;

            for (String key : mFormNamesAndURLsTemp.keySet()) {
                FormDetails fd = mFormNamesAndURLsTemp.get(key);
                totalResult.put(key, fd);
            }

            setODKCredentialInSP(numberOfTimeFormDownloaded);
            //calling the downloadFormList again to download forms from another server
            downloadFormList();
        }

    }


    /**
     * Creates an alert dialog with the given title and message. If shouldExit is set to true, the
     * activity will exit when the user clicks "ok".
     *
     * @param title
     * @param message
     * @param shouldExit
     */
    private void createAlertDialog(String title, String message, final boolean shouldExit) {
        Collect.getInstance().getActivityLogger().logAction(this, "createAlertDialog", "show");
        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(message);
        DialogInterface.OnClickListener quitListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE: // ok
                        Collect.getInstance().getActivityLogger().logAction(this, "createAlertDialog", "OK");
                        //Ratikanta
                        dialogMessage = "";
                        // just close the dialog
                        mAlertShowing = false;
                        // successful download, so quit
                        if (shouldExit) {
//                            finish();
                            //Ratikanta
                            Intent intent = new Intent(getApplicationContext(),
                                    MainMenuActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        };
        mAlertDialog.setCancelable(false);
        mAlertDialog.setButton(getString(R.string.ok), quitListener);
        mAlertDialog.setIcon(android.R.drawable.ic_dialog_info);
        mAlertMsg = message;
        mAlertTitle = title;
        mAlertShowing = true;
        mShouldExit = shouldExit;
        if(!(dialogMessage.equals("")))
            mAlertDialog.show();
    }


    @SuppressLint("StringFormatMatches")
    @Override
    public void progressUpdate(String currentFile, int progress, int total) {
        mAlertMsg = getString(R.string.fetching_file, currentFile, progress, total);
        mProgressDialog.setMessage(mAlertMsg);
    }


    @Override
    public void formsDownloadingComplete(LinkedHashMap<FormDetails, String> result) {
        if (mDownloadFormsTask != null) {
            mDownloadFormsTask.setDownloaderListener(null);
        }

        if (mProgressDialog.isShowing()) {
            // should always be true here
            mProgressDialog.dismiss();
        }

        Set<FormDetails> keys = result.keySet();
        StringBuilder b = new StringBuilder();
        for (FormDetails k : keys) {
            b.append(k.formName +
                    " (" +
                    ((k.formVersion != null) ?
                            (this.getString(R.string.version) + ": " + k.formVersion + " ")
                            : "") +
                    "ID: " + k.formID + ") - " +
                    result.get(k));
            b.append("\n\n");
        }

        //Ratikanta
        dialogMessage = b.toString().trim();

        createAlertDialog(getString(R.string.download_forms_result), b.toString().trim(), EXIT);
    }

    /**
     * This will set the sharedPreference with first DGA credentials
     * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
     */
    private void setODKCredentialInSP(int index) {
        // TODO Auto-generated method stub

        noOfTimeCredentialAdded=0;
        url = serverURLs.get(index).split(PreferencesActivity.KEY_SEPARATOR)[0];
        username = serverURLs.get(index).split(PreferencesActivity.KEY_SEPARATOR)[1];
        password = serverURLs.get(index).split(PreferencesActivity.KEY_SEPARATOR)[2];
        if(url != null)
            url = url.trim();
        if(username != null)
            username = username.trim();
        if(password != null)
            password = password.trim();


        SharedPreferences subsettings = PreferenceManager.getDefaultSharedPreferences(Collect.getInstance().getBaseContext());
        SharedPreferences.Editor editor = subsettings.edit();
        editor.putString(PreferencesActivity.KEY_SERVER_URL, url);
        editor.putString(PreferencesActivity.KEY_USERNAME, username);
        editor.putString(PreferencesActivity.KEY_PASSWORD, password);
        editor.commit();

    }


}