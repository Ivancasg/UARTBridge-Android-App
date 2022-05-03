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
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import java.io.IOException;
//import java.io.InputStream;
//import java.security.InvalidParameterException;

public class MainMenu extends SerialPortActivity {

    EditText config_serialPort;
    EditText config_baudrate;

    EditText mReception_km;

    //InputStream mInputStream;
    //ReadThread mReadThread;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        config_serialPort = (EditText) findViewById(R.id.editText_serial);
        config_baudrate = (EditText) findViewById(R.id.editText_baudrate);

        mReception_km = (EditText) findViewById(R.id.editText_km);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        config_serialPort.setText(sp.getString("DEVICE", ""));
        config_baudrate.setText(sp.getString("BAUDRATE", "-1"));

        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                config_serialPort.setText(sp.getString("DEVICE", "-1"));
                config_baudrate.setText(sp.getString("BAUDRATE", "-1"));

//                mApplication = (Application) getApplication();
//                try {
//                    mSerialPort = mApplication.getSerialPort();
//                    mOutputStream = mSerialPort.getOutputStream();
//                    mInputStream = mSerialPort.getInputStream();
//
//                    /* Create a receiving thread */
//                    mReadThread = new ReadThread();
//                    mReadThread.start();
//                } catch (SecurityException e) {
//                    DisplayError(R.string.error_security);
//                } catch (IOException e) {
//                    DisplayError(R.string.error_unknown);
//                } catch (InvalidParameterException e) {
//                    DisplayError(R.string.error_configuration);
//                }
            }
        });

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

                if (mReception_km != null) {
                    mReception_km.setText(bufferStrings);
                }
            }
        });
    }

}
