package com.example.womensafety;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.Adapter.Adapter2;
import com.example.womensafety.Adapter.Contact;
import com.example.womensafety.Data.DbHelper;
import com.example.womensafety.test.SecondPage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StartScreen extends AppCompatActivity implements SensorEventListener {
    private DbHelper mDbHelper;
    private ArrayList<Contact> savedContactList;
    private Adapter2 adapter2;
    private RecyclerView recyclerView;
    private TextView xTextView, yTextView, zTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean available, itIsNotFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ, xDifference, yDifference, zDifference;
    private float shakeThreshold = 5f;
    private Vibrator vibrator;
    public String longitude, latitude;
    private int LocationRequestCode = 10001;
    private static final String TAG = "StartScreen";
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFirstPage();
            }
        });
        mDbHelper = new DbHelper(this);

        //recyclerView Initialization
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //permission to send sms
        ActivityCompat.requestPermissions(StartScreen.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS},
                PackageManager.PERMISSION_GRANTED);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            available = true;
        } else {
            Log.d("StartScreen", "Accelerometer sensor is not available");
            available = false;
        }
        //initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }


    public void openFirstPage() {
        Intent intent = new Intent(this, SecondPage.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
        savedContactList = mDbHelper.getAllContacts();
        int listSize = savedContactList.size();

        for (int i = 0; i < listSize; i++) {
            Log.i("Member name: ", String.valueOf(savedContactList.get(i)));
        }

        adapter2 = new Adapter2(savedContactList, 2);
        recyclerView.setAdapter(adapter2);

        //location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            askLocationPermission();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        currentX = event.values[0];
        currentY = event.values[1];
        currentZ = event.values[2];

        if (itIsNotFirstTime) {
            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);
            if ((xDifference > shakeThreshold && yDifference > shakeThreshold) || (yDifference > shakeThreshold && zDifference > shakeThreshold)
                    || (xDifference > shakeThreshold && zDifference > shakeThreshold)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    sendSms();
                } else {
                    vibrator.vibrate(500);
                }
            }
        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        itIsNotFirstTime = true;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (available)
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (available)
            sensorManager.unregisterListener(this);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "askLocationPermission:show alert dialog..");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationRequestCode);
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    longitude = String.valueOf(location.getLongitude());
                    latitude = String.valueOf(location.getLatitude());
                    Log.d(TAG, "onSuccess: " + location.getLongitude());
                    Log.d(TAG, "onSuccess: " + location.getLatitude());
                } else {
                    Log.d(TAG, "onSuccess: Location was null.. ");
                }

            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure" + e.getLocalizedMessage());
            }
        });

    }

    public void sendSms() {
        for (int i = 0; i < savedContactList.size(); i++) {
            String number = savedContactList.get(i).getNumber();
            Log.i(TAG, "onSend" + number);
            String message = "Emergency Help..\n" +
                    "I'm in trouble please help me at below location\n" +
                    "  https://maps.google.com/maps?q=" + latitude + "," + longitude;
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(number, null, message, null, null);
        }
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }

    }
}

