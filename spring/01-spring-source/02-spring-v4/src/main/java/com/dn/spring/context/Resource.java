package com.dn.spring.context;

import java.io.File;

public interface Resource extends InputStreamSource {

	String CLASS_PATH_PREFIX = "classpath:";

	String File_SYSTEM_PREFIX = "file:";

	boolean exists();

	boolean isReadable();

	boolean isOpen();

	File getFile();
}
