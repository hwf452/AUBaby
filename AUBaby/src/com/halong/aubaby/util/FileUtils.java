package com.halong.aubaby.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	public static byte[] toByteArray(InputStream inputStream) throws IOException{
		
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		byte[] buffer =new byte[4096];
		int n=0;
		while((n=inputStream.read(buffer))!=-1){
			byteArrayOutputStream.write(buffer,0,n);
		}
		
		return byteArrayOutputStream.toByteArray();
	}
	
	

}
