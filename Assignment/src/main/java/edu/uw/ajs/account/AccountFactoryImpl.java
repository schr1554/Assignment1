package main.java.edu.uw.ajs.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountFactory;

public class AccountFactoryImpl implements AccountFactory {

	private static final Logger logger = LoggerFactory.getLogger(AccountFactoryImpl.class);

	@Override
	public Account newAccount(String accountName, byte[] password, int balance) {

		logger.info("Setting up new account in factory " + accountName);
		Account account = null;

		try {
			account = new AccountImpl(accountName, password, balance);
			logger.info("Setup complete new account in factory " + accountName);

		} catch (AccountException e) {
			e.printStackTrace();
		}

		return account;
	}

}
