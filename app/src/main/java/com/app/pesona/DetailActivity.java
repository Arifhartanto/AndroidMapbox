package com.app.pesona;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.pesona.List.EXTRA_CREATOR;
import static com.app.pesona.List.EXTRA_ALAMAT;
import static com.app.pesona.List.EXTRA_DEKSKRIPSI;
import static com.app.pesona.List.EXTRA_LATITUDE;
import static com.app.pesona.List.EXTRA_LONGITUDE;
import static com.app.pesona.List.EXTRA_TELPON;
import static com.app.pesona.List.EXTRA_URL;

public class DetailActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener {

    private ImageView peta, rute;
    private ArrayList<ExampleItem> mExampleList;
    private ExampleAdapter mExampleAdapter;
    private RecyclerView mRecyclerView;
    private RequestQueue mRequestQueue;

    public static final String EXTRA_URL2 = "imageUrl";
    public static final String EXTRA_CREATOR2 = "creatorName";
    public static final String EXTRA_ALAMAT2 = "Alamat";
    public static final String EXTRA_DEKSKRIPSI2 = "Deksripsi";
    public static final String EXTRA_LATITUDE2 = "Latitude";
    public static final String EXTRA_LONGITUDE2 = "Longitude";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        String alamat = intent.getStringExtra(EXTRA_ALAMAT);
        String deksripsi = intent.getStringExtra(EXTRA_DEKSKRIPSI);
        String latitude = intent.getStringExtra(EXTRA_LATITUDE);
        String longitude = intent.getStringExtra(EXTRA_LONGITUDE);
        String telpon = intent.getStringExtra(EXTRA_TELPON);

        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        LatLng latLng = new LatLng(lat ,lng);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_view_like_detail);
        TextView textViewDeksripsi = findViewById(R.id.View_deksripsi);
        TextView textViewLatitude = findViewById(R.id.vlatitude);
        TextView textViewLongitude = findViewById(R.id.vlongitude);
        TextView textViewTelpon = findViewById(R.id.vkota);

        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(creatorName);
        textViewLikes.setText(alamat);
        textViewDeksripsi.setText(deksripsi);
        textViewTelpon.setText(telpon);
        textViewLatitude.setText("Latitude    : "+latitude);
        textViewLongitude.setText("Longitude : "+longitude);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DetailActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mExampleAdapter);
        mExampleList = new ArrayList<>();
        mExampleAdapter = new ExampleAdapter(this, mExampleList);

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

        peta =(ImageView) findViewById(R.id.peta);
        peta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map.namawisata = creatorName;
                Map.lat = lat;
                Map.lng = lng;

                Intent intent = new Intent(DetailActivity.this, Map.class);
                startActivity(intent);
            }
        });

        rute =(ImageView) findViewById(R.id.rut);
        rute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RuteWisata.lat = lat;
                RuteWisata.lng = lng;
                RuteWisata.lati = latitude;
                RuteWisata.longi = longitude;
                RuteWisata.latLng = latLng;
                RuteWisata.des = creatorName;
                Intent i = new Intent(DetailActivity.this, RuteWisata.class);
                startActivity(i);
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void parseJSON() {
        Intent intent = getIntent();
        String telp = intent.getStringExtra(EXTRA_TELPON);
        String url = "http://192.168.43.47/Pesona/json/penginapan.php?no_telp="+telp;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String creatorName = hit.getString("nama_penginapan");
                                String imageUrl = hit.getString("gambar_penginapan");
                                String alamat = hit.getString("alamat_penginapan");
                                String deksripsi = hit.getString("deksripsi_penginapan");
                                String latitude = hit.getString("latitude_penginapan");
                                String longitude = hit.getString("longitude_penginapan");
                                String telpon = hit.getString("no_telp");

                                mExampleList.add(new ExampleItem(imageUrl, creatorName, alamat, deksripsi, latitude, longitude, telpon));
                            }

                            mExampleAdapter = new ExampleAdapter(DetailActivity.this, mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
                            mExampleAdapter.setOnItemClickListener(DetailActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("Error!");
                builder.setMessage("Periksa Sambungan Internet Anda");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Coba Lagi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parseJSON();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity2.class);
        ExampleItem clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_URL2, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR2, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_ALAMAT2, clickedItem.getAlamat());
        detailIntent.putExtra(EXTRA_DEKSKRIPSI2, clickedItem.getdeksripsi());
        detailIntent.putExtra(EXTRA_LATITUDE2, clickedItem.getlatitude());
        detailIntent.putExtra(EXTRA_LONGITUDE2, clickedItem.getlongitude());

        startActivity(detailIntent);
    }
}