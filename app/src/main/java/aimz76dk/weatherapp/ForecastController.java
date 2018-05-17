package aimz76dk.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;


import cz.msebera.android.httpclient.Header;

import static aimz76dk.weatherapp.WeatherDataModel.forecastFromJson;

public class ForecastController extends AppCompatActivity {

    // View objects
    private TextView mDayLabel1;
    private ImageView mWeatherImage1;
    private TextView mTemperatureLabel1;

    private TextView mDayLabel2;
    private ImageView mWeatherImage2;
    private TextView mTemperatureLabel2;

    private TextView mDayLabel3;
    private ImageView mWeatherImage3;
    private TextView mTemperatureLabel3;

    // Tag for logging & OpenWeatherMap URL for API
    private final String LOGCAT_TAG = "Forecast";
    private final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_controller);

        // GET / SET view objects from layout
        mDayLabel1 = findViewById(R.id.day1);
        mDayLabel2 = findViewById(R.id.day2);
        mDayLabel3 = findViewById(R.id.day3);

        mTemperatureLabel1 = findViewById(R.id.temp1);
        mTemperatureLabel2 = findViewById(R.id.temp2);
        mTemperatureLabel3 = findViewById(R.id.temp3);

        mWeatherImage1 = findViewById(R.id.conIcon1);
        mWeatherImage2 = findViewById(R.id.conIcon2);
        mWeatherImage3 = findViewById(R.id.conIcon3);

        // Text field for query
        final EditText editTextField = findViewById(R.id.forecastQueryET);

        ImageButton backButton = findViewById(R.id.forecastBackButton);

        // When user clicks back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back and destroy this
                finish();
            }
        });


        // When user hits 'enter', do the request and handle what to do
        editTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Log.d(LOGCAT_TAG, "Getting weather forecast for new city");

                // AsyncHttpClient belongs to the loopj dependency
                AsyncHttpClient client = new AsyncHttpClient();

                // Making an HTTP GET request by providing a URL and the parameters
                RequestParams params = new RequestParams();
                // Entered city
                params.put("q", editTextField.getText());
                Log.d(LOGCAT_TAG, "Entered city is " + editTextField.getText());
                // Gets 4 days with today, so we skip the first day when we update UI
                params.put("cnt", 4);
                params.put("appid", "e72ca729af228beabd5d20e3b7749713");
                params.put("units", "metric");
                client.get(FORECAST_URL, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.d(LOGCAT_TAG, "Success! JSON: " + response.toString());

                    // Update UI by passing response to forecastFromJson method in WeatherDataModel class
                    // Which returns a list of objects of WeatherDataModel - 1 for each day
                    updateForecastUI(forecastFromJson(response));

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                    Log.e(LOGCAT_TAG, "Fail " + e.toString());
                    Toast.makeText(ForecastController.this, "Request Failed, Try Again", Toast.LENGTH_SHORT).show();

                    Log.d(LOGCAT_TAG, "Status code " + statusCode);
                    Log.d(LOGCAT_TAG, "Here's what we got instead " + response.toString());
                }

            });
            return true;

            }
        });

    }

    private void updateForecastUI(List<WeatherDataModel> forecastList) {

        int resourceID;

        // int = 1 because we want to skip today / fist item in array
        for (int i = 1; i <= 3; i++) {

            WeatherDataModel tmpWeatherDataModel = forecastList.get(i);

            // Check which day for determine what UI to update
            switch (i) {

                case 1:
                    mDayLabel1.setText(tmpWeatherDataModel.getDay());
                    mTemperatureLabel1.setText(tmpWeatherDataModel.getTemperature());
                    resourceID = getResources().getIdentifier(tmpWeatherDataModel.getIconName(), "drawable", getPackageName());
                    mWeatherImage1.setImageResource(resourceID);
                    break;

                case 2:
                    mDayLabel2.setText(tmpWeatherDataModel.getDay());
                    mTemperatureLabel2.setText(tmpWeatherDataModel.getTemperature());
                    resourceID = getResources().getIdentifier(tmpWeatherDataModel.getIconName(), "drawable", getPackageName());
                    mWeatherImage2.setImageResource(resourceID);
                    break;

                case 3:
                    mDayLabel3.setText(tmpWeatherDataModel.getDay());
                    mTemperatureLabel3.setText(tmpWeatherDataModel.getTemperature());
                    resourceID = getResources().getIdentifier(tmpWeatherDataModel.getIconName(), "drawable", getPackageName());
                    mWeatherImage3.setImageResource(resourceID);
                    break;

            }
        }
    }

}

