package com.grid.hdsyt.ui.setting;

import com.grid.hdsyt.R;
import com.grid.hdsyt.utils.SPUtils;
import com.grid.hdsyt.utils.T;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class PrintSettingActivity extends Activity implements OnClickListener {

	private TextView tv_btn_back;
	private TextView tv_btn_save;
	private EditText et_ip;
	private EditText et_port;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_setting);
		
		tv_btn_back = (TextView) findViewById(R.id.tv_btn_back);
		tv_btn_save = (TextView) findViewById(R.id.tv_btn_save);
		et_ip = (EditText) findViewById(R.id.et_ip);
		et_port = (EditText) findViewById(R.id.et_port);
		
		tv_btn_back.setOnClickListener(this);
		tv_btn_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_btn_back:
			this.finish();
			break;
		case R.id.tv_btn_save:
			
			String ip = et_ip.getText().toString().trim();
			String port = et_port.getText().toString().trim();
			
			if(TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)){
				T.showShort(this, "Ip地址或端口号都不能为空！");
				return;
			}
			SPUtils.put(this, "ip", ip);
			SPUtils.put(this, "port", port);
			
			T.showShort(this, "保存成功!");
			
			break;

		default:
			break;
		}
	}

	
}
