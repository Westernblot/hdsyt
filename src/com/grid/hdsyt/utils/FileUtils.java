package com.grid.hdsyt.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * 文件操作 辅助类
 * @author PC
 *
 */
public class FileUtils {

	/**
	 * //path 把syt.db这个数据库拷贝到data/data/《包名》/files/syt.db
	 */
	public static void copyDB(Context context) {
		//只要你拷贝了一次，我就不要你再拷贝了
		try {
			File file = new File(context.getFilesDir(), "syt.db");
			if (file.exists() && file.length() > 0) {
				//正常了，就不需要拷贝了
				System.out.println("已经存在数据库，无需拷贝了！");
			} else {
				System.out.println("准备拷贝数据库!");
				InputStream is = context.getAssets().open("syt.db");

				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
