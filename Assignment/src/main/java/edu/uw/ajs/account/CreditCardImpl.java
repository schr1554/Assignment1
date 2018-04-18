package main.java.edu.uw.ajs.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.CreditCard;

@SuppressWarnings("serial")
public class CreditCardImpl implements CreditCard {

	private static final Logger logger = LoggerFactory.getLogger(AddressImpl.class);

	private String accountNumber;
	private String expirationDate;
	private String holder;
	private String issuer;
	private String type;

	@Override
	public String getAccountNumber() {
		logger.info("Get account number " + this.accountNumber);
		return this.accountNumber;
	}

	@Override
	public String getExpirationDate() {
		logger.info("Get expirationDate " + this.expirationDate);

		return this.expirationDate;
	}

	@Override
	public String getHolder() {
		logger.info("Get holder " + this.holder);

		return this.holder;
	}

	@Override
	public String getIssuer() {
		logger.info("Get issuer " + this.issuer);

		return this.issuer;
	}

	@Override
	public String getType() {
		logger.info("Get type " + this.type);

		return this.type;
	}

	@Override
	public void setAccountNumber(String accountNumber) {
		logger.info("Set accountNumber " + this.accountNumber);

		this.accountNumber = accountNumber;
	}

	@Override
	public void setExpirationDate(String expirationDate) {
		logger.info("Set expirationDate " + this.expirationDate);

		this.expirationDate = expirationDate;
	}

	@Override
	public void setHolder(String holder) {
		logger.info("Set holder " + this.holder);

		this.holder = holder;
	}

	@Override
	public void setIssuer(String issuer) {
		logger.info("Set issuer " + this.issuer);

		this.issuer = issuer;
	}

	@Override
	public void setType(String type) {
		logger.info("Set type " + this.type);

		this.type = type;
	}

}
