package main;

import excel.importer.utils.Utility;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hi");

		String data = "[\"hello\", \"haha\", 1, 2, '3', 'xin chao các bạn']";

		System.out.println(Utility.toArray(data));
	}
}
