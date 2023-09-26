package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.importer.utils.Utility;

public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		
		 System.out.println(org.slf4j.LoggerFactory.getILoggerFactory().toString());
		
		System.out.println("Hi");

		String data = "[\"hello\", \"haha\", 1, 2, '3', 'xin chao các bạn']";

		 logger.info("This is an informational log message.");
		System.out.println(Utility.toArray(data));
	}
}
