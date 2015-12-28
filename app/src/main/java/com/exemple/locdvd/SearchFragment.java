package com.exemple.locdvd;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment  extends Fragment {

    EditText searchText;
    Button searchButton;
    ListView searchList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_search, null);

        searchText =(EditText)view.findViewById(R.id.search_queryText);
        searchButton=(Button)view.findViewById(R.id.search_queryLaunch);
        searchList = (ListView)view.findViewById(R.id.search_List);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSearch();
            }
        });
        return view;
    }


    private void launchSearch()  {
        try {
            String title = URLEncoder.encode(searchText.getText().toString(), "UTF-8");
            String url=String.format("http://www.omdbapi.com/?s=%s&type=movie&r=json", title);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,jsonRequestListener ,errorListener);
            getRequestQueue().add(request);
        }
        catch (UnsupportedEncodingException e) {
            Log.e("Search",e.getLocalizedMessage());
        }
    }


    private Response.Listener<JSONObject> jsonRequestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Toast.makeText(getActivity(),"On a une reponse", Toast.LENGTH_LONG).show();
            try {


                
                JSONArray jsonArray = response.getJSONArray("Search");

                ArrayList<OMdbFilm> listOMdbFilm = new ArrayList<OMdbFilm>();

                for (int i =0;i<jsonArray.length();i++) {
                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                    OMdbFilm omdbFilm = new OMdbFilm();
                    omdbFilm.title = jsonObject.getString("Title");
                    omdbFilm.year = jsonObject.getString("Year");
                    omdbFilm.imdbID = jsonObject.getString("imdbID");
                    omdbFilm.type = jsonObject.getString("Type");
                    listOMdbFilm.add(omdbFilm);
                }
                SearchListAdapter searchListAdapter = new SearchListAdapter(getActivity(),listOMdbFilm);
                searchList.setAdapter(searchListAdapter);
            } catch (JSONException e) {
                Log.e("JSON", e.getLocalizedMessage());
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),"On a une erreur", Toast.LENGTH_LONG).show();
            Log.d("REQUEST", error.toString());
        }
    };

    public static class OMdbFilm  {
        public String title ;
        public String year ;
        public String imdbID;
        public String type ;

    }

    RequestQueue requestQueue;
    RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }

    ImageLoader imageLoader;
    ImageLoader getImageLoader()  {
        if(imageLoader==null) {
            ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
                LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }
            };

            imageLoader = new ImageLoader(getRequestQueue(),imageCache);
        }
       return imageLoader;
    }

    class SearchListAdapter extends ArrayAdapter<OMdbFilm> {

        Context context;
        public SearchListAdapter(Context context,  List<OMdbFilm> omdbFilms) {
            super(context, R.layout.listitem_omdbfilm, omdbFilms);
            this.context = context;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View view=null;
            if(convertView==null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.listitem_omdbfilm, null);
            } else {
                view = convertView;
            }

            final OMdbFilm omdbFilm = getItem(pos);

            view.setTag(omdbFilm);

            TextView titre =(TextView)view.findViewById(R.id.listItemOMdbFilm_title);
            TextView annee =(TextView)view.findViewById(R.id.listItemOMdbFilm_year);
            final Button detailButton =(Button)view.findViewById(R.id.listItemOMdbFilm_detail);
            final Button closeButton=(Button)view.findViewById(R.id.listItemOMdbFilm_closeDetail);
            final RelativeLayout detailLayout = (RelativeLayout)view.findViewById(R.id.listItemOMdbFilm_detailLayout);
            final NetworkImageView detailPoster = (NetworkImageView)view.findViewById(R.id.listItemOMdbFilm_poster);
            final TextView detailPlot = (TextView)view.findViewById(R.id.listItemOMdbFilm_plot);

            detailLayout.setVisibility(View.GONE);
            detailButton.setVisibility(View.VISIBLE);

            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailLayout.setVisibility(View.VISIBLE);
                    detailButton.setVisibility(View.GONE);
                    String url = String.format("http://www.omdbapi.com/?i=%s&plot=full&r=json", omdbFilm.imdbID);
                    JsonObjectRequest jsonObjectRequest;
                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String url = response.getString("Poster");
                                        String plot = response.getString("Plot");
                                        detailPlot.setText(plot);
                                        detailPoster.setImageUrl(url, getImageLoader());
                                    } catch (JSONException e) {
                                        Log.e("JSON", e.getLocalizedMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("DETAIL", error.getLocalizedMessage());
                                }
                            });
                    getRequestQueue().add(jsonObjectRequest);
                }
            });
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailLayout.setVisibility(View.GONE);
                    detailButton.setVisibility(View.VISIBLE);
                }
            });
            titre.setText(omdbFilm.title);
            annee.setText(omdbFilm.year);

            return view;
        }

    }

}
