package main.java.edu.uw.ajs.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Address;

@SuppressWarnings("serial")
public class AddressImpl implements Address {

	private static final Logger logger = LoggerFactory.getLogger(AddressImpl.class);

	private String city;
	private String state;
	private String streetAddress;
	private String zipCode;

	@Override
	public String getCity() {
		logger.info("Get city " + this.city);
		return this.city;
	}

	@Override
	public String getState() {
		logger.info("Get state " + this.state);

		return this.state;
	}

	@Override
	public String getStreetAddress() {
		logger.info("Get street " + this.streetAddress);

		return this.streetAddress;
	}

	@Override
	public String getZipCode() {
		logger.info("Get zipCode " + this.zipCode);

		return this.zipCode;
	}

	@Override
	public void setCity(String city) {
		logger.info("Set city " + this.city);

		this.city = city;
	}

	@Override
	public void setState(String state) {
		logger.info("Set state " + this.state);

		this.state = state;

	}

	@Override
	public void setStreetAddress(String streetAddress) {
		logger.info("Set streetAddress " + this.streetAddress);

		this.streetAddress = streetAddress;
	}

	@Override
	public void setZipCode(String zipCode) {
		logger.info("Set zipCode " + this.zipCode);

		this.zipCode = zipCode;

	}

}
