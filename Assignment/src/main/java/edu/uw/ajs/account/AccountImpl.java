package main.java.edu.uw.ajs.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.order.Order;

public class AccountImpl implements Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(AccountImpl.class);

	private Address address;
	private int balance;
	private CreditCard creditCard;
	private String email;
	private String fullName;
	private String name;
	private byte[] passwordHash;
	private String phone;
	private transient AccountManager accountManager;

	public AccountImpl() {

	}

	/**
	 * @param accountName
	 * @param password
	 * @param balance
	 * @throws AccountException
	 */
	public AccountImpl(String accountName, byte[] password, int balance) throws AccountException {

		if (balance < 100000) {
			throw new AccountException("NEW ACCOUNT BALANCE MUST BE AT LEAST $1,000");
		}
		setName(accountName);

		passwordHash = password;

		setBalance(balance);

	}

	@Override
	public Address getAddress() {
		logger.info("Getting address " + address);
		return this.address;
	}

	@Override
	public int getBalance() {
		logger.info("Getting balance " + balance);
		return this.balance;
	}

	@Override
	public CreditCard getCreditCard() {
		logger.info("Getting creditcard " + creditCard);

		return this.creditCard;
	}

	@Override
	public String getEmail() {
		logger.info("Getting email " + email);

		return this.email;
	}

	@Override
	public String getFullName() {
		logger.info("Getting full name " + fullName);

		return this.fullName;
	}

	@Override
	public String getName() {
		logger.info("Getting name " + name);

		return this.name;
	}

	@Override
	public byte[] getPasswordHash() {
		logger.info("Getting password " + passwordHash);
		return this.passwordHash;
	}

	@Override
	public String getPhone() {
		logger.info("Getting phone " + phone);

		return this.phone;
	}

	@Override
	public void reflectOrder(Order order, int executionPrice) {

		this.balance += order.valueOfOrder(executionPrice);
		if (this.accountManager != null) {

			try {
				accountManager.persist(this);
			} catch (AccountException e) {
				logger.error("Account manager has not been created");
				e.printStackTrace();
			}

		}

	}

	@Override
	public void registerAccountManager(AccountManager mgr) {
		if (accountManager == null) {
			this.accountManager = mgr;
		} else {
			logger.info("attemping to set manager but can only be called once");
		}

	}

	@Override
	public void setAddress(Address address) {
		logger.info("Setting address " + address);

		this.address = address;
	}

	@Override
	public void setBalance(int blnc) {
		logger.info("Setting balance " + balance);
		System.out.println(blnc);
		this.balance = blnc;
	}

	@Override
	public void setCreditCard(CreditCard credca) {
		logger.info("Setting creditCard " + creditCard);

		this.creditCard = credca;
	}

	@Override
	public void setEmail(String email) {
		logger.info("Setting email " + email);

		this.email = email;

	}

	@Override
	public void setFullName(String fullName) {
		logger.info("Setting fullName " + fullName);

		this.fullName = fullName;

	}

	@Override
	public void setName(String accountName) throws AccountException {
		logger.info("Setting name: LENGTH " + accountName.length() + " NAME " + accountName);

		if (accountName.length() >= 8) {
			this.name = accountName;
		} else {
			throw new AccountException("NAME MUST BE 8 CHARACTERS IN LENGTH");
		}

	}

	@Override
	public void setPasswordHash(byte[] passwordHash) {
		logger.info("Setting password " + passwordHash);

		this.passwordHash = passwordHash;
	}

	@Override
	public void setPhone(String phone) {
		logger.info("Setting phone " + phone);

		this.phone = phone;

	}

}
