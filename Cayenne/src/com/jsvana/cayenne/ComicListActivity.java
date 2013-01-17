package com.jsvana.cayenne;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.json.*;

/**
 * An activity representing a list of Comics. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ComicDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ComicListFragment} and the item details (if present) is a
 * {@link ComicDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ComicListFragment.Callbacks} interface to listen for item selections.
 */
public class ComicListActivity extends FragmentActivity implements
		ComicListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	private HashMap<String, String> _comics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comic_list);

		if (findViewById(R.id.comic_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ComicListFragment) getSupportFragmentManager().findFragmentById(
					R.id.comic_list)).setActivateOnItemClick(true);
		}

		final ComicListActivity context = this;
		
		PiperkaQuery q = new PiperkaQuery();
		q.setCallback(new PiperkaQuery.Callback() {
			public void callback(String data) {
				try {
					JSONArray comicsData = new JSONArray(data);
					_comics = new HashMap<String, String>();
					
					for (int i = 0; i < comicsData.length(); i++) {
						JSONArray comic = comicsData.getJSONArray(i);
						_comics.put(comic.getString(0), comic.getString(1));
					}
					context.fetchPreferences();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		q.execute("http://piperka.net/d/comics_ordered.json");
	}

	public void fetchPreferences() {
		PiperkaQuery q = new PiperkaQuery();
		q.setCallback(new PiperkaQuery.Callback() {
			public void callback(String data) {
				try {
					JSONObject json = new JSONObject(data);
					JSONArray subscriptions = (JSONArray)json.get("subscriptions");
					
					for (int i = 0; i < subscriptions.length(); i++) {
						JSONArray subscription = (JSONArray)subscriptions.get(i);
						
						Comic comic = new Comic(subscription.getInt(0), _comics.get(subscription.getString(0)), subscription.getInt(1), subscription.getInt(4));
						
						((ComicListFragment)getSupportFragmentManager().findFragmentById(R.id.comic_list)).addItem(comic);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		q.execute("http://piperka.net/s/uprefs");
	}
	
	/**
	 * Callback method from {@link ComicListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(Comic comic) {
		// In single-pane mode, simply start the detail activity
		// for the selected item ID.
		Intent detailIntent = new Intent(this, ComicDetailActivity.class);
		detailIntent.putExtra(ComicDetailFragment.COMIC, comic);
		startActivity(detailIntent);
	}
}
