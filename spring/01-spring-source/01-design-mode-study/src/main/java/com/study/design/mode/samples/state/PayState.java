package com.study.design.mode.samples.state;

public class PayState implements State {

	private NewCoffeeMachine machine;

	public PayState(NewCoffeeMachine machine) {
		this.machine = machine;
	}

	@Override
	public void pay() {
		System.out.println("您已支付，请去确定购买！");
	}

	@Override
	public void refund() {
		System.out.println("退款成功，请收好！");
		this.machine.state = this.machine.NO_PAY;
	}

	@Override
	public void buy() {
		System.out.println("购买成功，请取用");
		this.machine.state = this.machine.SOLD;
	}

	@Override
	public void getCoffee() {
		System.out.println("请先确定购买！");
	}
}
