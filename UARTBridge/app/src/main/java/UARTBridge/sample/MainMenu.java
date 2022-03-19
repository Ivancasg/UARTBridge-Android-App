/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package UARTBridge.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainMenu extends SerialPortActivity {

    EditText config_serialPort;
    EditText config_baudrate;

    EditText mReception_rate;
    EditText mReception_km;
    EditText mReception_k;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        config_serialPort = (EditText) findViewById(R.id.editText_serial);
        config_baudrate = (EditText) findViewById(R.id.editText_baudrate);

        mReception_rate = (EditText) findViewById(R.id.editText_rate);
        mReception_km = (EditText) findViewById(R.id.editText_km);
        mReception_k = (EditText) findViewById(R.id.editText_k);

        SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
        config_serialPort.setText(sp.getString("DEVICE", ""));
        config_baudrate.setText(sp.getString("BAUDRATE", "-1"));

        final Button buttonSetup = (Button)findViewById(R.id.ButtonSetup);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainMenu.this, SerialPortPreferences.class));
			}
		});
    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {

        runOnUiThread(new Runnable() {
            public void run() {
                String bufferStrings = new String(buffer, 0, size);
                if (bufferStrings.length() >= 15) {//%$6.25%099%0.48
                    String[] parsedStrings = bufferStrings.split("%");

                    if (mReception_rate != null && parsedStrings.length > 1) {
                        mReception_rate.setText(parsedStrings[1]);
                    }
                    if (mReception_km != null && parsedStrings.length > 2) {
                        mReception_km.setText(parsedStrings[2]);
                    }
                    if (mReception_k != null && parsedStrings.length > 3) {
                        mReception_k.setText(parsedStrings[3]);
                    }
                }
            }
        });
    }

}
