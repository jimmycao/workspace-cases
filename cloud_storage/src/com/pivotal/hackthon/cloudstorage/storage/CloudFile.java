package com.pivotal.hackthon.cloudstorage.storage;

public class CloudFile {

	private String name;

	private String fullPath;

	private long size;

	private boolean isDir;
	
	public boolean isDir() {
		return isDir;
	}


	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFullPath() {
		return fullPath;
	}


	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}


	public long getSize() {
		return size;
	}


	public void setSize(long size) {
		this.size = size;
	}


	public static class Builder {

		private CloudFile cFile = new CloudFile();

		public static Builder newBuilder() {
			return new Builder();
		}

		public Builder setName(String name) {
			cFile.name = name;
			return this;
		}

		public Builder setSize(long size) {
			cFile.size = size;
			return this;
		}

		public Builder isDir(boolean isDir){
			cFile.isDir=isDir;
			return this;
		}
		
		public CloudFile build() {
			return cFile;
		}
	}
}
