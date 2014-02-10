package com.opendatathe.entities;

import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.images.Image;

/**
 * Class to be used only in further version. 
 * @author Ismael Sarmento
 *
 */
@PersistenceCapable
public class FeaturedDataset {

	/** Reference for the featured dataset id */
	private Long datasetId;
	private Image image;
}
