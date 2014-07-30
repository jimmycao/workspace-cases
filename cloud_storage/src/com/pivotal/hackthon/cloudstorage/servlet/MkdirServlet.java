package com.pivotal.hackthon.cloudstorage.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MkdirServlet extends CloudServlet {

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uid = req.getParameter("uid");
		String path = req.getParameter("path");
		String newDir = req.getParameter("newDir");
		storage.mkdir(uid, path, newDir);
	}
}
