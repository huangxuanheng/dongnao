package com.dn.spring.aop.advisor;

import java.util.List;

public interface AdvisorRegistry {

	public void registAdvisor(Advisor ad);

	public List<Advisor> getAdvisors();
}
