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
package org.palscash.network.api.client;

import java.math.BigDecimal;

import org.palscash.common.crypto.mnemonics.Mnemonics.Language;
import org.palscash.network.api.model.GetAccountResponse;
import org.palscash.network.api.model.GetBalanceResponse;
import org.palscash.network.api.model.GetFeeResponse;
import org.palscash.network.api.model.GetNewAccountResponse;
import org.palscash.network.api.model.PingResponse;
import org.palscash.network.api.model.RestoreAccountResponse;
import org.palscash.network.api.model.TransactionCountResponse;
import org.palscash.network.api.model.TransactionInfo;
import org.palscash.network.api.model.TransactionListResponse;
import org.palscash.network.api.model.TransferResponse;

public interface PalsCashClient {

	void setHost(String host);

	String getHost();

	/**
	 * Ping network
	 */
	PingResponse ping() throws Exception;

	/**
	 * Get current transaction fee from network
	 */
	GetFeeResponse getFee() throws Exception;

	/**
	 * Get new account private/public keys. The account doesn't exist on the network
	 * but its information can be used to send transfers to.
	 */
	GetNewAccountResponse getNewAccount(Language language) throws Exception;

	GetNewAccountResponse getNewAccount() throws Exception;

	/**
	 * Restore an account from a list of mnemonics
	 */
	RestoreAccountResponse restoreAccount(String[] mnemonics) throws Exception;

	/**
	 * Get account balance
	 */
	GetBalanceResponse getBalance(String address) throws Exception;

	/**
	 * Get account info
	 */
	GetAccountResponse getAccount(String address) throws Exception;

	/**
	 * Submit transfer request (synchronous)
	 */
	TransferResponse transfer(String privateKeyB58, String to, BigDecimal amount, String memo) throws Exception;

	/**
	 * Get transaction details by tx uuid
	 */
	TransactionInfo getTransactionDetails(String uuid) throws Exception;

	/**
	 * Get transactions count
	 */
	TransactionCountResponse getTransactionCount(String uuid) throws Exception;

	/**
	 * List transactions FROM account
	 */
	TransactionListResponse getTransactionsFromAccount(String uuid) throws Exception;

	/**
	 * List transactions TO account
	 */
	TransactionListResponse getTransactionsToAccount(String uuid) throws Exception;

}
