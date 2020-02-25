package com.study.design.mode.samples;

import java.util.Observable;
import java.util.Observer;

public class ObserverSample {

	public static void main(String[] args) {
		Observable subject1 = new Observable() {
			public synchronized void notifyObservers(Object data) {
				setChanged();
				super.notifyObservers(data);
			}
		};

		subject1.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println("观察者1收到通知被更新了..." + arg);
			}
		});

		subject1.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println("观察者2收到通知被更新了..." + arg);
			}
		});

		subject1.notifyObservers("change1");
		subject1.notifyObservers("change2");
	}
}
