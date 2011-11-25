package com.lowie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class BluetoothActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 0;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String server_addr_temp = "http://169.254.100.112";
    private String server_port_temp = "8000";
    private String Tag = "myBT";
    
    
    private Button scan = null;
    private EditText server_addr = null;
    private EditText server_port = null;
    private CheckBox bta_check = null;
    
    private void get_ui(){
    	this.scan = (Button) findViewById(R.id.scan);
    	this.server_addr = (EditText) findViewById(R.id.editText_server);
    	this.server_port = (EditText) findViewById(R.id.editText_port);
    	this.bta_check = (CheckBox) findViewById(R.id.checkBox_bta);
    }
    
    public void init(){
    	this.server_addr.setText(this.server_addr_temp);
    	this.server_port.setText(this.server_port_temp);
    	this.bta_check.setChecked(false);
    	this.scan.setEnabled(false);
    }
    
    public void check_bta(){
    	this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {    
        	System.out.println("Don't support buletooth.");
        	this.bta_check.setChecked(false);
        	this.scan.setClickable(false);
        }
        
        if (!mBluetoothAdapter.isEnabled()) {    
        	Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);    
        	startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        this.bta_check.setChecked(true);
        this.scan.setEnabled(true);
    }
    
    public void bt_scan_click(){
    	this.scan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(Tag, "bt scan button click");
				bt_scan();
			}
		});
    }
    
    public void bt_scan(){
    	Log.d(Tag, "bt scan start");
    	mBluetoothAdapter.startDiscovery();
    	final BroadcastReceiver mReceiver = new BroadcastReceiver() {   
        	EditText text = (EditText) findViewById(R.id.editText1);
        	ArrayList<String> result = new ArrayList<String>();
        	
        	public void onReceive(Context context, Intent intent) { 
        		int indicator = 0;
        		String action = intent.getAction();   
        		// When discovery finds a device        
        		if (BluetoothDevice.ACTION_FOUND.equals(action)) {            
        			// Get the BluetoothDevice object from the Intent            
        			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);            
        			String bt_addr = device.getAddress();
        			String bt_name = device.getName() ;
        			result.add(device.getAddress());
        			
        			String result_text = "** start **";
        			text.append(result_text);
        			Log.d(Tag, result_text);
        			
        			result_text = "Name: "+bt_name+ " , MAC : " +bt_addr+"\n";
        			text.append(result_text);
        			Log.d(Tag, result_text);
        			
        			server_connect(bt_name, bt_addr);
        			
        			result_text = "** end **";
        			text.append(result_text);
        			Log.d(Tag, result_text);
        		}  
        	}
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
    }
    
    
    public void server_connect(String bt_name, String bt_mac){
    	BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            String url_text = this.server_addr_temp+":"+this.server_port_temp+"/conntest/echo?name="+bt_name+"&mac="+bt_mac;
            URI url = new URI(url_text);
            /*
             * it looks like http params can not contain space, so we should check for a better encode way. 
             */
            Log.d(Tag, "URL_get: "+url.toString());
            request.setURI(url);
            HttpResponse response = client.execute(request);
            in = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            //String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            String page = sb.toString();
            /*
             *  You can decode it here with json as the result is in json object. 
             */
            Log.d(Tag, "HTTP_return:"+page);
        } catch (Exception e) {
        	Log.e(Tag,e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        System.out.println("OK");
        
        this.get_ui();
        this.init();
        this.check_bta();
        this.bt_scan_click();
    }
        
       
}