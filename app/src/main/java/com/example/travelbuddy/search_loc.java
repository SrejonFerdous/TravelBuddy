package com.example.travelbuddy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;

public class search_loc extends Fragment implements OnMapReadyCallback {

    View view;
    LinearLayout current_loc;
    Button ok;
    private GoogleMap myMap;
    private SearchView searchView;
    private FusedLocationProviderClient client;
    private SupportMapFragment supportMapFragment;
    private final int FINE_PERMISSION_CODE = 1;
    String city, state, country, postalCode, knownName, dis, totAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_loc, container, false);
        searchView = (SearchView) view.findViewById(R.id.search_loc_sv);
        current_loc = (LinearLayout) view.findViewById(R.id.search_loc_current);
        ok=(Button) view.findViewById(R.id.search_loc_ok);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.search_loc_map);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        current_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                getCurrentLocation();
                                ok.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ok.setVisibility(View.VISIBLE);
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (addressList.size() != 0) {
                        Address address = addressList.get(0);
                        city = address.getLocality();
                        state = address.getAdminArea();
                        country = address.getCountryName();
                        postalCode = address.getPostalCode();
                        knownName = address.getFeatureName();
                        dis = address.getSubAdminArea();
                        totAddress = address.getAddressLine(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        myMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    } else Toast.makeText(getActivity(), "wrong place", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ok.setVisibility(View.GONE);
                return false;
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totAddress!=null && city!=null){
                    Fragment fragment=new search();
                    Bundle bundle=new Bundle();
                    bundle.putString("totAddress",totAddress);
                    bundle.putString("city",city);
                    bundle.putString("country",country);
                    bundle.putString("state",state);
                    bundle.putString("knownName",knownName);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,fragment)
                            .addToBackStack(null).commit();
                }else Toast.makeText(getActivity(),"Select a location",Toast.LENGTH_SHORT).show();
                if(ok.getVisibility()==View.VISIBLE) ok.setVisibility(View.GONE);
            }
        });
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            try{
                ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
            }catch (IllegalStateException e){
                Toast.makeText(getActivity(),"(search_loc:maps) permission request problem :"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
            return;
        }
        Task<Location> locationTask = client.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                        List<Address> addressList = null;
                        Geocoder geocoder=new Geocoder(getContext());//can try getActivity()
                        try {
                            addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        Address address=addressList.get(0);
                        city = address.getLocality();
                        state = address.getAdminArea();
                        country = address.getCountryName();
                        postalCode = address.getPostalCode();
                        knownName = address.getFeatureName();
                        dis = address.getSubAdminArea();
                        totAddress = address.getAddressLine(0);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(addressList.get(0).getAddressLine(0)));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                    }
                });
            }
        });
    }
}