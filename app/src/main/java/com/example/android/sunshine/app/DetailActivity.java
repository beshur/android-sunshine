package com.example.android.sunshine.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.content.Intent;

import java.util.ArrayList;

public class DetailActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String LOG_TAG = "SunshineDetail";
        private ShareActionProvider mShareActionProvider;
        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        private static final String FORECAST_SHARE_PREFIX = "Forecast for ";

        private static String mShareLocation;
        private static String mForecastStr;

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getActivity().getMenuInflater().inflate(R.menu.detail, menu);

            // Locate MenuItem with ShareActionProvider
            MenuItem item = menu.findItem(R.id.action_share);

            // Fetch and store ShareActionProvider
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            // set share intent
            setShareIntent(createShareIntent(mForecastStr));

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement

            return super.onOptionsItemSelected(item);
        }

        public void onStart() {
            super.onStart();

        }

        // Call to update the share intent
        private void setShareIntent(Intent shareIntent) {
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(shareIntent);
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        public Intent createShareIntent(String text) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String shareTextFinal = "";

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mShareLocation = sharedPref.getString("pref_location_key",
                    getResources().getString(R.string.pref_location_default));

            String[] shareText = {FORECAST_SHARE_PREFIX, mShareLocation, ": ", text, FORECAST_SHARE_HASHTAG};
            for (int i = 0; i < shareText.length; i++) {
                shareTextFinal += shareText[i];
            }
            intent.putExtra(Intent.EXTRA_TEXT, shareTextFinal);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);


            return intent;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // Get the Intent that started this activity and extract the string
            Intent intent = getActivity().getIntent();
            mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);

            // Capture the layout's TextView and set the string as its text
            TextView textView = (TextView) rootView.findViewById(R.id.textView);
            textView.setText(mForecastStr);


            return rootView;
        }
    }
}