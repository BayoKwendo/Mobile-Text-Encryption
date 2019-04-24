package com.e.encryptions;

/***
 *    Application Name : MessageBox 
 *    Author : Vimal Rughani
 *    Website : http://pulse7.net
 *    For more details visit http://pulse7.net/android/read-sms-message-inbox-sent-draft-android/
 */
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_SMS;

public class MessageBox extends Activity implements OnClickListener {

	// GUI Widget
	Button btnSent, btnInbox, btnDraft, btnNew;
	TextView lblMsg, lblNo, textView;
	private static final int PERMISSION_REQUEST_CODE = 200;

	ListView lvMsg;
	LinearLayoutCompat layout;
    boolean doubleBackToExitPressedOnce = false;

	// Cursor Adapter
	SimpleCursorAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagebox);

		// Init GUI Widget
		btnInbox = (Button) findViewById(R.id.btnInbox);
		btnNew = findViewById(R.id.btnNew);
		btnNew.setOnClickListener(this);

		btnInbox.setOnClickListener(this);
		requestPermission();

		textView= findViewById(R.id.textView);

		btnSent = (Button) findViewById(R.id.btnSentBox);
		btnSent.setOnClickListener(this);

		btnDraft = (Button) findViewById(R.id.btnDraft);
		btnDraft.setOnClickListener(this);

		layout =findViewById(R.id.layout);
		lvMsg = (ListView) findViewById(R.id.lvMsg);


	}

	@Override
	public void onClick(View v) {

        if (v == btnNew){
			String txt = "";
			Intent i = new Intent( getApplicationContext(), MainActivity.class);
			i.putExtra("new_variable_name", txt);
			startActivity(i);
		}
		if (v == btnInbox) {
			layout.setVisibility(View.GONE);

			// Create Inbox box URI
			Uri inboxURI = Uri.parse("content://sms/inbox");

			// List required columns
			String[] reqCols = new String[] { "_id", "address", "body" };

			// Get Content Resolver object, which will deal with Content
			// Provider
			ContentResolver cr = getContentResolver();

			// Fetch Inbox SMS Message from Built-in Content Provider
			Cursor c = cr.query(inboxURI, reqCols, null, null, null);

			// Attached Cursor with adapter and display in listview
			adapter = new SimpleCursorAdapter(this, R.layout.row, c,
					new String[] { "body", "address" }, new int[] {
							R.id.lblMsg, R.id.lblNumber });
			lvMsg.setAdapter(adapter);
			lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override



				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



					TextView textView = view.findViewById(R.id.lblMsg);
                    String txt= textView.getText().toString();
					Intent i = new Intent( getApplicationContext(), MainActivity.class);
					i.putExtra("new_variable_name", txt);
					startActivity(i);
				}
			});

		}

		if (v == btnSent) {
			layout.setVisibility(View.GONE);

			// Create Sent box URI
			Uri sentURI = Uri.parse("content://sms/sent");

			// List required columns
			String[] reqCols = new String[] { "_id", "address", "body" };

			// Get Content Resolver object, which will deal with Content
			// Provider
			ContentResolver cr = getContentResolver();

			// Fetch Sent SMS Message from Built-in Content Provider
			Cursor c = cr.query(sentURI, reqCols, null, null, null);

			// Attached Cursor with adapter and display in listview
			adapter = new SimpleCursorAdapter(this, R.layout.row, c,
					new String[] { "body", "address" }, new int[] {
							R.id.lblMsg, R.id.lblNumber });
			lvMsg.setAdapter(adapter);

		}

		if (v == btnDraft) {
			layout.setVisibility(View.GONE);
			// Create Draft box URI
			Uri draftURI = Uri.parse("content://sms/draft");

			// List required columns
			String[] reqCols = new String[] { "_id", "address", "body" };

			// Get Content Resolver object, which will deal with Content
			// Provider
			ContentResolver cr = getContentResolver();

			// Fetch Sent SMS Message from Built-in Content Provider
			Cursor c = cr.query(draftURI, reqCols, null, null, null);

			// Attached Cursor with adapter and display in listview
			adapter = new SimpleCursorAdapter(this, R.layout.row, c,
					new String[] { "body", "address" }, new int[] {
							R.id.lblMsg, R.id.lblNumber });
			lvMsg.setAdapter(adapter);

			if (cr==null){
				textView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            }

		}

	}

    @Override
    public void onBackPressed() {

		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		} else {
			this.doubleBackToExitPressedOnce = true;
			layout.setVisibility(View.VISIBLE);
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

		}

        }
	private void requestPermission() {

		ActivityCompat.requestPermissions(this, new String[]{READ_SMS}, PERMISSION_REQUEST_CODE);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE:
				if (grantResults.length > 0) {
					boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
					if (locationAccepted) {

						Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

					}
					else {
						Toast.makeText(this, "Permission Denied, Now you can Send text", Toast.LENGTH_SHORT).show();

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							if (shouldShowRequestPermissionRationale(READ_SMS)) {
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										requestPermissions(new String[]{READ_SMS},
												PERMISSION_REQUEST_CODE);
										recreate();
									}
								};
								return;
							}
						}

					}
				}


				break;
		}
	}
}
