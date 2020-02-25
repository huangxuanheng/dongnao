package com.dn.spring.context;

import java.io.IOException;

public interface ResourceLoader {

	Resource getResource(String location) throws IOException;
}
