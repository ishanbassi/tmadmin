package com.bassi.tmapp.service;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class CommonUtilService {
	CommonUtilService() {
		
	}
	
	
	
	public static Stream<Object> flatten(Object[] array) {
        return Arrays.stream(array)
            .flatMap(o -> o instanceof Object[] a? flatten(a): Stream.of(o));
    }

	
}
