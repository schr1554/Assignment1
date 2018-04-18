package main.java.edu.uw.ajs.account;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.dao.AccountDao;

public class AccountManagerImpl implements AccountManager {

	private static final Logger logger = LoggerFactory.getLogger(AccountManagerImpl.class);

	private static final String ENCODING = "UTF-8";

	private static final String ALGORITHM = "SHA-1";

	private AccountDao dao;

	public AccountManagerImpl() {

	}

	public AccountManagerImpl(final AccountDao dao) {
		logger.info("Creating account manager impl.");
		this.dao = dao;

	}

	@Override
	public void close() throws AccountException {
		logger.info("Closing account manager impl.");
		dao.close();
		dao = null;
	}

	@Override
	public Account createAccount(String accountName, String password, int balance) throws AccountException {

		logger.info("Creating a account.");

		byte[] passByte;
		Account acct = new AccountImpl();
		passByte = hashPassword(password);
		acct.setName(accountName);
		acct.setPasswordHash(passByte);
		acct.setBalance(balance);
		persist(acct);

		logger.info("Account " + accountName + " created");

		return acct;

	}

	@Override
	public void deleteAccount(String accountName) throws AccountException {
		logger.info("Deleting " + accountName);

		dao.deleteAccount(accountName);
	}

	@Override
	public Account getAccount(String accountName) throws AccountException {
		final Account acct = dao.getAccount(accountName);

		if (acct != null) {
			acct.registerAccountManager(this);
			logger.info("Registering account manger " + accountName);

		}
		return acct;
	}

	@Override
	public void persist(Account account) throws AccountException {
		dao.setAccount(account);
		logger.info("Persist account " + account);

	}

	@Override
	public boolean validateLogin(String accountName, String password) throws AccountException {

		logger.info("Validate login " + accountName);

		logger.info("New pass " + password);

		boolean valid = false;
		final Account account = getAccount(accountName);

		if (account != null) {
			final byte[] passwordHash = hashPassword(password);

			logger.info("New pass " + passwordHash);

			valid = MessageDigest.isEqual(account.getPasswordHash(), passwordHash);
		}
		logger.info("Validated login......" + valid);

		return valid;
	}

	private static byte[] hashPassword(String password) {

		MessageDigest md = null;

		logger.info("Message digest......" + password);

		try {
			md = MessageDigest.getInstance(ALGORITHM);
			try {
				md.update(password.getBytes(ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return md.digest();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md.digest();

	}
}
