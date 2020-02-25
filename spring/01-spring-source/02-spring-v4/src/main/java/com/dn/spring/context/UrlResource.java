package com.dn.spring.context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlResource implements Resource {

	private URL url;

	public UrlResource(String url) throws IOException {
		this.url = new URL(url);
	}

	public UrlResource(URL url) {
		super();
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (this.url != null) {
			return this.url.openStream();
		}
		return null;
	}

	@Override
	public boolean exists() {
		return this.url != null;
	}

	@Override
	public boolean isReadable() {
		return exists();
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public File getFile() {
		return null;
	}

}
