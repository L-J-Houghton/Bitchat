package fragments;

//import com.bitchat.MainActivity;
//import com.bitchat.MySingleton;
import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bitchat.MainActivity;
import com.bitchat.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class gMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap gMap;                     //google map object
    private GoogleApiClient googleApiClient;    //google api client object
    private LocationRequest locationRequest;    //location request object

    protected static final String TAG = gMapFragment.class.getSimpleName(); //TAG = gMapFragment

    //connection failure resolution
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int MY_LOCATION = 0;

    //Goldsmiths Map Bounds
    //goldSmiths (south-west corner, north-east corner)
    private static final LatLngBounds goldSmithMapBounds =
            new LatLngBounds(new LatLng(51.471721, -0.039895), new LatLng(51.476693, -0.032262));

    //latitude and longitude
    double currentLatitude;
    double currentLongitude;

    //markers
    private Marker markerRHB;
    private Marker markerLibrary;
    private Marker markerSU;
    private Marker markerPSH;
    private Marker markerDTH;
    private Marker markerDoC;
    private Marker markerCG;
    private Marker markerGym;
    private Marker markerWHB;

    //Forum related
    ArrayAdapter adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_map, container, false);

        View forumView = inflater.inflate(R.layout.fragment_forum_list, container, false);
        listView = (ListView) forumView.findViewById(R.id.forum_list);

        listView.setAdapter(adapter);

        return mapView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Goldsmiths");

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //mapFragment: retrieves the mapView in UI
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_layout);
        //getMapAsync: will start the map service
        mapFragment.getMapAsync(this);

        //GOOGLE API CLIENT BUILD
        //GoogleApiClient object is created using the Builder pattern
        //LocationServices end point will be used from Google Play Services
        //next two lines -> this class will handle the connection stuff
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)           //onConnected, onConnectionSuspended
                .addOnConnectionFailedListener(this)    //onConnectionFailed
                .build();

        //LOCATION REQUEST
        //High Accurary Priority -> requesting as accurate a location as possible
        //Interval of 5 seconds, between active location updates
        //Passive Interval of 1 second, while other applications might be requesting location
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)          // 5 seconds, in milliseconds
                .setFastestInterval(1 * 1000);  // 1 second, in milliseconds

        //for real-world device testing
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        //CUSTOMIZED MUTED BLUE MAP
        gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));


        //LATITUDE & LONGITUDE + CAMERA & VIEW
        LatLng goldsmithsUoL = new LatLng(51.474775, -0.035977);

        CameraPosition GOLDSMITHS = new CameraPosition.Builder()
                .target(goldsmithsUoL)
                .zoom(17.0f)
                .bearing(245)
                .tilt(56)
                .build();

        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(GOLDSMITHS));

        //LOCATION LAYER: ENABLE
        //In order to enable location layer, we require user permission
        //if location permission has been granted, set the location layer
        //else request the permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        } else {
            //requestPermissions is only made available for API level 23+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION);
        }

        //MARKERS
        //for buildings of Goldsmiths, University of London Campus

        //Richard Hoggart Building
        final LatLng RHB = new LatLng(51.474211, -0.035816);
        markerRHB = gMap.addMarker(new MarkerOptions()
                .position(RHB)
                .title("RHB")
                .snippet("Richard Hoggart Building")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Rutherford Building: Library & IT Services
        final LatLng Library = new LatLng(51.475013, -0.035679);
        markerLibrary = gMap.addMarker(new MarkerOptions()
                .position(Library)
                .title("Rutherford Library")
                .snippet("Library & IT Services")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Student Union
        final LatLng SU = new LatLng(51.474497, -0.036167);
        markerSU = gMap.addMarker(new MarkerOptions()
                .position(SU)
                .title("SU")
                .snippet("Student Union")
                .alpha(0.5f)
                .visible(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Professor Stuart Hall Building
        final LatLng PSH = new LatLng(51.472722, -0.037185);
        markerPSH = gMap.addMarker(new MarkerOptions()
                .position(PSH)
                .title("PSH")
                .snippet("Professor Stuart Hall Building")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Deptford Town Hall
        final LatLng DTH = new LatLng(51.475293, -0.037810);
        markerDTH = gMap.addMarker(new MarkerOptions()
                .position(DTH)
                .title("DTH")
                .snippet("Deptford Town Hall Building")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Department of Computing
        final LatLng DoC = new LatLng(51.474149, -0.037943);
        markerDoC = gMap.addMarker(new MarkerOptions()
                .position(DoC)
                .title("DoC")
                .snippet("Department of Computing")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //College Green
        final LatLng CG = new LatLng(51.473427, -0.036750);
        markerCG = gMap.addMarker(new MarkerOptions()
                .position(CG)
                .title("CG")
                .snippet("College Greens")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Club Pulse Fitness Centre
        final LatLng Gym = new LatLng(51.473001, -0.037431);
        markerGym = gMap.addMarker(new MarkerOptions()
                .position(Gym)
                .title("Gym")
                .snippet("Club Pulse Fitness Centre")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Ian Gullard Lecture Theatre + Whitehead Building
        final LatLng WHB = new LatLng(51.473755, -0.037080);
        markerWHB = gMap.addMarker(new MarkerOptions()
                .position(WHB)
                .title("WHB")
                .snippet("Ian Gullard Lecture Theatre/Whitehead Building")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        //Listening to enter respective forums page
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(marker.equals(markerRHB))
                    ((MainActivity)getActivity()).enterForum("RHB","From Map Forum");

                if(marker.equals(markerLibrary))
                    ((MainActivity)getActivity()).enterForum("Lib","From Map Forum");

                if(marker.equals(markerPSH))
                    ((MainActivity)getActivity()).enterForum("PSH","From Map Forum");

                if(marker.equals(markerDTH))
                    ((MainActivity)getActivity()).enterForum("DTH","From Map Forum");

                if(marker.equals(markerDoC))
                    ((MainActivity)getActivity()).enterForum("DoC","From Map Forum");

                if(marker.equals(markerCG))
                    ((MainActivity)getActivity()).enterForum("CG","From Map Forum");

                if(marker.equals(markerGym))
                    ((MainActivity)getActivity()).enterForum("Gym","From Map Forum");

                if(marker.equals(markerWHB))
                    ((MainActivity)getActivity()).enterForum("WHB","From Map Forum");

            }
        });


    }


    //LOCATION PERMISSION REQUEST DIALOG BOX
    //if permission has been granted, enable location layer
    //else message is displayed
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        gMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Bit Chat requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                break;
        }
    }

    //CONNECTION CALL BACKs
    //Provides callbacks that are called when the client is connected or disconnected
    //  - onConnected
    //  - onConnectionSuspended
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location Services Connected");
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Services Suspended. Requires reconnect.");
    }

    //CONNECTION FAILED LISTENER
    //Provides callbacks for scenarios that result in a failed attempt to connect the client to the service.
    //  - onConnectionFailed
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.i(TAG, "Connection Failed: Error Code = " + connectionResult.getErrorCode());
        }
    }

    //LOCATION LISTENER
    //  - onLocationChanged
    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, location.toString()); //location changes logged in Android Monitor

        //TODO :
        //  - enter latitude and longitude in a database
        currentLatitude  = location.getLatitude();
        currentLongitude = location.getLongitude();

        //move the camera so current location is centered
        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);

        //current location must be within goldsmiths defined map bounds
        //else camera stay static and doesn't change position
        if(goldSmithMapBounds.contains(currentLatLng))
            gMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

    }

    //MAPS ACTIVITY
    //  - onStart
    //  - onPause
    //  - onResume
    //  - onStop
    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(googleApiClient.isConnected())
            requestLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

}
