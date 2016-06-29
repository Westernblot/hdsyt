package com.grid.hdsyt.ui.main;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grid.hdsyt.R;

public class MainGridAdapter extends BaseAdapter{

	Context context;
	List<Map<String,Object>> mpList;
	
	public MainGridAdapter(Context context,List<Map<String,Object>> mpList){
		this.context=context;
		this.mpList=mpList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mpList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		TextView textView = new TextView(context);
//		String name = (String) mpList.get(position).get("name");
//		textView.setTextSize(22);
//		textView.setText(name);
		
		View view;
		if (convertView == null) {
			view = View.inflate(context, R.layout.grid_fenlei, null);
		}else{
			view = convertView;
		}

		TextView tv_single_name = (TextView) view
				.findViewById(R.id.tv_single_name);
		TextView tv_single_price = (TextView) view
				.findViewById(R.id.tv_single_price);
		String name = (String) mpList.get(position).get("name");
		String price = (String) mpList.get(position).get("price");
		tv_single_name.setText(name);
		if(!TextUtils.isEmpty(price)){
		    tv_single_price.setText("￥"+price);
		}else{
			tv_single_price.setText("");
		}
		return view;
	}
	
	
	//ui线程
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};

}
