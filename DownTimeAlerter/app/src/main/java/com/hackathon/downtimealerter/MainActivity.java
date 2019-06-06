package com.hackathon.downtimealerter;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity 
{
    EditText urlet;
    Button submitbt, submitss;
    TextView txtmsg;
    final Handler handler = new Handler();
    URL request_url = null;
    HttpURLConnection http_conn = null;
    private OkHttpClient mClient = new OkHttpClient();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        urlet = findViewById(R.id.urlet);
        submitbt = findViewById(R.id.submitbt);
        submitss = findViewById(R.id.submitss);
        txtmsg = findViewById(R.id.txtmsg);
        submitbt.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View v) 
			{
                String url = urlet.getText().toString();
                    backendRunning(url);
                    txtmsg.setVisibility(View.VISIBLE);
                    submitss.setVisibility(View.VISIBLE);
                    submitbt.setVisibility(View.GONE);
                    urlet.setVisibility(View.GONE);
            }
        });
        submitss.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                txtmsg.setVisibility(View.GONE);
                submitss.setVisibility(View.GONE);
                submitbt.setVisibility(View.VISIBLE);
                urlet.setVisibility(View.VISIBLE);
            }
        });
    }

    public void backendRunning(final String url)
    {
        Runnable runnable = new Runnable() 
		{
            @Override
            public void run() 
			{
                try
				{
                    Thread thread = new Thread(new Runnable() 
					{
                        @Override
                        public void run() 
						{
                            HttpURLConnection.setFollowRedirects(true);
                            try 
							{
                                request_url = new URL(url);
                                http_conn = (HttpURLConnection) request_url.openConnection();
                                http_conn.setConnectTimeout(100000);
                                http_conn.setReadTimeout(100000);
                                http_conn.setInstanceFollowRedirects(true);
                                String responseURL = String.valueOf(http_conn.getResponseCode());
                                if (!responseURL.equals("200"))
                                {
                                    post("http://2cbXXXXX.ngrok.io/",responseURL, new  Callback()
									{
                                        @Override
                                        public void onFailure(Call call, IOException e) 
										{
                                            e.printStackTrace();
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response)
										{
                                            runOnUiThread(new Runnable() 
											{
                                                @Override
                                                public void run() 
												{
                                                    Toast.makeText(getApplicationContext(),"SMS Sent! ",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                                else
                                {
                                    Log.e("GetStatus",String.valueOf(http_conn.getResponseCode()));
                                }
                            }
                            catch (Exception e) 
							{
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
                catch (Exception e) 
				{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                finally
				{
                    handler.postDelayed(this, 60000);
                }
            }
        };
        handler.post(runnable);
    }
    Call post(String url, String rescode,Callback callback) 
	{
        RequestBody formBody = new FormBody.Builder()
                .add("To", "+9198XXXXXXXX")
                .add("Body", "Response Code for URL is "+rescode)
                .build();
        Request request = new Request.Builder()
                .header("Accept", "text/html, application/xhtml+xml, application/xml;q=0.9, */*;q=0.8")
                .url(url)
                .post(formBody)
                .build();
        Call response = mClient.newCall(request);
        response.enqueue(callback);
        return response;
    }
}
