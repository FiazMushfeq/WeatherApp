package com.example.fiaz.weatherapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView mainTextView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    ImageView mainImageView;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    TextView quote;
    EditText editText;
    Button button;
    String zipcode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTextView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        mainImageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        quote = findViewById(R.id.textViewQuote);
        editText = findViewById(R.id.id_editText);
        editText.setTextColor(Color.GRAY);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipcode = editText.getText().toString();
                AsyncThread weatherThread = new AsyncThread();
                weatherThread.execute(zipcode);
                System.out.println("STATUS: " + weatherThread.getStatus());
                System.out.println(zipcode);
            }
        });
        quote.setTextColor(Color.CYAN);
        mainTextView.setTextColor(Color.MAGENTA);
        textView2.setTextColor(Color.RED);
        textView3.setTextColor(Color.RED);
        textView4.setTextColor(Color.RED);
        textView5.setTextColor(Color.RED);
        //AsyncThread weatherThread = new AsyncThread();
        //weatherThread.execute();
    }
    public class AsyncThread extends AsyncTask<String, Void, Void> {
        String result = "";

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String addZipCode = strings[0];
                String putURL = "http://api.openweathermap.org/data/2.5/forecast?APPID=&zip=" + addZipCode;
                URL url = new URL(putURL);
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID="); // params can be switched
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                // String result = getStringFromInputStream(inputStream);
                String readInfo = "";
                while((readInfo = bufferedReader.readLine()) != null) {
                    result += readInfo;
                }
            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            JSONObject jsonResult;
            try {
                jsonResult = new JSONObject(result);
                JSONArray jsonList = jsonResult.getJSONArray("list");
                for (int i = 0; i < 5; i++) {
                    JSONObject list = jsonList.getJSONObject(i);
                    String mainItem = list.getString("main");
                    JSONObject mainItems = new JSONObject(mainItem);
                    JSONArray weatherItems = list.getJSONArray("weather");
                    JSONObject weatherList = weatherItems.getJSONObject(0); // check
                    String weatherConditions = weatherList.getString("main");
                    // Time
                    // https://stackoverflow.com/questions/16592493/getting-time-from-a-date-object
                    int time = list.getInt("dt");
                    Date timeDate = new Date((long)time * 1000);
                    boolean isAM = true;
                    SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                    String getHour = hourFormat.format(timeDate);
                    int hour = Integer.parseInt(getHour.toString());
                    if(hour > 12) {
                        hour -= 12;
                        isAM = false;
                    }
                    SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
                    String getMinute = minuteFormat.format(timeDate);
                    String timeList = "";
                    if(isAM) {
                        timeList = hour + ":" + getMinute + " AM";
                    } else {
                        timeList = hour + ":" + getMinute + " PM";
                    }
                    // Low and High Temperatures
                    double getMin = (1.8*(mainItems.getDouble("temp_min") - 273.15)) + 32;
                    double getMax = (1.8*(mainItems.getDouble("temp_max") - 273.15)) + 32;
                    int min = Math.round((float)getMin);
                    int max = Math.round((float)getMax);
                    // Weather Condition
                    String desc = weatherConditions;
                    if(i == 0) {
                        mainTextView.setText("Low: " + min + "°F\nHigh: " + max + "°F\n" + desc + "\n" + timeList);
                        whichImage(desc, mainImageView);
                        mainImageView.getLayoutParams().height = 150;
                        mainImageView.getLayoutParams().width = 150;
                    }
                    if(i == 1) {
                        textView2.setText("Low: " + min + "°F\nHigh: " + max + "°F\n" + desc + "\n" + timeList);
                        whichImage(desc, imageView2);
                        imageView2.getLayoutParams().height = 100;
                        imageView2.getLayoutParams().width = 100;
                    }
                    if(i == 2) {
                        textView3.setText("Low: " + min + "°F\nHigh: " + max + "°F\n" + desc + "\n" + timeList);
                        whichImage(desc, imageView3);
                        imageView3.getLayoutParams().height = 100;
                        imageView3.getLayoutParams().width = 100;
                    }
                    if(i == 3) {
                        textView4.setText("Low: " + min + "°F\nHigh: " + max + "°F\n" + desc + "\n" + timeList);
                        whichImage(desc, imageView4);
                        imageView4.getLayoutParams().height = 100;
                        imageView4.getLayoutParams().width = 100;
                    }
                    if(i == 4) {
                        textView5.setText("Low: " + min + "°F\nHigh: " + max + "°F\n" + desc + "\n" + timeList);
                        whichImage(desc, imageView5);
                        imageView5.getLayoutParams().height = 100;
                        imageView5.getLayoutParams().width = 100;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void whichImage(String weather, ImageView imageView) {
            if(weather.equals("Cloudy") || weather.equals("Clouds")) {
                imageView.setImageResource(R.drawable.cloudy);
            }
            if(weather.equals("Rain")) {
                imageView.setImageResource(R.drawable.rain);
            }
            if(weather.equals("Snow")) {
                imageView.setImageResource(R.drawable.snow);
            }
            if(weather.equals("Sunny")) {
                imageView.setImageResource(R.drawable.sunny);
            }
            if(weather.equals("Thunderstorm")) {
                imageView.setImageResource(R.drawable.thunderstorm);
            }
            if(weather.equals(("Clear"))) {
                imageView.setImageResource(R.drawable.clear);
            }
            if(imageView.equals(mainImageView)) {
                if(weather.equals("Cloudy") || weather.equals("Clouds")) {
                    quote.setText("My parents deserved justice, my heart is too cloudy with emotions");
                }
                if(weather.equals("Rain")) {
                    quote.setText("I won't kill you, but I don't have to give you a rain coat");
                }
                if(weather.equals("Snow")) {
                    quote.setText("It's not who I am underneath, but I need something to warm me up");
                }
                if(weather.equals("Sunny")) {
                    quote.setText("I'm Batman, and I feel too hot");
                }
                if(weather.equals("Thunderstorm")) {
                    quote.setText("Something elemental, something terrifying, the last thunder of the night");
                }
                if(weather.equals(("Clear"))) {
                    quote.setText("I'll be looking for you since I can finally see");
                }
            }
        }
    }
    /*
    //https://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    */
}
