package com.opendatathe.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.gson.Gson;
import com.opendatathe.entities.Dataset;
import com.opendatathe.utils.JDOUtils;
import com.opendatathe.utils.TransformInputTypes;

public class DatasetServlet extends HttpServlet{

	private static final long serialVersionUID = -6319353783561802269L;
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		JDOUtils jdo = new JDOUtils();
		String data = req.getParameter("data");
		Gson gson = new Gson();
		Dataset dataset = gson.fromJson(data, Dataset.class);
		Dataset result = jdo.findById(Dataset.class, dataset.getId());
		
		FileService fileService = FileServiceFactory.getFileService();
		// Later, read from the file using the Files API
		boolean lock = false; // Let other people read at the same time
		AppEngineFile blobFile = fileService.getBlobFile(new BlobKey(result.getBlobKey()));
		FileReadChannel readChannel = fileService.openReadChannel(blobFile, false);
		resp.getWriter().write(TransformInputTypes.inputStreamToString(Channels.newInputStream(readChannel)));
	}

}
