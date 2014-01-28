package com.opendatathe.servlet;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Upload extends HttpServlet {
    
	private static final long serialVersionUID = -7476086808076899772L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("file");
        
        if (blobKey == null) {
        	res.getWriter().write("Nao foi possivel guardar o objeto.");
            res.sendRedirect("/");
        } else {
        	res.getWriter().write(blobKey.getKeyString());
            //res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
        }
    }
}
