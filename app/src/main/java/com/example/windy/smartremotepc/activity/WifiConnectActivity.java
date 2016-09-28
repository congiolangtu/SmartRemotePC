package com.example.windy.smartremotepc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.windy.smartremotepc.R;

import java.net.Socket;

import com.example.windy.smartremotepc.mode.CodeIntent;

/**
 * Created by Windy on 19/03/2015.
 */
public class WifiConnectActivity extends Activity {

    EditText editIP;
    EditText editPort;
    Button btConnect;
    Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect);
        editIP= (EditText) findViewById(R.id.editIPAddWC);
        editPort= (EditText) findViewById(R.id.editPortWC);
        btConnect= (Button) findViewById(R.id.btConnectWC);
        btBack= (Button) findViewById(R.id.btBackWC);
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okEvent();
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void okEvent(){
        Socket socket=null;
        if(socket==null){
            Intent intent=getIntent();
            intent.putExtra("ipAddress",editIP.getText()+"");
            intent.putExtra("port",Integer.parseInt(editPort.getText()+""));
            setResult(CodeIntent.RESULT_WIFI_CONNECT,intent);
            finish();
        }
    }
}
