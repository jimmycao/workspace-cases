package com.pivotal.hackthon.cloudstorage.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UploadServlet extends CloudServlet {

	private static Log LOG = LogFactory.getLog(UploadServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		FileItemFactory fileItemFactory = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(
				fileItemFactory);
		try {
			List<FileItem> fileItems = servletFileUpload.parseRequest(req);
			Map<String, String> formFields = extractFormFields(fileItems);
			LOG.info("Form Fields:");
			for (Map.Entry<String, String> entry : formFields.entrySet()) {
				LOG.info(entry.getKey() + "=" + entry.getValue());
			}
			String uid = formFields.get("uid");
			String wd = formFields.get("wd");
			if (uid == null) {
				uid = "zhangj82";
			}
			if (wd == null) {
				wd = "";
			}
			Iterator<FileItem> iterator = fileItems.iterator();
			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();
				boolean formFied = fileItem.isFormField();
				if (formFied) {
					LOG.info("formField");
				} else {
					String fileName = "/user/" + uid + "/" + wd + "/"
							+ fileItem.getName();
					OutputStream outputStream = storage.create(fileName);
					InputStream inputStream = fileItem.getInputStream();
					int readBytes = 0;
					byte[] buffer = new byte[10000];
					while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
						outputStream.write(buffer, 0, readBytes);
					}
					outputStream.close();
					inputStream.close();
				}
			}

			resp.sendRedirect("list?wd=" + wd);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Map<String, String> extractFormFields(List<FileItem> fileItems) {
		Map<String, String> map = new HashMap<String, String>();
		for (FileItem item : fileItems) {
			if (item.isFormField()) {
				map.put(item.getFieldName(), item.getString());
			}
		}
		return map;
	}

}
