package com.dn.spring.aop.advisor;

import com.dn.spring.aop.pointcut.Pointcut;

public interface PointcutAdvisor extends Advisor {

	Pointcut getPointcut();
}
