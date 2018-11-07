/*

MIT License

Copyright (c) 2017 ZDP Developers
Copyright (c) 2018 PalsCash Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package org.palscash.network.api.client.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.palscash.common.crypto.Base58;
import org.palscash.common.crypto.Curves;
import org.palscash.common.crypto.PalsCashKeyPair;
import org.palscash.common.crypto.mnemonics.Mnemonics.Language;
import org.palscash.network.api.Urls;
import org.palscash.network.api.client.PalsCashClient;
import org.palscash.network.api.model.GetAccountResponse;
import org.palscash.network.api.model.GetBalanceResponse;
import org.palscash.network.api.model.GetFeeResponse;
import org.palscash.network.api.model.GetNewAccountRequest;
import org.palscash.network.api.model.GetNewAccountResponse;
import org.palscash.network.api.model.PingResponse;
import org.palscash.network.api.model.RestoreAccountRequest;
import org.palscash.network.api.model.RestoreAccountResponse;
import org.palscash.network.api.model.TransactionCountResponse;
import org.palscash.network.api.model.TransactionInfo;
import org.palscash.network.api.model.TransactionListResponse;
import org.palscash.network.api.model.TransferRequest;
import org.palscash.network.api.model.TransferResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PalsCashClientImpl implements PalsCashClient {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private RestTemplate restTemplate;

	private String host;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
	}

	@Override
	public PingResponse ping() throws Exception {
		URI uri = new URI(host + Urls.URL_PING);
		log.trace("Ping: " + uri);
		return this.restTemplate.getForObject(uri, PingResponse.class);

	}

	@Override
	public GetFeeResponse getFee() throws Exception {
		URI uri = new URI(host + Urls.URL_GET_TX_FEE);
		log.trace("Get fee: " + uri);
		return restTemplate.getForObject(uri, GetFeeResponse.class);

	}

	@Override
	public GetNewAccountResponse getNewAccount(Language language) throws Exception {
		final URI uri = new URI(host + Urls.URL_GET_NEW_ACCOUNT);
		return restTemplate.postForObject(uri, new GetNewAccountRequest(language.name()), GetNewAccountResponse.class);
	}

	@Override
	public GetNewAccountResponse getNewAccount() throws Exception {
		final URI uri = new URI(host + Urls.URL_GET_NEW_ACCOUNT);
		return restTemplate.getForObject(uri, GetNewAccountResponse.class);

	}

	@Override
	public GetBalanceResponse getBalance(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_BALANCE, "{address}", address));

		log.debug("getBalance: " + uri);

		final GetBalanceResponse response = restTemplate.getForObject(uri, GetBalanceResponse.class);

		log.debug("gotBalance: " + response);

		return response;

	}

	@Override
	public TransferResponse transfer(String privateKeyB58, String to, BigDecimal amount, String memo) throws Exception {

		final URI uri = new URI(host + Urls.URL_TRANSFER);

		PalsCashKeyPair kp = PalsCashKeyPair.createFromPrivateKeyBase58(privateKeyB58, Curves.DEFAULT_CURVE);

		log.debug("transfer: " + uri);

		TransferRequest req = new TransferRequest();

		req.setAmount(amount.toPlainString());
		req.setFrom(kp.getPalsCashAccount().getUuid());
		req.setMemo(memo);
		req.setRequestUuid(UUID.randomUUID().toString());
		req.setPublicKey(kp.getPublicKeyAsBase58());
		req.setTo(to);

		req.setSignature(Base58.encode(kp.sign(req.getUniqueTransactionUuid())));
		
		System.out.println(new ObjectMapper().writeValueAsString(req));

		return restTemplate.postForObject(uri, req, TransferResponse.class);

	}

	@Override
	public TransactionInfo getTransactionDetails(String uuid) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_TX_DETAILS, "{uuid}", uuid));

		log.debug("getTransactionDetails: " + uri);

		final TransactionInfo response = restTemplate.getForObject(uri, TransactionInfo.class);

		return response;

	}

	@Override
	public GetAccountResponse getAccount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_ACCOUNT, "{address}", address));

		log.debug("getAccount: " + uri);

		final GetAccountResponse response = restTemplate.getForObject(uri, GetAccountResponse.class);

		return response;

	}

	@Override
	public TransactionCountResponse getTransactionCount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_TX_COUNT, "{uuid}", address));

		log.debug("getTransactionCount: " + uri);

		final TransactionCountResponse response = restTemplate.getForObject(uri, TransactionCountResponse.class);

		return response;

	}

	@Override
	public TransactionListResponse getTransactionsFromAccount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_LIST_TX_FROM, "{uuid}", address));

		log.debug("getTransactionsFromAccount: " + uri);

		final TransactionListResponse response = restTemplate.getForObject(uri, TransactionListResponse.class);

		return response;

	}

	@Override
	public TransactionListResponse getTransactionsToAccount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_LIST_TX_TO, "{uuid}", address));

		log.debug("getTransactionsToAccount: " + uri);

		final TransactionListResponse response = restTemplate.getForObject(uri, TransactionListResponse.class);

		return response;

	}

	@Override
	public RestoreAccountResponse restoreAccount(String[] mnemonics) throws Exception {

		final URI uri = new URI(host + Urls.URL_GET_RESTORE_ACCOUNT);

		restTemplate.postForObject(uri, new RestoreAccountRequest(Language.ENGLISH, mnemonics, Curves.DEFAULT_CURVE),
				RestoreAccountResponse.class);

		return null;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String getHost() {
		return host;
	}

}
