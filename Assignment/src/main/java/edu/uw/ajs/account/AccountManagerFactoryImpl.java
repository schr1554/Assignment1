package main.java.edu.uw.ajs.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.AccountManagerFactory;
import edu.uw.ext.framework.dao.AccountDao;

public class AccountManagerFactoryImpl implements AccountManagerFactory {

	private static final Logger logger = LoggerFactory.getLogger(AccountManagerFactoryImpl.class);

	@Override
	public AccountManager newAccountManager(AccountDao dao) {
		logger.info("Creating a new Account Manager");
		return new AccountManagerImpl(dao);
	}

}
