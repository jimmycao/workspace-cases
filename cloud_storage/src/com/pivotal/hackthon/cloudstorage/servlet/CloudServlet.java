package com.pivotal.hackthon.cloudstorage.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.pivotal.hackthon.cloudstorage.storage.CloudStorage;
import com.pivotal.hackthon.cloudstorage.storage.impl.HDFSCloudStorageImpl;

public class CloudServlet extends HttpServlet {

	CloudStorage storage;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		storage = new HDFSCloudStorageImpl();
	}
}
