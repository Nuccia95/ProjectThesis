package com.demo.thesisbackend.managers;

import java.util.List;

import shared.thesiscommon.bean.Resource;

public interface ResourceManager {

	public Resource createResource(Resource res);
	
	public void deleteResource(Resource res);
	
	public List<String> getResourcesNames();

	public List<Resource> getAllResources();
}