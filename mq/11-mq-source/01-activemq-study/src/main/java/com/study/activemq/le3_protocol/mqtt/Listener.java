/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.study.activemq.le3_protocol.mqtt;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 * Uses an callback based interface to MQTT. Callback based interfaces are
 * harder to use but are slightly more efficient.
 */
class Listener {

	public static void main(String[] args) throws Exception {

		MQTT mqtt = new MQTT();
		mqtt.setHost("localhost", 1883);
		// mqtt.setUserName(user);
		// mqtt.setPassword(password);

		final CallbackConnection connection = mqtt.callbackConnection();
		connection.listener(new org.fusesource.mqtt.client.Listener() {
			long count = 0;
			long start = System.currentTimeMillis();

			public void onConnected() {
			}

			public void onDisconnected() {
			}

			public void onFailure(Throwable value) {
				value.printStackTrace();
				System.exit(-2);
			}

			public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
				String body = msg.utf8().toString();
				System.out.println("收到消息：" + body);
				ack.run();
			}
		});
		connection.connect(new Callback<Void>() {
			@Override
			public void onSuccess(Void value) {
				Topic[] topics = { new Topic("foo/blah/bar", QoS.AT_LEAST_ONCE) };
				connection.subscribe(topics, new Callback<byte[]>() {
					public void onSuccess(byte[] qoses) {
					}

					public void onFailure(Throwable value) {
						value.printStackTrace();
						System.exit(-2);
					}
				});
			}

			@Override
			public void onFailure(Throwable value) {
				value.printStackTrace();
				System.exit(-2);
			}
		});

		// Wait forever..
		synchronized (Listener.class) {
			while (true)
				Listener.class.wait();
		}
	}

}