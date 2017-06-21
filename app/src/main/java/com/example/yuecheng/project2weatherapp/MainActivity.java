package com.example.yuecheng.project2weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    String location = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edt = (EditText) findViewById(R.id.editText1);
        location = edt.getText().toString();

        ImageDownloadTask task1 =  new ImageDownloadTask();
        Bitmap result;
        task1.execute("http");
    }

    public void btnFunction(View view) {
        WeatherTask task = new WeatherTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+location+",us&appid=ccb397d609e61e09683ef081cdd29a22");
    }



    public class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String pageURL = strings[0];
            URL url = null;
            HttpURLConnection urlConnection = null;
            String result = "";

            try {
                //convert pageURL to URL
                url = new URL(pageURL);
                //open connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream stream = urlConnection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);

                int data = streamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = streamReader.read();
                }
            } catch (MalformedURLException o) {
                o.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            Double temp = 0.0;
            Double lon = 0.0;
            Double lat = 0.0;
            String weather="";
            Double speed = 0.0;
            Double deg = 0.0;

            try{
                JSONObject jsonObject = new JSONObject(result);
                //jsonObject.getJSONObject("main").getDouble("temp");

                temp = jsonObject.getJSONObject("main").getDouble("temp");
                lon = jsonObject.getJSONObject("coord").getDouble("lon");
                lat = jsonObject.getJSONObject("coord").getDouble("lat");
                weather = jsonObject.getJSONObject("weather").toString();
                speed = jsonObject.getJSONObject("wind").getDouble("lat");
                deg = jsonObject.getJSONObject("wind").getDouble("lat");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView txv1 = (TextView) findViewById(R.id.textView5);
            TextView txv2 = (TextView) findViewById(R.id.textView6);
            TextView txv3 = (TextView) findViewById(R.id.textView7);
            TextView txv4 = (TextView) findViewById(R.id.textView8);

            txv1.setText(Double.toString(lon) + Double.toString(lat));
            txv2.setText(Double.toString(temp));
            txv3.setText(weather);
            txv4.setText(Double.toString(speed) + Double.toString(deg));

            Log.i("lon: ", Double.toString(lon));
            Log.i("lat: ", Double.toString(lat));
            Log.i("temp: ", Double.toString(temp));
            Log.i("weather: ", weather);
            Log.i("speed: ", Double.toString(speed));
            Log.i("deg: ", Double.toString(deg));

            //Log.i("data: ", result);

        }
    }

    public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            Bitmap image = null;

            try {
                url = new URL(strings[0]);
                //open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                //connect
                urlConnection.connect();
                InputStream stream = urlConnection.getInputStream();
                image = BitmapFactory.decodeStream(stream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap image) {
            ImageView img = (ImageView)findViewById(R.id.imageView);
            img.setImageBitmap(image);

        }



    }




}
