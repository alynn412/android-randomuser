package my.edu.utem.randomuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView nameTextView, emailTextView, addressTextView, phoneTextView, dobTextView;
    ImageView imageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        dobTextView = findViewById(R.id.dobTextView);
        imageView = findViewById(R.id.pictureTextView);
        progressBar = findViewById(R.id.pbLoading);
    }

    public void getUser(View view) {

        // Instantiate the RequestQueue.
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://randomuser.me/api";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject personObject = jsonObject.getJSONArray("results").getJSONObject(0);
                            String first = personObject.getJSONObject("name").getString("first");
                            String last = personObject.getJSONObject("name").getString("last");
                            String title = personObject.getJSONObject("name").getString("title");
                            nameTextView.setText("Name: " + title + " " + first + " " + last);

                            String email = personObject.getString("email");
                            emailTextView.setText(". Email addr: " + email);

                            String street = personObject.getJSONObject("location").getString("street");
                            String city = personObject.getJSONObject("location").getString("city");
                            String state = personObject.getJSONObject("location").getString("state");
                            addressTextView.setText(". Home addr: " + street + ", " + city + ", " + state);

                            String phone = personObject.getString("phone");
                            phoneTextView.setText(". Phone: " + phone);

                            String dob = personObject.getJSONObject("dob").getString("date");
                            emailTextView.setText(". DOB: " + dob);

                            String picURL = personObject.getJSONObject("picture").getString("large");
                            Glide.with(MainActivity.this).load(picURL).into(imageView);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                nameTextView.setText("That didn't work!");
                progressBar.setVisibility(View.GONE);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
