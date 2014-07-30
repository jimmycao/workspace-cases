package com.pivotal.hackthon.cloudstorage.storage.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.common.collect.Lists;
import com.pivotal.hackthon.cloudstorage.storage.CloudFile;
import com.pivotal.hackthon.cloudstorage.storage.CloudStorage;

public class HDFSCloudStorageImpl implements CloudStorage {

	private static Log LOG = LogFactory.getLog(HDFSCloudStorageImpl.class);

	private FileSystem fs;

	public HDFSCloudStorageImpl() {
		try {
			Configuration conf = new Configuration();
			System.out.println("HDFS:" + conf.get("fs.defaultFS"));
			fs = FileSystem.get(new Configuration());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<CloudFile> list(String uid, String curPath) throws IOException {
		Path path = new Path("/user/" + uid + "/" + curPath);
		LOG.info("List path:" + path);
		FileStatus[] statues = fs.listStatus(path);
		List<CloudFile> cFiles = Lists.newArrayList();
		for (FileStatus s : statues) {
			CloudFile cFile = CloudFile.Builder.newBuilder()
					.setName(s.getPath().getName()).setSize(s.getLen())
					.isDir(s.isDirectory()).build();
			cFiles.add(cFile);
		}
		return cFiles;
	}

	@Override
	public void mkdir(String uid, String curPath, String newDir)
			throws IOException {
		fs.mkdirs(new Path("/user/" + uid + "/" + curPath + "/" + newDir));
	}

	@Override
	public InputStream open(String uid, String file) throws IOException {
		String path = "/user/" + uid + "/" + file;
		LOG.info("Open file:" + path);
		return fs.open(new Path(path));
	}

	@Override
	public CloudFile getCFile(String uid, String file) throws IOException {
		String path = "/user/" + uid + "/" + file;
		FileStatus s = fs.getFileStatus(new Path(path));
		CloudFile cFile = new CloudFile.Builder()
				.setName(s.getPath().toString()).setSize(s.getLen()).build();
		return cFile;

	}

	@Override
	public OutputStream create(String path) throws IOException {
		LOG.info("Create File:" + path);
		return fs.create(new Path(path));
	}
}
