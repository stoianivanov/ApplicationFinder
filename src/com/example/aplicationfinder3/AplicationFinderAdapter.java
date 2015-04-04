package com.example.aplicationfinder3;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class AplicationFinderAdapter extends ArrayAdapter<ApplicationInfo> implements Filterable{

	
	private List<ApplicationInfo> appsList = null;
	private Context context;
	private PackageManager packageManager;
	private List<ApplicationInfo> apps=null;
	public AplicationFinderAdapter(Context context, int ID,
			List<ApplicationInfo> apps) {
		super(context, ID, apps);
		this.context=context;
		this.appsList=apps;
		this.apps= apps;
		packageManager= context.getPackageManager();
	}
	
	private class ApplicationFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			final List<ApplicationInfo> filteredApplication=
						new ArrayList<ApplicationInfo>(appsList.size());
			Pattern pattern = Pattern
					.compile(constraint.toString(), Pattern.CASE_INSENSITIVE);
			for (ApplicationInfo applicationInfo : appsList) {
				if(pattern.matcher(applicationInfo.packageName).find()){
					Log.i("tag","add app");
					filteredApplication.add(applicationInfo);
				}
				
			}
			final FilterResults result = new FilterResults();
			result.values=filteredApplication;
			result.count= filteredApplication.size();
			return result;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			apps.clear();
			apps.addAll((List<ApplicationInfo>)results.values);
			Log.i("up", "LISt View Update");
			notifyDataSetChanged();
		}
		
	}
	
	@Override
	public int getCount() {

		return ((null != appsList) ? appsList.size() : 0);
	}
	
	@Override
	public ApplicationInfo getItem(int position) {
		return ((null != appsList) ? appsList.get(position) : null);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=convertView;
		if (null == view) {
			LayoutInflater layoutInflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.apps_list, null);
			}
		ApplicationInfo data = appsList.get(position);
		if (null != data) {
			TextView appName = (TextView) view
					.findViewById(R.id.app_name);
			TextView packageName = (TextView) view
					.findViewById(R.id.app_paackage);
			ImageView iconview = (ImageView) view
					.findViewById(R.id.app_icon);
			
			appName.setText(data.loadLabel(packageManager));
			packageName.setText(data.packageName);
			
			iconview.setImageDrawable(data.loadIcon(packageManager));
		}
		
		return view;
	}
	@Override
	public  Filter getFilter() {
		return new ApplicationFilter();
	}
}
