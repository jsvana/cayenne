package com.jsvana.cayenne;

import org.json.*;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsvana.cayenne.dummy.DummyContent;

/**
 * A fragment representing a single Comic detail screen. This fragment is either
 * contained in a {@link ComicListActivity} in two-pane mode (on tablets) or a
 * {@link ComicDetailActivity} on handsets.
 */
public class ComicDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String COMIC = "comic";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Comic _comic;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ComicDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(COMIC)) {
			_comic = (Comic)getArguments().getSerializable(COMIC);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comic_detail,
				container, false);

		if (_comic != null) {
			((TextView) rootView.findViewById(R.id.comic_detail)).setText(_comic.name);
			((TextView) rootView.findViewById(R.id.comic_homepage)).setMovementMethod(LinkMovementMethod.getInstance());
			((TextView) rootView.findViewById(R.id.comic_most_recent)).setMovementMethod(LinkMovementMethod.getInstance());
			
			PiperkaQuery q = new PiperkaQuery();
			q.setCallback(new PiperkaQuery.Callback() {
				public void callback(String data) {
					try {
						JSONObject json = new JSONObject(data);
						
						JSONArray pages = json.getJSONArray("pages");
						
						_comic.homePage = json.getString("homepage");
						_comic.urlBase = json.getString("url_base");
						_comic.urlTail = json.getString("url_tail");
						_comic.mostRecent = pages.getJSONArray(pages.length() - 1).getString(0);
						
						String homepageLink = "<a href=\"" + _comic.homePage + "\">" + _comic.homePage + "</a>";
						String mostRecentLink = "<a href=\"" + _comic.urlBase + _comic.mostRecent + _comic.urlTail + "\">Most Recent</a>";
						
						((TextView)getView().findViewById(R.id.comic_homepage)).setText(Html.fromHtml(homepageLink));
						((TextView)getView().findViewById(R.id.comic_most_recent)).setText(Html.fromHtml(mostRecentLink));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			q.execute("http://piperka.net/s/archive/" + _comic.id);
		}

		return rootView;
	}
}
