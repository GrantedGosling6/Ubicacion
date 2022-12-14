package com.example.ubicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private TextView txtLongitud, txtLatitud, txtProvider, txtAccuracy, txtTimeToFix, txtEnabledProviders, txtChangeEnabledProviders;
    private long upTimeToResume;
    private Button btnShowOnMap, btnChangeEnableProviders;
    private List<String> enabledProviders;
    private String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        txtProvider = findViewById(R.id.txtProvider);
        txtAccuracy = findViewById(R.id.txtAccuracy);
        txtTimeToFix = findViewById(R.id.txtTimeToFix);
        txtEnabledProviders = findViewById(R.id.txtEnabledProviders);
        btnChangeEnableProviders = findViewById(R.id.btnChangeLocationProviderSettings);
        btnChangeEnableProviders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(Intent));

            }
        });
        btnShowOnMap = findViewById(R.id.btnShowOnMap);
        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("geo:"+lat+","+ lon);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        StringBuffer stringBuffer = new StringBuffer();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        enabledProviders = locationManager.getProviders(criteria, true);
        if(enabledProviders == null)
            txtEnabledProviders.setText("");
        else{
            for(String enableProvider : enabledProviders){
               stringBuffer.append(enableProvider).append(" ");
               try {
                   locationManager.requestSingleUpdate(enableProvider, this, null );
               }catch(SecurityException ex){
                   ex.printStackTrace();
               }
            }
            txtEnabledProviders.setText(stringBuffer);
        }
        upTimeToResume = SystemClock.uptimeMillis();
        txtLongitud.setText("");
        txtLatitud.setText("");
        txtAccuracy.setText("");
        txtProvider.setText("");
        txtTimeToFix.setText("");
        findViewById(R.id.lblTimeToFixUnits).setVisibility(View.GONE);
        findViewById(R.id.lblAccuracyUnits).setVisibility(View.GONE);

    }


    @Override
    protected void onPause(){
        super.onPause();
        try {
            locationManager.removeUpdates(this );

        }catch (SecurityException ex){
            ex.printStackTrace();
        }

    }




    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        txtLatitud.setText(lat);
        txtLongitud.setText(lon);
        txtProvider.setText(String.valueOf(location.getProvider()));
        txtAccuracy.setText(String.valueOf(location.getAccuracy()));
        long timeToFix = SystemClock.uptimeMillis() - upTimeToResume;

        findViewById(R.id.lblTimeToFixUnits).setVisibility(View.VISIBLE);
        findViewById(R.id.lblAccuracyUnits).setVisibility(View.VISIBLE);

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}