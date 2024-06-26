package com.bassi.tmapp.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bassi.tmapp.service.PublishedTmService;


@RestController
@RequestMapping("/api/published")
public class PublishedTmResources {
	
	
	private PublishedTmService publishedTmService;
	
	PublishedTmResources(
			PublishedTmService publishedTmService
			) {
		this.publishedTmService  = publishedTmService;
	}

	
	
	
}
