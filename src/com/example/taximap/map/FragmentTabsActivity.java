package com.example.taximap.map;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;
import com.example.taximap.*;
import com.example.taximap.menu.Contact;
import com.example.taximap.menu.Help;

public class FragmentTabsActivity extends ActivityGroup {

	private AccountManager mAccountManager;
	private Intent filterIntent;
	private Intent helpIntent;
	private Intent contactIntent;
	public static TabHost tabHost = null;
	public static int currentTabIndex = 0;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) { // every time the
															// screen rotate it
															// will call this
															// func
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs);
		mAccountManager = AccountManager.get(this);
		if (tabHost == null) {
			// Wei put them into this if statement so they only created once.
			this.contactIntent = new Intent(this, Contact.class);
			this.helpIntent = new Intent(this, Help.class);
			this.filterIntent = new Intent(this, FilterActivity.class);
		}
		tabHost = (TabHost) findViewById(R.id.tabhost);

		tabHost.setup(this.getLocalActivityManager());
		// Adding all TabSpec to TabHost
		tabHost.addTab(tabHost.newTabSpec("Map").setIndicator("Map")
				.setContent(new Intent(this, MapViewActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("List").setIndicator("List")
				.setContent(new Intent(this, ListViewActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("Profile").setIndicator("Profile")
				.setContent(new Intent(this, ProfileViewActivity.class))); 
		tabHost.setCurrentTab(2);
		tabHost.setCurrentTab(currentTabIndex);
	}

	private void quitApplication() {
		new AlertDialog.Builder(this)
				.setTitle("Log Out")
				.setMessage("Log Out of Taxi Map?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Get all the accounts for this application on
								// this device
								try {
									Account[] accounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
									// There maybe more than one account, so the
									// last one created is used
									Account userAccount = accounts[accounts.length - 1];
									// set LOGOUT key to null in users Account so it
									// won't automatically log in
									mAccountManager.setUserData(userAccount,Constants.LOGOUT, "true");
									// Go back to log in screen
								} catch (Exception e) {}
								startActivity(new Intent(
										FragmentTabsActivity.this, Login.class));

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) { // menu/action bar
		switch (item.getItemId()) {
		case R.id.menu_filter:
			// Go to filter page
			startActivity(this.filterIntent);
			break;
		case R.id.menu_help:
			// go to help page
			startActivity(this.helpIntent);
			return true;
		case R.id.menu_contacts:
			// Go to contact page
			startActivity(this.contactIntent);
			return true;
		case R.id.menu_exit:
			quitApplication();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}