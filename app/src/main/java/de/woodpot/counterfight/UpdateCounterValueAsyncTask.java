package de.woodpot.counterfight;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 17.04.2015.
 */
public class UpdateCounterValueAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    private ProgressDialog pDialog;
    private String counterType;
    private String groupIdIntent;
    private String counterValue = "0";

    // Tag-Strings
    private static String TAG_USERNAME = "userName";
    private static final String TAG_SET_COUNTER_TYPE = "counterType";
    private static final String TAG_COUNTER_TYPE_EDIT = "edit";
    private static final String TAG_COUNTER_TYPE_INCREASE = "increase";
    private static final String TAG_COUNTERVALUE = "counterValue";
    private static final String TAG_GROUPID = "groupId";
    private static final String TAG_SUCCESS = "success";

    private SessionManager sm;
    private FragmentSwitcher fragmentSwitcher;
    JSONParser jParser = new JSONParser();

    // Url-Strings
    private static String url_update_counter = "http://www.counterfight.net/update_counter_value.php";

    public UpdateCounterValueAsyncTask(Context context, String counterType, String groupIdIntent,
                                       String counterValue, FragmentSwitcher fragmentSwitcher) {
        this.context = context;
        this.counterType = counterType;
        this.groupIdIntent = groupIdIntent;
        this.counterValue = counterValue;

        Log.d("UpdateCounterValueAsyncTask", "Counter-Type: " + counterType);
        sm = new SessionManager(this.context.getApplicationContext());
        this.fragmentSwitcher = fragmentSwitcher;
        pDialog = new ProgressDialog(this.context);
    };

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... args) {

        //groupId aus AllGroupsActivity übergeben
        String username = null;
        //String groupId = null;

        sm = new SessionManager(context);
        if (sm.isLoggedIn() == true) {
            username = sm.getUsername();
        }

        final List<NameValuePair> increase_params = new ArrayList<NameValuePair>();
        increase_params.add(new BasicNameValuePair(TAG_USERNAME, username));
        increase_params.add(new BasicNameValuePair(TAG_GROUPID, groupIdIntent));
        increase_params.add(new BasicNameValuePair(TAG_SET_COUNTER_TYPE, counterType));
        increase_params.add(new BasicNameValuePair(TAG_COUNTERVALUE, counterValue));
        Log.d("UpdateCounterValueAsyncTask increase_params: ", increase_params.toString());
        JSONObject json4 = null;

        /** Überprüfen, ob Counter erhöht oder neu gesetzt werden soll
         * Wenn neu gesetzt, dann wird zusätzlich der counterValue-Parameter übergeben
         */

        if (counterType.equals(TAG_COUNTER_TYPE_INCREASE)) {
            try {
                json4 = jParser.makeHttpRequest(url_update_counter, "POST", increase_params);
                Log.d("UpdateCounterValueAsyncTask POST: ", increase_params.toString());
            } catch (Exception e) {
                Log.e("UpdateCounterValueAsyncTask", "JSON POST: " + e.getMessage());
            }
        }

        if (counterType.equals(TAG_COUNTER_TYPE_EDIT)) {

            try {
                json4 = jParser.makeHttpRequest(url_update_counter, "POST", increase_params);
                Log.d("GroupDetailActivity POST: ", increase_params.toString());
            } catch (Exception e) {
                Log.e("GroupDetailActivity", "JSON POST: " + e.getMessage());
            }
        }

        try {
            int success = json4.getInt(TAG_SUCCESS);

            if (success == 1) {
                Log.d("GroupDetailActivityFragment JSON: ", "(get) success 1: update");

            } else {
                Log.d("GroupDetailActivityFragment JSON: ", "(get) success 0: kein update");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    protected void onPostExecute(String file_url) {

//        Bundle fragmentData = new Bundle();
//        //groupId = String.format("%05d", groupId);
//
//        fragmentData.putString("groupId", groupIdIntent);
//        fragmentData.putString("groupName", "jap");

        GroupDetailFragment fragment = new GroupDetailFragment();
        fragmentSwitcher.refreshFragment(fragment);
    }


}


