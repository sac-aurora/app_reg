package aurora.teamcop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {

    /*stringRequest code was guided through online resources
    * https://www.simplifiedcoding.net/android-studio-volley-tutorial-to-create-a-login-application/
    * https://developer.android.com/training/volley/simple.html*/

    //various parameters for the hashmap of the post request
    private static final String server_url = "http://agni.iitd.ernet.in/cop290/assign0/register/";
    public static final String Key_name_team = "teamname";
    public static final String Key_number_1 = "entry1";
    public static final String Key_name_1 = "name1";
    public static final String Key_number_2 = "entry2";
    public static final String Key_name_2 = "name2";
    public static final String Key_number_3 = "entry3";
    public static final String Key_name_3 = "name3";



    private Button registerButton;
    private EditText eT_name_team;
    private EditText eT_name_1;
    private EditText eT_name_2;
    private EditText eT_name_3;
    private EditText eT_number_1;
    private EditText eT_number_2;
    private EditText eT_number_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        eT_name_team = (EditText) findViewById(R.id.name_team);
        eT_name_1 = (EditText) findViewById(R.id.name_1);
        eT_name_2 = (EditText) findViewById(R.id.name_2);
        eT_name_3 = (EditText) findViewById(R.id.name_3);
        eT_number_1 = (EditText) findViewById(R.id.number_1);
        eT_number_2 = (EditText) findViewById(R.id.number_2);
        eT_number_3 = (EditText) findViewById(R.id.number_3);

        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
    }
        //Function performing the actual data transfer with the server sending the POST request
    private void registerTeam() {
        final String name_team = eT_name_team.getText().toString().trim();
        final String name_1 = eT_name_1.getText().toString().trim();
        final String name_2 = eT_name_2.getText().toString().trim();
        final String name_3 = eT_name_3.getText().toString().trim();
        final String number_1 = eT_number_1.getText().toString().trim();
        final String number_2 = eT_number_2.getText().toString().trim();
        final String number_3 = eT_number_3.getText().toString().trim();

        //use of regular expressions for input validation
        Pattern entry_num = Pattern.compile("^201[0-4][A-Za-z]{2}[1-7][0][0-9]{3}$");
        Pattern name = Pattern.compile("^[A-Za-z][a-z ]*$");
        Pattern name3 = Pattern.compile("^$");
        Pattern num3 = Pattern.compile("^$");
        Pattern team_name = Pattern.compile("^$");
        Matcher nameofteam = team_name.matcher(name_team);
        Matcher first_name = name.matcher(name_1);
        Matcher second_name = name.matcher(name_2);
        Matcher third_name = name.matcher(name_3);
        Matcher first_num = entry_num.matcher(number_1);
        Matcher second_num = entry_num.matcher(number_2);
        Matcher third_num = entry_num.matcher(number_3);
        Matcher name3m = name3.matcher(name_3);
        Matcher num3m = num3.matcher(number_3);

        //generating a toast for an incorrect entry
        if (nameofteam.matches()) {
            Toast.makeText(Login.this, "Incorrect team name", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!first_name.matches()) {
            Toast.makeText(Login.this, "Incorrect name of member 1", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!first_num.matches()) {
            Toast.makeText(Login.this, "Incorrect entry number of member 1", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!second_name.matches()) {
            Toast.makeText(Login.this, "Incorrect name of member 2", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!second_num.matches()) {
            Toast.makeText(Login.this, "Incorrect entry number of member 2", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!(third_name.matches() & third_num.matches()) & (!(name3m.matches() & num3m.matches()))) {
            Toast.makeText(Login.this, "Incorrect fields for member 3", Toast.LENGTH_LONG).show();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Key_name_team,name_team);
                params.put(Key_number_1,number_1);
                params.put(Key_name_1,name_1);
                params.put(Key_number_2, number_2);
                params.put(Key_name_2, name_2);
                params.put(Key_number_3, number_3);
                params.put(Key_name_3, name_3);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //getting the response and taking the required action
    private void showResponse(String json) {
        Intent intentS = new Intent(this, SuccessDisplay.class);
        Intent intentF = new Intent(this, FailureDisplay.class);
        ParseJSON result = new ParseJSON(json);
        result.parseJSON();
        if(result.response_success == 1) startActivity(intentS);
        if(result.response_success == 0) {
            System.out.println(result.response_message);
            startActivity(intentF);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == registerButton) {
            registerTeam();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
