package com.example.aplicationfinder3;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity   {
	
	private PackageManager packageManager = null;
	private List<ApplicationInfo> applist = null;
	private AplicationFinderAdapter listadaptor = null;
	private EditText searchText;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        packageManager = getPackageManager();
        new LoadApplications().execute();
        searchText= (EditText) findViewById(R.id.search);
        searchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				listadaptor.getFilter().filter(s.toString());
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		boolean result = true;
		
		switch (item.getItemId()) {
		case R.id.menu_about:
			displayAboutDialog();
			
			break;

		default:
			result = super.onOptionsItemSelected(item); 
			break;
		}
		return result;
	}
	
	private void displayAboutDialog(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.about_title));
		builder.setMessage(getString(R.string.about_desc));
		
		
		builder.setPositiveButton("Know More", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javatechig.com"));
				startActivity(browserIntent);
				dialog.cancel();
		}
		});
		
		builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			dialog.cancel();
			}
			});
		builder.show();
			
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ApplicationInfo app = applist.get(position);
		try {
				Intent intent = packageManager
				.getLaunchIntentForPackage(app.packageName);
				
				if (null != intent) {
					startActivity(intent);
				}
		} catch (ActivityNotFoundException e) {
				Toast.makeText(MainActivity.this, e.getMessage(),
				Toast.LENGTH_LONG).show();
		} catch (Exception e) {
				Toast.makeText(MainActivity.this, e.getMessage(),
				Toast.LENGTH_LONG).show();
		}
	}
	
	
	private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list){
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		
		for (ApplicationInfo info : list) {
			try {
					if (null != packageManager
							.getLaunchIntentForPackage(info.packageName)) {
						
					applist.add(info);
			}
			} catch (Exception e) {
					e.printStackTrace();
			}
		}
		
		return applist;
	}
    private class LoadApplications extends AsyncTask<Void, Void, Void> {
    	private ProgressDialog progress = null;
    	
    	
    	@Override
    	protected Void doInBackground(Void... params) {
	    	applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
	    	listadaptor = new AplicationFinderAdapter(MainActivity.this,
	    	R.layout.apps_list, applist);
	    	return null;
    	}
    	
    
    	@Override
    	protected void onCancelled() {
    		super.onCancelled();
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		setListAdapter(listadaptor);
    		progress.dismiss();
    		super.onPostExecute(result);
    	}
    	@Override
    	protected void onPreExecute() {
    		progress = ProgressDialog.show(MainActivity.this, null,
    				"Loading application info...");
    		super.onPreExecute();
    	}
    	@Override
    	protected void onProgressUpdate(Void... values) {
	    	super.onProgressUpdate(values);
	    }
   	}
    /*
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void afterTextChanged(Editable s) {
		listadaptor.getFilter().filter(s.toString());
		
	}
	*/
}
