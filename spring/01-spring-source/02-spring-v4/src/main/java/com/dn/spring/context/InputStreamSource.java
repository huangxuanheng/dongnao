package com.dn.spring.context;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSource {
	InputStream getInputStream() throws IOException;
}
