package com.testlogin;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUserId;
    EditText etPassword;
    EditText etComapny;
    Button btnLogin;
    TextView txtResponse;

    private String userId;
    private String password;
    private String company;
    private boolean loginStatus;
    private String responseStr;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserId = (EditText) findViewById(R.id.et_user_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        etComapny = (EditText) findViewById(R.id.et_company);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtResponse = (TextView) findViewById(R.id.txt_response);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userId = etUserId.getText().toString();
        password = etPassword.getText().toString();
        company = etComapny.getText().toString();

        LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
        loginAsyncTask.execute();
    }

    private void connectionGETestablish() {
        String dataUrl = "http://seamlesserp.somnetics.in/api_connect";
        //String dataUrlParameters = "request=login/auth&user_name="+userId+"&user_pass="+password+"&company="+company;
        String dataUrlParameters = "request=login/auth&user_name=poulami.bhose@somnetics.in&user_pass=Itsmee@321&company=58598da8-e4ae-4dd7-b36e-a1acca9ee88e";
        URL url;
        HttpURLConnection connection = null;
        try {
// Create connection
            url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
// Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();
// Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            responseStr = response.toString();
            Log.d("Server response",responseStr);
            JSONObject jsonObject = (new JSONObject(responseStr));
            code = jsonObject.getString("code");
            if (code.equals("100")) {
                loginStatus = false;
            } else if (code.equals("200")) {
                loginStatus = true;
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private class LoginAsyncTask extends AsyncTask {
        @Override
        protected Void doInBackground(Object[] params) {
            connectionGETestablish();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Toast.makeText(getApplicationContext(), code+"", Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), responseStr, Toast.LENGTH_LONG).show();
            if (loginStatus) {
                Toast.makeText(getApplicationContext(),"Successful..!!",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"UnSuccessful..!!",Toast.LENGTH_LONG).show();
            }
            txtResponse.setText(responseStr);
        }
    }
}
