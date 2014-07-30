package com.pivotal.hackthon.cloudstorage.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface CloudStorage {

	public List<CloudFile> list(String uid, String curPath) throws IOException;

	public void mkdir(String uid, String curPath, String newDir)
			throws IOException;

	public InputStream open(String uid, String file) throws IOException;

	public CloudFile getCFile(String uid, String file) throws IOException;

	public OutputStream create(String path) throws IOException;
}
