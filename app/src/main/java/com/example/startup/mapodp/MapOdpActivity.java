package com.example.startup.mapodp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.startup.mapodp.app.AppConfig;
import com.example.startup.mapodp.helper.HttpHandler;
import com.example.startup.mapodp.helper.SessionManager;
import com.example.startup.mapodp.helper.UserGlobal;
import com.example.startup.mapodp.helper.UserSQLiteHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapOdpActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MapOdpActivity.class.getSimpleName();
    private static final long SET_INTERVAL = 10 * 1000;
    private static final long FASTEST_INTERVAL = 1 * 1000;
    public Toolbar toolbar;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    View mapView;

    Location mLastLocation;
    double lat = 0, lng = 0;


    ImageView imgAddress;
    ImageView imgName;
    ImageView imgList;
    ImageView imgNearby;

    LinearLayout buttonAddress;
    LinearLayout buttonName;
    LinearLayout buttonList;
    LinearLayout buttonNearby;

    ImageView logout;
    ImageView refresh;

    TextView label;
    ImageView icon;

    int ADDRESS= 0;
    int NAME= 1;
    int LIST= 2;
    int DETAILS= 3;


    int state=3;
    private UserSQLiteHandler db;
    private SessionManager session;

    







    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    ArrayList<ODP> odpList;

    Marker markers[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_odp);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        db = new UserSQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        buildGoogleApiClient();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        imgAddress = (ImageView) findViewById(R.id.img_address);
        imgName = (ImageView) findViewById(R.id.img_name);
        imgList = (ImageView) findViewById(R.id.img_list);
        imgNearby = (ImageView) findViewById(R.id.img_nearby);

        logout = (ImageView) findViewById(R.id.logout) ;
        refresh = (ImageView) findViewById(R.id.refresh) ;

        buttonAddress = (LinearLayout) findViewById(R.id.button_address);
        buttonName = (LinearLayout) findViewById(R.id.button_name);
        buttonList = (LinearLayout) findViewById(R.id.button_list);
        buttonNearby = (LinearLayout) findViewById(R.id.button_nearby);

        icon = (ImageView) findViewById(R.id.icon);
        label = (TextView) findViewById(R.id.label);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getODP().execute();
            }
        });
        buttonAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAdapter(0);
            }
        });
        buttonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAdapter(1);
            }
        });
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAdapter(2);
            }
        });
        buttonNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabAdapter(3);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });



    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    private void tabAdapter(int no)
    {
        if(no==0)
        {
            callPlaceAutocompleteActivityIntentForAddress();
        }
        else  if(no==1)
        {
            callNameDialog();
        }
        else if(no==2)
        {
            callListODP();
        }
        else if(no==3)
        {
            setNearMe();
        }
    }

    private void setNearMe() {
        icon.setImageResource(R.drawable.nearby_active);
        label.setText("Nearest ODP from Your Location");

        int index = Searcher.getNearest(odpList,new LatLng(lat,lng));
        adjustCamera(index);
    }

    private void callNameDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(MapOdpActivity.this);
        dialog.setContentView(R.layout.activity_dialog_name);
        dialog.setTitle("Search ODP by Name");

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.odp_name);

        LinearLayout cancelButton = (LinearLayout) dialog.findViewById(R.id.button_cancel);
        LinearLayout okButton = (LinearLayout) dialog.findViewById(R.id.button_ok);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = text.getText().toString();
            if(!name.contentEquals(""))
            {
                if(Searcher.isExist(odpList,name))
                {
                    int index = Searcher.byName(odpList,name);
                    icon.setImageResource(R.drawable.name_active);
                    label.setText(odpList.get(index).namaODP);
                    adjustCamera(index);
                }
                else
                {
                    Toast.makeText(MapOdpActivity.this, "no ODP named "+name, Toast.LENGTH_SHORT).show();
                }

            }

                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        dialog.show();
    }

    private void callListODP() {

        Intent i = new Intent(MapOdpActivity.this,ListOdpActivity.class);
        i.putParcelableArrayListExtra("odp_list",odpList);
        startActivityForResult(i,LIST);
    }


    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 500);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext(),MapOdpActivity.this)) {
            getMyLocation();
        }
        else
        {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSION_REQUEST_CODE_LOCATION,getApplicationContext(),MapOdpActivity.this);
        }

    }

    private void getMyLocation()
    {
        mMap.setMyLocationEnabled(true);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            new getODP().execute();

        }
    }


    public  void requestPermission(String strPermission, int perCode, Context _c, Activity _a){

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
            Toast.makeText(getApplicationContext(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
        }
    }

    public  boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMyLocation();
                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }




    private void callPlaceAutocompleteActivityIntentForAddress() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, ADDRESS);
//PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADDRESS) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                icon.setImageResource(R.drawable.address_active);
                label.setText("The Nearest ODP from "+place.getAddress());

                int index = Searcher.getNearest(odpList,place.getLatLng());
                adjustCamera(index);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                label.setText(status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else if (requestCode == LIST) {
            if (resultCode == RESULT_OK) {
                int index = data.getIntExtra("index",0);
                icon.setImageResource(R.drawable.name_active);
                label.setText(odpList.get(index).namaODP);
                adjustCamera(index);


            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private void adjustCamera(int i)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        builder.include(markers[i].getPosition());


        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.05); // offset from edges of the map 12% of screen


        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }




    private class getODP extends AsyncTask<String, Integer, Double> {

        ProgressDialog asyncDialog = new ProgressDialog(MapOdpActivity.this);

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result){


            markers= new Marker[odpList.size()];
            for(int i=0;i<odpList.size();i++) {
                markers[i] = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(odpList.get(i).latitude, odpList.get(i).longitude))
                        .title(odpList.get(i).namaODP)
                        .snippet("click to view details")
                );
                markers[i].setTag(i);
            }
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    int i = (int) arg0.getTag();
                    Intent intent = new Intent(MapOdpActivity.this,ODPDetailsActivity.class);
                    intent.putExtra("odp",odpList.get(i));
                    intent.putExtra("tag",i);
                    startActivity(intent);
                    //Toast.makeText(MapOdpActivity.this, odpList.get(i).namaODP, Toast.LENGTH_SHORT).show();
                }
            });

            asyncDialog.dismiss();
            super.onPostExecute(result);
        }



        protected void onProgressUpdate(Integer... progress){
        }

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage("please wait...");
            //show dialog
            asyncDialog.show();

            super.onPreExecute();
        }

        public void postData() {
            // Create a new HttpClient and Post Header


            String url = AppConfig.getODPLocationURL();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                //nameValuePairs.add(new BasicNameValuePair("username", UserGlobal.getUser(getApplicationContext()).username));
  //              nameValuePairs.add(new BasicNameValuePair("password", UserGlobal.getUser(getApplicationContext()).password));

                nameValuePairs.add(new BasicNameValuePair("username", "admin"));
                nameValuePairs.add(new BasicNameValuePair("password", "admin"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "UTF-8");

                if (jsonStr != null) {
                    try {
                        JSONArray arrayODP = new JSONArray(jsonStr);

                        odpList = new ArrayList<ODP>();
                        for(int i=0;i<arrayODP.length();i++)
                        {
                            JSONObject odpJson=arrayODP.getJSONObject(i);
                            odpList.add(new ODP(odpJson));
                        }

                    } catch (final JSONException e) {
                       // Toast.makeText(MapOdpActivity.this, "Json parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    //Toast.makeText(MapOdpActivity.this, "Couldn't get json from server", Toast.LENGTH_SHORT).show();

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }



    }



}
