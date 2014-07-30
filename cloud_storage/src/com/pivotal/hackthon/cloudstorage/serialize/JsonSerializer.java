package com.pivotal.hackthon.cloudstorage.serialize;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.pivotal.hackthon.cloudstorage.storage.CloudFile;

public class JsonSerializer {

	public static String serialize(List<CloudFile> cFiles) {
		return new Gson().toJson(cFiles);
	}

	private static String serialize(CloudFile cFile) {
		return new Gson().toJson(cFile);
	}

	public static void main(String[] args) {
		CloudFile cFile = new CloudFile();
		System.out.println(serialize(Lists.newArrayList(cFile)));
	}
}
