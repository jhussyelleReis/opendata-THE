package com.opendatathe.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.FinalizationException;
import com.google.appengine.api.files.LockException;
import com.google.gson.Gson;
import com.opendatathe.entities.Dataset;
import com.opendatathe.utils.JDOUtils;
import com.opendatathe.utils.TransformInputTypes;

public class ContributionServlet extends HttpServlet {

	private static final long serialVersionUID = -8191660237662902018L;
	final Logger logger = LoggerFactory.getLogger(ContributionServlet.class);
	/** the name of the parameter in the request which contains the content of the file */
	private static final String FILE = "file";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Dataset dataset = new Dataset();
		try{
	        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
	        for (FileItem item : items) {
	        	try {
		            if (item.isFormField()) {
		                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
		                String fieldname = item.getFieldName();
		                Object fieldvalue = item.getString();
		                Field field = Dataset.class.getDeclaredField(fieldname);
		                field.setAccessible(true);
		                if(field.getType().equals(Boolean.class)){
		                	if(fieldvalue.equals("on")){
		                		fieldvalue = true;
		                	}else {
		                		fieldvalue = false;
		                	}
		                }
		                field.set(dataset, fieldvalue);//FIXME create transformer, search for library
		            } else {
		                // Process form file field (input type="file").
		                String filename = FilenameUtils.getName(item.getName());
		                if(item.getFieldName().equals(FILE )){
		                	dataset.setBlobKey(sendData(item.getInputStream()).getKeyString());
		                }
		            }
	        	}
	        	catch (NoSuchFieldException e) {
	        		logger.warn(e.getMessage());
	        	} catch (SecurityException e) {
	        		e.printStackTrace();
	        	} catch (IllegalArgumentException e) {
	        		e.printStackTrace();
	        	} catch (IllegalAccessException e) {
	        		e.printStackTrace();
	        	}
	        }
		} catch (FileUploadException e) {
    		throw new ServletException("Cannot parse multipart request.", e);
    	}
		
		new JDOUtils().save(dataset);
		resp.getWriter().write(new Gson().toJson(dataset));
	}

	private BlobKey sendData(InputStream inputStream) {
		// Get a file service
		FileService fileService = FileServiceFactory.getFileService();
		BlobKey blobKey = null;
		try {
			// Create a new Blob file with mime-type "text/plain"
			AppEngineFile file = fileService.createNewBlobFile("text/csv");//TODO check for type first
	
			// Open a channel to write to it
			boolean lock = false;
			FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
	
			// Different standard Java ways of writing to the channel
			// are possible. Here we use a PrintWriter:
			PrintWriter writer = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
			writer.print(TransformInputTypes.inputStreamToString(inputStream));	
			// Close without finalizing and save the file path for writing later
			writer.close();
			String path = file.getFullPath();
	
			// Write more to the file in a separate request:
			file = new AppEngineFile(path);
	
			// This time lock because we intend to finalize
			lock = true;
			writeChannel = fileService.openWriteChannel(file, lock);
			
			// Now finalize
			writeChannel.closeFinally();
			// Now read from the file using the Blobstore API
			blobKey = fileService.getBlobKey(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FinalizationException e) {
			e.printStackTrace();
		} catch (LockException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return blobKey;
	}

}
