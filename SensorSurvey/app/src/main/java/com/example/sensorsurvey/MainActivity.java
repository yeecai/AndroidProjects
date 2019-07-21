package com.example.sensorsurvey;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;

    // Individual light and proximity sensors.
    private Sensor mSensorProximity;
    private Sensor mSensorLight;
    private Sensor mSensorHumidity;

    // TextViews to display current sensor values
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;
    private TextView mTextSensorHumidity;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
      /*  List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sensorText = new StringBuilder();

        for(Sensor currentSensor : sensorList) {
            sensorText.append(currentSensor.getName()).append(
                    System.getProperty("line.separator")
            );
        }

        TextView sensorTextView = (TextView) findViewById(R.id.sensor_list);
        sensorTextView.setText(sensorText);*/

      mTextSensorLight = (TextView) findViewById(R.id.label_light);
      mTextSensorProximity = (TextView) findViewById(R.id.label_proximity);
      mTextSensorHumidity = (TextView) findViewById(R.id.label_humidity);
      mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setBackgroundColor(Color.rgb(255, 255, 255));

      mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
      mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
      mSensorHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

      String sensor_error = getResources().getString(R.string.error_no_sensor);

      if(mSensorLight == null) {
          mTextSensorLight.setText(sensor_error);
      }

      if(mSensorProximity == null) {
          mTextSensorProximity.setText(sensor_error);
      }

      if(mSensorHumidity == null) {
          mTextSensorHumidity.setText(sensor_error);
      }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mSensorProximity != null) {
            mSensorManager.registerListener(this,mSensorProximity,SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorLight != null) {
            mSensorManager.registerListener(this,mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorHumidity != null) {
            mSensorManager.registerListener(this, mSensorHumidity, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(getResources().getString(
                        R.string.label_light, currentValue));

                break;
            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(getResources().getString(
                        R.string.label_proximity, currentValue));
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                mTextSensorHumidity.setText(getResources().getString(
                        R.string.label_humidity, currentValue));
            default:
                // do nothing, no break even lol
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
