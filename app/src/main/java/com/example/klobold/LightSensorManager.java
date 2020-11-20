package com.example.klobold;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensorManager implements SensorEventListener {

    private MainActivity activity;
    private SensorManager sensorManager;
    private Sensor lightSensor;

    private static float lightThreshold = 10;
    private float currentLightValue;


    LightSensorManager(MainActivity activity) {
        this.activity = activity;
        sensorManager = (SensorManager) activity.getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        currentLightValue = 0;
    }

    void registerListener() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    boolean isAboveThreshold() {
        return (currentLightValue > lightThreshold);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            currentLightValue = event.values[0];

            activity.modeManager.adaptMode(isAboveThreshold());
        }
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }
}
