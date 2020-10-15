package com.demo.frontend.clientservices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import shared.thesiscommon.bean.Resource;

@Service
public class ResourceService {

	@Autowired
	private RestTemplate restTemplate;
	private static final String URL = "http://localhost:9999/";

	public Resource createResource(Resource res) {
		HttpEntity<Resource> request = new HttpEntity<>(res);
		return restTemplate.postForObject(URL + "createResource", request, Resource.class);
	}

	public void deleteResource(Resource res) {
		HttpEntity<Resource> request = new HttpEntity<>(res);
		restTemplate.postForObject(URL + "deleteResource", request, Resource.class);
	}

	public Resource updateResource(Resource res) {
		HttpEntity<Resource> request = new HttpEntity<>(res);
		return restTemplate.postForObject(URL + "updateResource", request, Resource.class);
	}
	
	public List<Resource> getAll(){
		List<Resource> resources = new ArrayList<>();
		Resource[] res = restTemplate.getForObject(URL + "getAllResources", Resource[].class);
		for (Resource r: res)
			resources.add(r);
		return resources;
	}

	public List<String> getResourcesNames() {
		List<String> resources = new ArrayList<>();
		String[] res = restTemplate.getForObject(URL + "getResourcesNames", String[].class);
		for (String resName : res)
			resources.add(resName);
		return resources;
	}

}
