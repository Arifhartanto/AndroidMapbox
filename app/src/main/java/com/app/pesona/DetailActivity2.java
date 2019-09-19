package com.app.pesona;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.app.pesona.DetailActivity.EXTRA_CREATOR2;
import static com.app.pesona.DetailActivity.EXTRA_ALAMAT2;
import static com.app.pesona.DetailActivity.EXTRA_DEKSKRIPSI2;
import static com.app.pesona.DetailActivity.EXTRA_LATITUDE2;
import static com.app.pesona.DetailActivity.EXTRA_LONGITUDE2;
import static com.app.pesona.DetailActivity.EXTRA_URL2;

public class DetailActivity2 extends AppCompatActivity {

    private ImageView peta, rute;
    private ArrayList<ExampleItem> mExampleList;
    private ExampleAdapter mExampleAdapter;
    private RecyclerView mRecyclerView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL2);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR2);
        String alamat = intent.getStringExtra(EXTRA_ALAMAT2);
        String deksripsi = intent.getStringExtra(EXTRA_DEKSKRIPSI2);
        String latitude = intent.getStringExtra(EXTRA_LATITUDE2);
        String longitude = intent.getStringExtra(EXTRA_LONGITUDE2);

        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        LatLng latLng = new LatLng(lat ,lng);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_view_like_detail);
        TextView textViewDeksripsi = findViewById(R.id.View_deksripsi);
        TextView textViewLatitude = findViewById(R.id.vlatitude);
        TextView textViewLongitude = findViewById(R.id.vlongitude);

        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(creatorName);
        textViewLikes.setText(alamat);
        textViewDeksripsi.setText(deksripsi);
        textViewLatitude.setText("Latitude    : "+latitude);
        textViewLongitude.setText("Longitude : "+longitude);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DetailActivity2.GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mExampleAdapter);
        mExampleList = new ArrayList<>();
        mExampleAdapter = new ExampleAdapter(this, mExampleList);

        peta =(ImageView) findViewById(R.id.peta);
        peta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map.namawisata = creatorName;
                Map.lat = lat;
                Map.lng = lng;

                Intent intent = new Intent(DetailActivity2.this, Map.class);
                startActivity(intent);
            }
        });

        rute =(ImageView) findViewById(R.id.rut);
        rute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RuteWisata.lat = lat;
                RuteWisata.lng = lng;
                RuteWisata.latLng = latLng;
                RuteWisata.des = creatorName;
                Intent i = new Intent(DetailActivity2.this, RuteWisata.class);
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
}