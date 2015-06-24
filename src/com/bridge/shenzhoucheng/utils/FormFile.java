package com.bridge.shenzhoucheng.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;

public class FormFile {

	/* 上传文件的数据 */
	private byte[] data;
	private InputStream inStream;
	private File file;
	/* 文件名称 */
	private String filename;
	/* 请求参数名称 */
	private String parameterName;
	/* 内容类型 */
	private String contentType = "application/octet-stream"; 
	/* 文件路径 */ 

	/* 单一路径 */
	public FormFile(String filePath) {
		this.filename = filePath.substring(filePath.lastIndexOf("/") + 1);// 518caa505512d.JPG
		this.parameterName = filename.substring(0, filename.lastIndexOf("."));// 518caa505512d
		this.file = new File(filePath);
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (contentType != null)
			this.contentType = contentType;
	}

	/* 单一路径 */
	public FormFile(Context mContext, String filePath) {
		this.filename = filePath.substring(filePath.lastIndexOf("/") + 1);// 518caa505512d.JPG
		this.parameterName = filePath.substring(filePath.lastIndexOf("/") + 1);
		File file = new File(filePath);
		this.file = file;
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.contentType = contentType;
	}

	public FormFile(String filename, byte[] data, String parameterName,
			String contentType) {
		this.data = data;
		this.filename = filename;
		this.parameterName = parameterName;
		if (contentType != null)
			this.contentType = contentType;
	}
	

	public FormFile(String filename, File file, String parameterName,
			String contentType) {
		this.filename = filename;
		this.parameterName = parameterName;
		this.file = file;
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (contentType != null)
			this.contentType = contentType;
	}

	public File getFile() {
		return file;
	}

	public InputStream getInStream() {
		return inStream;
	}

	public byte[] getData() {
		return data;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
