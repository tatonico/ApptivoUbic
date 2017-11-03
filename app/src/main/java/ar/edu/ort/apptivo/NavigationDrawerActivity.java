package ar.edu.ort.apptivo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap mMap;
    ArrayList<Linea> ListaLineas = new ArrayList<>();
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private FABToolbarLayout morph;
    private Button btnOK;
    EditText edtpartida, edtllegada;
    ImageView uno,dos,tres,cuatro;
    boolean boolArriba= false;
    LatLng LastCoordinates;
    String SelectedLine;
    CountDownTimer timer;
    UiSettings uiset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ObtenerRef();
        ClickListeners();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        morph = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.fab) {
                    morph.show();
                }
                morph.hide();
            }
        });
        morph.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






    }
    public void ObtenerRef()
    {
        btnOK = (Button) findViewById(R.id.btnOk);
        edtpartida = (EditText) findViewById(R.id.myEditText);
        edtllegada = (EditText) findViewById(R.id.myEditText2);
        uno= (ImageView) findViewById(R.id.uno);
        dos= (ImageView) findViewById(R.id.dos);
        tres= (ImageView) findViewById(R.id.tres);
        cuatro= (ImageView) findViewById(R.id.cuatro);
    }
    public void ClickListeners()
    {
        btnOK.setOnClickListener(Click);
        uno.setOnClickListener(ClickUno);
        dos.setOnClickListener(ClickDos);
        tres.setOnClickListener(ClickTres);
        cuatro.setOnClickListener(ClickCuatro);
    }
    View.OnClickListener ClickUno =new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            morph.hide();
            if (boolArriba) {
                Toast.makeText(NavigationDrawerActivity.this, "Usted ya se encuentra en un colectivo.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(NavigationDrawerActivity.this, "Gracias por avisar que se ha subico.", Toast.LENGTH_SHORT).show();
                boolArriba = true;
            }
        }
    };
    View.OnClickListener ClickDos =new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            morph.hide();
            if (boolArriba) {
                Toast.makeText(NavigationDrawerActivity.this, "Gracias por indicar que ya no esta en el colectivo", Toast.LENGTH_SHORT).show();
                boolArriba= false;
            }
            else {
                Toast.makeText(NavigationDrawerActivity.this, "Usted no esta en un colectivo.", Toast.LENGTH_SHORT).show();
            }
        }
    };
    View.OnClickListener ClickTres =new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            morph.hide();
            Toast.makeText(NavigationDrawerActivity.this, "Tres", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener ClickCuatro =new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            morph.hide();
            Toast.makeText(NavigationDrawerActivity.this, "Cuatro", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!edtpartida.getText().toString().isEmpty() && !edtllegada.getText().toString().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NavigationDrawerActivity.this);
                builder.setMessage("Usted indico bien la calle " + edtpartida.getText().toString()
                        + " y " + edtllegada.getText().toString() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(NavigationDrawerActivity.this, "Gracias por indicar", Toast.LENGTH_SHORT).show();
                                try {
                                    LatLng latlngPartida = getLocationFromAddress(NavigationDrawerActivity.this, edtpartida.getText().toString());
                                    LatLng latlngLlegada = getLocationFromAddress(NavigationDrawerActivity.this, edtllegada.getText().toString());
                                    mMap.clear();
                            /*drawCircle(latlngLlegada, "#E41436");
                            drawCircle(latlngPartida, "#30BA59");*/


                                    mMap.addMarker(new MarkerOptions()
                                            .position(latlngPartida)
                                            .title("Partida"));
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latlngLlegada)
                                            .title("Llegada"));
                                    DibujarCamino(latlngPartida, latlngLlegada);
                                    Intent intent=new Intent(NavigationDrawerActivity.this, LineasActivity.class);
                                    intent.putExtra("list", ListaLineas);
                                    startActivity(intent);
                                }
                                catch(Exception e)
                                {
                                    Toast.makeText(NavigationDrawerActivity.this, "Error al buscar las calles.", Toast.LENGTH_SHORT).show();
                                    Log.e("Error", "Error: " + e.toString());
                                }
                            }

                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert = builder.create();
                alert.show();
                        } else {
                    Log.d("no entra", "no entra");
                    Toast.makeText(NavigationDrawerActivity.this, "Complete las calles", Toast.LENGTH_SHORT).show();
                }

        }

    };

        public LatLng getLocationFromAddress(Context context, String strAddress) {
            Geocoder coder = new Geocoder(context);
            List<Address> address;
            LatLng p1 = null;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p1;

        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.navigation_drawer, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.nav_camera) {
                Toast.makeText(this, "Raios", Toast.LENGTH_SHORT).show();
            }
            if (id == R.id.nav_gallery) {
                Toast.makeText(this, "Raiosxxxx", Toast.LENGTH_SHORT).show();
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_camera) {
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }




        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.setMyLocationEnabled(true);



            timer = new CountDownTimer(5000, 20) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    try{
                        SendCoordinates();
                    }catch(Exception e){
                        Log.e("Error", "Error: " + e.toString());
                    }
                }
            }.start();

            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }

        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    public void SendCoordinates(){
        if (boolArriba){
        new SendTask().execute(String.valueOf(LastCoordinates.latitude), String.valueOf(LastCoordinates.longitude ),
                "http://localost/", SelectedLine);
        }
        timer.start();
    }
    private class SendTask extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String confirmacion) {
            super.onPostExecute(confirmacion);
            if (confirmacion == "Good"){

            }else{
                Log.i("Coordenada enviada", "BAD");
            }
        }
        @Override
        protected String doInBackground(String... parametros) {
            OkHttpClient client = new OkHttpClient();
            String Url= parametros[3]+ "/" + parametros[1] +"/"+parametros[2] + "/" + parametros[4];
            Request request = new Request.Builder()
                    .url(Url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en localhost
                String resultado = response.body().string();
                return resultado;
            } catch (IOException e) {
                Log.d("Error",e.getMessage());             // Error de Network
                return e.getMessage();
            }
        }
    }

        @Override
        public void onConnected(Bundle bundle) {

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }

        }

        @Override
        public void onConnectionSuspended(int i) {
            Toast.makeText(this, "Se ha perdido la conexion", Toast.LENGTH_SHORT).show();
            mMap.clear();
        }

        @Override
        public void onLocationChanged(Location location) {

            mLastLocation = location;
            //Place current location marker
            LastCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posicion actual para Polshu.");
        mCurrLocationMarker = mMap.addMarker(markerOptions);*/
            mMap.clear();
            drawCircle(LastCoordinates, "#3684D7");
            Log.d("latlngformat", LastCoordinates.toString());

            //move map camera
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LastCoordinates, 15));
        /*mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));*/

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

        public boolean checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted. Do the
                        // contacts-related task you need to do.
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            if (mGoogleApiClient == null) {
                                buildGoogleApiClient();
                            }
                            mMap.setMyLocationEnabled(true);
                        }

                    } else {

                        // Permission denied, Disable the functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                // other 'case' lines to check for other permissions this app might request.
                // You can add here other case statements according to your requirement.
            }
        }

        private void drawCircle(LatLng point, String color) {
            Log.d("En: ", Double.toString(point.latitude) + Double.toString(point.longitude));
            // Instantiating CircleOptions to draw a circle around the marker
            CircleOptions circleOptions = new CircleOptions();
            // Specifying the center of the circle
            circleOptions.center(point);
            // Radius of the circle
            circleOptions.radius(0);
            // Border color of the circle
            circleOptions.strokeColor(Color.parseColor(color));
            // Fill color of the circle
            circleOptions.fillColor(Color.parseColor(color));
            // Border width of the circle
            circleOptions.strokeWidth(0);
            // Adding the circle to the GoogleMap
            mMap.addCircle(circleOptions);
            Log.d("Inserto", "insertado");
        }

        private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

            String mode = "mode=transit&alternatives=true";

            // Sensor enabled
            String sensor = "sensor=true";
            String key = "key=IzaSyDfgAa6EV-cDddFKl8MV1hWVoiMXJ9j4rc";

            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&" + mode /*+ "&" + key*/;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
            //maps.googleapis.com/maps/api/directions/json?origin=Brooklyn&destination=Queens&mode=transit&key=YOUR_API_KEY

            Log.d("URL", url);
            return url;

        }

        private class ReadTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... url) {
                // TODO Auto-generated method stub
                String data = "";
                try {
                    MapHttpConnection http = new MapHttpConnection();
                    data = http.readUr(url[0]);


                } catch (Exception e) {
                    // TODO: handle exception
                    Log.d("Background Task", e.toString());
                }
                Log.d("TATO", data);
                ParsearJson(data);
                return data;
            }

            private void ParsearJson(String strJSON){
                JSONObject rootObject, currentRoute, currentLeg , currentline, currentDetails = null;
                JSONArray  routes,legs,steps;
                String Linea;


                try {
                    rootObject = new JSONObject(strJSON);
                    //Log.d("TATO", rootObject.toString());
                   routes = rootObject.getJSONArray("routes");
                    Log.d("TATO 0", routes.toString());

                    for (int i=0; i< routes.length(); i++){
                        Log.d("TATO 1", String.valueOf(routes.length()));
                        currentRoute = routes.getJSONObject(i);

                        legs = currentRoute.getJSONArray("legs");
                        Log.d("TATO 1.1", legs.toString());
                        for (int u=0; u< legs.length(); u++) {

                            currentLeg = legs.getJSONObject(i);
                            Log.d("TATO 2", currentLeg.toString());
                            steps = currentLeg.getJSONArray("steps");
                            for (int h=0; h< steps.length(); h++) {
                                Log.d("TATO 2.1", steps.toString());
                                JSONObject currentStep = steps.getJSONObject(h);
                                Log.d("TATO 2.2", currentStep.toString());

                                if(!currentStep.isNull("transit_details")) {
                                    currentDetails = currentStep.getJSONObject("transit_details");
                                    Log.d("TATO 2.3", currentDetails.toString());
                                    if(!currentDetails.isNull("line")) {
                                        currentline = currentDetails.getJSONObject("line");
                                        Log.d("TATO 2.35", currentline.toString());
                                        try {
                                            if(!currentline.isNull("short_name")) {
                                                Linea ObjLinea = new Linea();
                                                ObjLinea.nombre = currentline.getString("short_name");
                                                Log.d("TATO 2.4", ObjLinea.nombre);
                                                Log.d("TATO", String.valueOf(u) + "-"+ String.valueOf(h));
                                                ListaLineas.add(ObjLinea);
                                            }
                                        } catch (Exception e) {
                                            Log.d("Parseo", e.getMessage().toString());
                                        }
                                    }
                                }
                                //currenArrivalTime.getString("text");
                            }
                        }
                        //JSONObject currentArrivalTime = currentLeg.getJSONObject("arrival_time");
                        //Log.d("TATO 3", currentArrivalTime.toString());
                        //Log.d("TATO 4", currentArrivalTime.getString("text"));
                    }
                } catch (Throwable t) {
                    Log.d("TATO", "Could not parse malformed JSON: \"" + strJSON + "\"");

                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                new ParserTask().execute(result);
            }

        }

        public class MapHttpConnection {
            public String readUr(String mapsApiDirectionsUrl) throws IOException {
                String data = "";
                InputStream istream = null;
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(mapsApiDirectionsUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    istream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(istream));
                    StringBuffer sb = new StringBuffer();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    data = sb.toString();
                    br.close();


                } catch (Exception e) {
                    Log.d("Exception while reading", e.toString());
                } finally {
                    istream.close();
                    urlConnection.disconnect();
                }
                return data;

            }
        }

        public class PathJSONParser {

            public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
                List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;
                try {
                    jRoutes = jObject.getJSONArray("routes");
                    for (int i = 0; i < jRoutes.length(); i++) {
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                        List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                        for (int j = 0; j < jLegs.length(); j++) {
                            jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                            for (int k = 0; k < jSteps.length(); k++) {
                                String polyline = "";
                                polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                                    List<LatLng> list = decodePoly(polyline);
                                    for (int l = 0; l < list.size(); l++) {
                                        HashMap<String, String> hm = new HashMap<String, String>();
                                        hm.put("lat",
                                                Double.toString(((LatLng) list.get(l)).latitude));
                                        hm.put("lng",
                                                Double.toString(((LatLng) list.get(l)).longitude));
                                        path.add(hm);
                                }
                            }
                            routes.add(path);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;

            }

            private List<LatLng> decodePoly(String encoded) {
                List<LatLng> poly = new ArrayList<LatLng>();
                int index = 0, len = encoded.length();
                int lat = 0, lng = 0;

                while (index < len) {
                    int b, shift = 0, result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lat += dlat;

                    shift = 0;
                    result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lng += dlng;

                    LatLng p = new LatLng((((double) lat / 1E5)),
                            (((double) lng / 1E5)));
                    poly.add(p);
                }
                return poly;
            }
        }

        private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(
                    String... jsonData) {
                // TODO Auto-generated method stub
                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;
                try {
                    jObject = new JSONObject(jsonData[0]);
                    PathJSONParser parser = new PathJSONParser();
                    routes = parser.parse(jObject);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("RUTA", routes.toString());
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
                ArrayList<LatLng> points = null;
                PolylineOptions polyLineOptions = null;

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(4);
                    polyLineOptions.color(Color.BLUE);
                }

                mMap.addPolyline(polyLineOptions);

            }
        }

        public void DibujarCamino(LatLng latlngOne, LatLng latlngTwo) {
            String url = getMapsApiDirectionsUrl(latlngOne, latlngTwo);
            Log.d("URL", url);
            ReadTask downloadTask = new ReadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

        public void CalcularDistancia(LatLng latLongA, LatLng latLongB) {
            float[] results = new float[1];
            Location.distanceBetween(latLongA.latitude, latLongB.longitude,
                    latLongB.latitude, latLongB.longitude,
                    results);
        }
        }
