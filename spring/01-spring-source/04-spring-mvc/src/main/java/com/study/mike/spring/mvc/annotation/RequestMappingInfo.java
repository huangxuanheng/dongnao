package com.study.mike.spring.mvc.annotation;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

public class RequestMappingInfo {

	private RequestMapping classRequestMapping;

	private String beanName;

	private Class<?> beanType;

	private RequestMapping methodRequestMapping;

	private Method method;

	private Object handler;

	public RequestMappingInfo(RequestMapping classRequestMapping, String beanName, Class<?> beanType,
			RequestMapping methodRequestMapping, Method method) {
		super();
		this.classRequestMapping = classRequestMapping;
		this.beanName = beanName;
		this.beanType = beanType;
		this.methodRequestMapping = methodRequestMapping;
		this.method = method;
	}

	public boolean match(HttpServletRequest request) {

		// 1、path 不需要进行匹配了，匹配了path才选择的该Mapper

		// 2、 匹配 http Method
		if (!this.matchHttpMethod(request)) {
			return false;
		}

		// 3、匹配headers
		if (!this.matchHeaders(request)) {
			return false;
		}
		// 4、匹配params
		if (!this.matchParams(request)) {
			return false;
		}
		return true;
	}

	private boolean matchHttpMethod(HttpServletRequest request) {
		if (this.methodRequestMapping.method().length > 0) {
			boolean methodMatch = false;
			String m = request.getMethod();
			for (RequestMethod rm : this.methodRequestMapping.method()) {
				if (rm.name().equals(m)) {
					methodMatch = true;
					break;
				}
			}
			return methodMatch;
		} else if (this.classRequestMapping != null && this.classRequestMapping.method().length > 0) {
			boolean methodMatch = false;
			String m = request.getMethod();
			for (RequestMethod rm : this.methodRequestMapping.method()) {
				if (rm.name().equals(m)) {
					methodMatch = true;
					break;
				}
			}
			return methodMatch;
		}
		return true;
	}

