package com.luckyecuador.app.PintucoAPP.ui.puntos;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Maps.TaskLoadedCallback;
import com.luckyecuador.app.PintucoAPP.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //INICIO
    private GoogleMap mMap;
    //    private MarkerOptions place1, place2, place3;
//    Button btnGetDirection;
    private Polyline currentPolyline;
    DatabaseHelper handler;
    private String user;
    ArrayList<BasePharmaValue> pdv;
    //FIN
    FloatingActionButton fab;

    public MapsFragment(FloatingActionButton fab) {
        this.fab = fab;
    }

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("MAP VISIBLE", "MAP VISIBLE");
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("MAP INVISIBLE", "MAP INVISIBLE");
        fab.setVisibility(View.VISIBLE);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {

        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_maps, container, false);
        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);
        LoadData();

//        btnGetDirection = rootView.findViewById(R.id.btnGetDirection);

//        btnGetDirection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), place3.getPosition(), "driving"), "driving");
//            }
//        });
        //27.658143,85.3199503
        //27.667491,85.3208583

        pdv = handler.consultarRutaOptima(user);

//        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
//        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");
//        place3 = new MarkerOptions().position(new LatLng(27.679487, 85.3348787)).title("Location 3");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapNearBy);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("Markers", "Added Markers");
        List<Marker> markers = new ArrayList<Marker>();
        double sum_latitud = 0, sum_longitud = 0, prom_latitud = 0, prom_longitud = 0;
        for (int i = 0; i < pdv.size(); i++) {
            String title = pdv.get(i).getPos_id() + " - " + pdv.get(i).getPos_name();
            double latitud = Double.parseDouble(pdv.get(i).getLatitud());
            double longitud = Double.parseDouble(pdv.get(i).getLongitud());
            sum_latitud = sum_latitud + latitud;
            sum_longitud = sum_longitud + longitud;
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title(title));
            markers.add(marker);
        }
        prom_latitud = Math.floor((sum_latitud / pdv.size()) * 10000) / 10000;
        prom_longitud = Math.floor((sum_longitud / pdv.size()) * 10000) / 10000;
        Log.i("PROM LAT", prom_latitud + "");
        Log.i("PROM LONG", prom_longitud + "");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(prom_latitud, prom_longitud), 12.0f));
        //ZOOM
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
//        mMap.setPadding(150, 150, 150, 150);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        Log.i("USUARIO",user);
    }

    private String getUrl(LatLng origin, LatLng dest, LatLng wp, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Waypoints of route
        String str_wp = "waypoints=" + wp.latitude + "," + wp.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&"  + str_wp + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}