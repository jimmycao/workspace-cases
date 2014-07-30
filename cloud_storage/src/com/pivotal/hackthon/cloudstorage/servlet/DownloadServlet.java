package com.pivotal.hackthon.cloudstorage.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends CloudServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uid = req.getParameter("uid");
		if (uid == null || uid.equals("")) {
			uid = "zhangj82";
		}
		String wd = req.getParameter("wd");
		String fileName = req.getParameter("fileName");
		String filePath = wd + "/" + fileName;

		if (filePath == null || filePath.equals("")) {
			throw new ServletException("File Name can't be null or empty");
		}
		ServletContext ctx = getServletContext();
		InputStream fis = storage.open(uid, filePath);
		String mimeType = ctx.getMimeType(filePath);
		resp.setContentType(mimeType != null ? mimeType
				: "application/octet-stream");
		resp.setContentLength((int) storage.getCFile(uid, filePath).getSize());
		resp.setHeader("Content-Disposition", "attachment; filename=\""
				+ filePath + "\"");
		ServletOutputStream os = resp.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read = 0;
		while ((read = fis.read(bufferData)) != -1) {
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
		System.out.println("File downloaded at client successfully");
	}
}
