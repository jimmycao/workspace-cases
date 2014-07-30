package com.pivotal.hackthon.cloudstorage.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pivotal.hackthon.cloudstorage.storage.CloudFile;

public class ListServlet extends CloudServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userId = req.getParameter("uid");
		if (userId == null) {
			userId = "zhangj82";
		}
		String wd = req.getParameter("wd");
		if (wd == null) {
			wd = "/";
		}

		List<CloudFile> cFiles = storage.list(userId, wd);
		req.setAttribute("files", cFiles);
		req.setAttribute("wd", wd);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
