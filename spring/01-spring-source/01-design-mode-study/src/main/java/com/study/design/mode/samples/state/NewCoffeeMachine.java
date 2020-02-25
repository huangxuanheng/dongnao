package com.study.design.mode.samples.state;

public class NewCoffeeMachine {

	final State NO_PAY, PAY, SOLD, SOLD_OUT;
	State state;
	int store;

	public NewCoffeeMachine(int store) {
		NO_PAY = new NoPayState(this);
		PAY = new PayState(this);
		SOLD = new SoldState(this);
		SOLD_OUT = new SoldOutState(this);

		this.store = store;
		if (this.store > 0) {
			this.state = NO_PAY;
		}
	}

	public void pay() {
		this.state.pay();
	}

	public void refund() {
		this.state.refund();
	}

	// 购买
	public void buy() {
		this.state.buy();
	}

	// 取coffee
	public void getCoffee() {
		this.state.getCoffee();
	}
}
