package com.palscash.api.client;

import org.palscash.network.api.client.impl.PalsCashClientImpl;
import org.palscash.network.api.model.GetFeeResponse;
import org.palscash.network.api.model.PingResponse;

import junit.framework.TestCase;

public class TestPalsCashClient extends TestCase {

	private PalsCashClientImpl client = new PalsCashClientImpl();

	{
		client.setHost("http://localhost:8080");
		client.init();
	}

	public void testPing() throws Exception {
		PingResponse ping = client.ping();
		System.out.println(ping);
	}

	public void testFee() throws Exception {
		GetFeeResponse fee = client.getFee();
		System.out.println(fee);
	}

}