	private boolean matchHeaders(HttpServletRequest request) {
		if (this.methodRequestMapping.headers().length > 0) {
			for (String h : this.methodRequestMapping.headers()) {
				if (request.getHeader(h) == null) {
					return false;
				}
			}
		}
		if (this.classRequestMapping != null && this.classRequestMapping.headers().length > 0) {
			for (String h : this.classRequestMapping.headers()) {
				if (request.getHeader(h) == null) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean matchParams(HttpServletRequest request) {
		if (this.methodRequestMapping.params().length > 0) {
			for (String p : this.methodRequestMapping.params()) {
				if (request.getParameter(p) == null) {
					return false;
				}
			}
		} else if (this.classRequestMapping != null && this.classRequestMapping.params().length > 0) {
			for (String p : this.classRequestMapping.params()) {
				if (request.getParameter(p) == null) {
					return false;
				}
			}
		}

		return true;
	}

	public RequestMapping getClassRequestMapping() {
		return classRequestMapping;
	}

	public void setClassRequestMapping(RequestMapping classRequestMapping) {
		this.classRequestMapping = classRequestMapping;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Class<?> getBeanType() {
		return beanType;
	}

	public void setBeanType(Class<?> beanType) {
		this.beanType = beanType;
	}

	public RequestMapping getMethodRequestMapping() {
		return methodRequestMapping;
	}

	public void setMethodRequestMapping(RequestMapping methodRequestMapping) {
		this.methodRequestMapping = methodRequestMapping;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getHandler() {
		return handler;
	}

	public void setHandler(Object handler) {
		this.handler = handler;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beanName == null) ? 0 : beanName.hashCode());
		result = prime * result + ((beanType == null) ? 0 : beanType.hashCode());
		result = prime * result + ((classRequestMapping == null) ? 0 : classRequestMapping.hashCode());
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((methodRequestMapping == null) ? 0 : methodRequestMapping.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestMappingInfo other = (RequestMappingInfo) obj;
		if (beanName == null) {
			if (other.beanName != null)
				return false;
		} else if (!beanName.equals(other.beanName))
			return false;
		if (beanType == null) {
			if (other.beanType != null)
				return false;
		} else if (!beanType.equals(other.beanType))
			return false;
		if (classRequestMapping == null) {
			if (other.classRequestMapping != null)
				return false;
		} else if (!classRequestMapping.equals(other.classRequestMapping))
			return false;
		if (handler == null) {
			if (other.handler != null)
				return false;
		} else if (!handler.equals(other.handler))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (methodRequestMapping == null) {
			if (other.methodRequestMapping != null)
				return false;
		} else if (!methodRequestMapping.equals(other.methodRequestMapping))
			return false;
		return true;
	}

	public int hashCodeForRequestMapping(RequestMapping rm) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beanName == null) ? 0 : beanName.hashCode());
		result = prime * result + ((beanType == null) ? 0 : beanType.hashCode());
		result = prime * result + ((classRequestMapping == null) ? 0 : classRequestMapping.hashCode());
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((methodRequestMapping == null) ? 0 : methodRequestMapping.hashCode());
		return result;
	}

	public boolean duplicateTo(RequestMappingInfo rm) {
		// params参数存在不同
		if (paramsDiff(rm)) {
			return false;
		}
		// headers存在不同
		if (headersDiff(rm)) {
			return false;
		}
		// httpMethod 完全不同
		if (httpMethodDiff(rm)) {
			return false;
		}
		return true;
	}

	/**
	 * httpMethodDiff的检查是不能存在相同的
	 * 
	 * @param rm
	 * @return
	 */
	private boolean httpMethodDiff(RequestMappingInfo rm) {
		RequestMethod[] thisMethods = this.methodRequestMapping.method();
		if (thisMethods.length == 0) {
			if (this.classRequestMapping != null) {
				thisMethods = this.classRequestMapping.method();
			}
		}

		RequestMethod[] otherMethods = rm.methodRequestMapping.method();
		if (otherMethods.length == 0) {
			if (rm.classRequestMapping != null) {
				otherMethods = rm.classRequestMapping.method();
			}
		}

		if (thisMethods.length == 0) {
			if (otherMethods.length == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			if (otherMethods.length == 0) {
				return true;
			} else {
				for (RequestMethod m : thisMethods) {
					for (RequestMethod q : otherMethods) {
						if (m.equals(q)) {
							return false;
						}
					}
				}

				return true;
			}
		}
	}

	/**
	 * headersDiff的检查是只要有不同
	 * 
	 * @param rm
	 * @return
	 */
	private boolean headersDiff(RequestMappingInfo rm) {
		String[] thisHeaders = this.methodRequestMapping.headers();
		if (thisHeaders.length == 0) {
			if (this.classRequestMapping != null) {
				thisHeaders = this.classRequestMapping.headers();
			}
		}

		String[] otherHeaders = rm.methodRequestMapping.headers();
		if (otherHeaders.length == 0) {
			if (rm.classRequestMapping != null) {
				otherHeaders = rm.classRequestMapping.headers();
			}
		}

		if (thisHeaders.length == 0) {
			if (otherHeaders.length == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			if (otherHeaders.length == 0) {
				return true;
			} else {
				boolean hasSame = false;
				for (String h : thisHeaders) {
					hasSame = false;
					for (String q : otherHeaders) {
						if (h.equals(q)) {
							hasSame = true;
							break;
						}
					}
					if (!hasSame) {
						return true;
					}
				}

				return false;
			}
		}
	}

	/**
	 * paramsDiff的检查是只要有不同
	 * 
	 * @param rm
	 * @return
	 */
	private boolean paramsDiff(RequestMappingInfo rm) {
		String[] thisParams = this.methodRequestMapping.params();
		if (thisParams.length == 0) {
			if (this.classRequestMapping != null) {
				thisParams = this.classRequestMapping.params();
			}
		}

		String[] otherParams = rm.methodRequestMapping.params();
		if (otherParams.length == 0) {
			if (rm.classRequestMapping != null) {
				otherParams = rm.classRequestMapping.params();
			}
		}

		if (thisParams.length == 0) {
			if (otherParams.length == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			if (otherParams.length == 0) {
				return true;
			} else {
				boolean hasSame = false;
				for (String h : thisParams) {
					hasSame = false;
					for (String q : otherParams) {
						if (h.equals(q)) {
							hasSame = true;
							break;
						}
					}
					if (!hasSame) {
						return true;
					}
				}

				return false;
			}
		}
	}

}
