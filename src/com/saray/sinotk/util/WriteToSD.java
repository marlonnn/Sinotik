package com.saray.sinotk.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * ��assets�ļ����µ����ݿ�д��SD����
 * @author ���� 2015-10-11
 *
 */
public class WriteToSD {
	private Context context;
	String filePath = null;
	public WriteToSD(Context context){
		this.context = context;
		filePath = context.getFilesDir() + "/weather";
		if(!isExist()){
			write();
		}
	}
	
	private void write(){
		InputStream inputStream;
		try {
			inputStream = context.getResources().getAssets().open("cityCode.db");
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/cityCode.db");
			byte[] buffer = new byte[512];
			int count = 0;
			while((count = inputStream.read(buffer)) > 0){
				fileOutputStream.write(buffer, 0 ,count);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			inputStream.close();
			System.out.println("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private boolean isExist(){
		File file = new File(filePath + "/cityCode.db");
		if(file.exists()){
			return true;
		}else{
			return false;
		}
	}
}
