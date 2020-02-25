package com.study.design.mode.samples.proxy;

public class Tony implements Girl {

	private Girl girl;

	public Girl getGirl() {
		return girl;
	}

	public void setGirl(Girl girl) {
		this.girl = girl;
	}

	public boolean dating(float length) {
		// 前置增强
		doSomethingBefore();
		boolean res = this.girl.dating(length);
		// 后置增强
		doSomethingAfter();
		return res;
	}

	private void doSomethingBefore() {
		System.out.println("老板，这个我试过了，很不错，推荐给你！");
	}
	
	private void doSomethingAfter() {
		System.out.println("老板，你觉得怎样，欢迎下次再约！");
	}

}
