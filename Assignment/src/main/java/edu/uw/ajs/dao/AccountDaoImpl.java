package main.java.edu.uw.ajs.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.edu.uw.ajs.account.AccountImpl;
import main.java.edu.uw.ajs.account.AccountManagerImpl;
import main.java.edu.uw.ajs.account.AddressImpl;
import main.java.edu.uw.ajs.account.CreditCardImpl;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.dao.AccountDao;

public class AccountDaoImpl implements AccountDao {

	private static final Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

	private static final String ACCOUNT_FILENAME = "account.dat";

	private static final String ACCOUNT_ADDRESS = "address.properties";

	private static final String ACCOUNT_CREDITCARD = "creditcard.properties";

	private static final String ACCOUNTS_DIR = "target/accounts/";

	private final File accountDir = new File("target", "/accounts");

	@Override
	public Account getAccount(String accountName) {
		Account account = null;
		FileInputStream in = null;
		DataInputStream input = null;
		final File accountInput = new File(this.accountDir, accountName);
		final File accountNameInput = new File(accountInput, ACCOUNT_FILENAME);

		logger.info("GET ACCOUNT " + accountName);
		if (accountInput.exists()) {
			if (accountNameInput.exists()) {

				try {
					logger.info("ACCOUNT EXISTS " + accountNameInput);

					// Account Name
					in = new FileInputStream(accountNameInput);
					input = new DataInputStream(in);
					final String inAccountName = input.readUTF();
					final int length = input.readInt();
					byte[] inAccountPassword = new byte[length];
					input.readFully(inAccountPassword);

					final int inAccountBalance = input.readInt();
					account = new AccountImpl(inAccountName, inAccountPassword, inAccountBalance);

					final String inAccountEmail = input.readUTF();
					account.setEmail(inAccountEmail);

					final String inAccountFullName = input.readUTF();
					account.setFullName(inAccountFullName);

					final String inAccountPhone = input.readUTF();
					account.setPhone(inAccountPhone);

					// }

					in.close();

					logger.info("ACCOUNT SET " + accountName);

					// Account Address
					if ((new File(accountInput, ACCOUNT_ADDRESS).exists())) {
						logger.info("GETTING ADDRESS " + accountName);

						Properties accountAddressProps = new Properties();

						final File accountAddressInput = new File(accountInput, ACCOUNT_ADDRESS);
						in = new FileInputStream(accountAddressInput);
						accountAddressProps.load(in);
						AddressImpl address = new AddressImpl();
						address.setCity(accountAddressProps.getProperty("city"));
						address.setState(accountAddressProps.getProperty("state"));
						address.setStreetAddress(accountAddressProps.getProperty("streetaddress"));
						address.setZipCode(accountAddressProps.getProperty("zipcode"));
						account.setAddress(address);
						in.close();
					}

					// Account Creditcard
					if ((new File(accountInput, ACCOUNT_CREDITCARD).exists())) {
						logger.info("GETTING CREDITCARD " + accountName);

						Properties accountCreditCardProps = new Properties();
						final File accountCreditCardInput = new File(accountInput, ACCOUNT_CREDITCARD);
						in = new FileInputStream(accountCreditCardInput);
						accountCreditCardProps.load(in);
						CreditCardImpl creditCard = new CreditCardImpl();
						creditCard.setAccountNumber(accountCreditCardProps.getProperty("accountnumber"));
						creditCard.setExpirationDate(accountCreditCardProps.getProperty("expirationDate"));
						creditCard.setHolder(accountCreditCardProps.getProperty("holder"));
						creditCard.setIssuer(accountCreditCardProps.getProperty("issuer"));
						creditCard.setType(accountCreditCardProps.getProperty("type"));
						account.setCreditCard(creditCard);
						in.close();
					}

					AccountManagerImpl accountManager = new AccountManagerImpl(this);

					account.registerAccountManager(accountManager);

				} catch (IOException | AccountException e) {
					e.printStackTrace();
				}
			}
		}
		logger.info("ACCOUNT CREATED");

		return account;
	}

	@Override
	public void setAccount(Account account) throws AccountException {
		logger.info("SETTING ACCOUNT " + account.getName());

		final File accountDir = new File(this.accountDir, account.getName());

		if (!accountDir.exists()) {
			logger.info("ACCOUNT DIR EXISTS ACCOUNT " + accountDir);

			accountDir.mkdirs();
		}

		// Create Account Name File
		final File accountFileName = new File(accountDir, ACCOUNT_FILENAME);
		logger.info("FILE DIR TO BE CREATED " + accountFileName);

		if (accountFileName.exists()) {
			logger.info("DELETE FILE " + accountFileName);
			deleteFile(accountFileName);

		}

		try (DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(accountFileName))) {
			logger.info("OUTPUT DATA " + accountFileName);

			// Write Name
			dataOut.writeUTF(account.getName());
			logger.info("Name set " + account.getName());

			// Write Password
			dataOut.writeInt(account.getPasswordHash().length);

			dataOut.write(account.getPasswordHash());
			logger.info("Password set " + account.getPasswordHash());

			// Write Balance
			dataOut.writeInt(account.getBalance());
			logger.info("Balance set " + account.getBalance());

			// Write Email
			if (account.getEmail() != null) {
				dataOut.writeUTF(account.getEmail());
				logger.info("Email set " + account.getEmail());
			} else {
				dataOut.writeUTF("1");
			}

			// Write Full Name
			if (account.getFullName() != null) {
				dataOut.writeUTF(account.getFullName());
				logger.info("Full name set " + account.getFullName());
			} else {
				dataOut.writeUTF("1");

			}

			// Write Phone
			if (account.getPhone() != null) {
				dataOut.writeUTF(account.getPhone());
				logger.info("Phone set " + account.getPhone());
			} else {
				dataOut.writeUTF("1");

			}

			dataOut.flush();
			dataOut.close();
			logger.info("DATA OUTPUT DATE " + accountFileName);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Write Credit Card
		logger.info("OUTPUT CREDIT CARD " + accountFileName);

		if (!(account.getCreditCard() == null)) {

			final File creditCardFileName = new File(accountDir, ACCOUNT_CREDITCARD);
			logger.info("OUTPUT CREDIT CARD DIR EXITS " + accountFileName);
			if (creditCardFileName.exists()) {
				deleteFile(creditCardFileName);
				logger.info("OUTPUT CREDIT CARD DIR DELTED " + accountFileName);

			}

			try (PrintStream ps = new PrintStream(creditCardFileName)) {
				logger.info("OUTPUT CREDIT CARD DATA " + accountFileName);

				CreditCard creditCard = account.getCreditCard();
				Properties accountCreditCardProps = new Properties();
				OutputStream output = new FileOutputStream(creditCardFileName);

				String accountnumber = "";
				String expirationDate = "";
				String holder = "";
				String issuer = "";
				String type = "";

				if (creditCard.getAccountNumber() != null) {
					accountnumber = creditCard.getAccountNumber();
				}

				if (creditCard.getExpirationDate() != null) {
					expirationDate = creditCard.getExpirationDate();
				}

				if (creditCard.getHolder() != null) {
					holder = creditCard.getHolder();
				}

				if (creditCard.getIssuer() != null) {
					issuer = creditCard.getIssuer();
				}

				if (creditCard.getType() != null) {
					type = creditCard.getType();
				}

				accountCreditCardProps.setProperty("accountnumber", accountnumber);
				accountCreditCardProps.setProperty("expirationDate", expirationDate);
				accountCreditCardProps.setProperty("holder", holder);
				accountCreditCardProps.setProperty("issuer", issuer);
				accountCreditCardProps.setProperty("type", type);
				accountCreditCardProps.store(output, null);
				output.flush();
				output.close();
				logger.info("CREDIT CARD DATA OUTPUT" + accountFileName);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Write Address
		logger.info("OUTPUT ADDRESS " + accountFileName);

		if (!(account.getAddress() == null)) {

			final File addressFileName = new File(accountDir, ACCOUNT_ADDRESS);
			logger.info("OUTPUT ADDRESS DIR EXITS " + accountFileName);

			if (addressFileName.exists()) {
				deleteFile(addressFileName);
				logger.info("OUTPUT ADDRESS DIR DELETED " + accountFileName);

			}

			try (PrintStream ps = new PrintStream(addressFileName)) {
				logger.info("OUTPUT ADDRESS DATA " + accountFileName);

				Address address = account.getAddress();
				Properties accountAddressProps = new Properties();
				OutputStream output = new FileOutputStream(addressFileName);
				accountAddressProps.setProperty("city", address.getCity());
				accountAddressProps.setProperty("state", address.getState());
				accountAddressProps.setProperty("streetaddress", address.getStreetAddress());
				accountAddressProps.setProperty("zipcode", address.getZipCode());
				accountAddressProps.store(output, null);
				output.flush();
				output.close();
				logger.info("CREDIT ADDRESS OUTPUT" + accountFileName);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Write Account Object As JSON File

		// **************TODO ADD LOGIC HERE
		ObjectMapper mapper = new ObjectMapper();

		// Object to JSON in file
		try {
			mapper.writeValue(new File(this.accountDir, account.getName() + ".JSON"), account);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Object to JSON in String
		try {
			String jsonInString = mapper.writeValueAsString(account);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deleteAccount(String accountName) throws AccountException {
		final File account = new File(this.accountDir, accountName);
		logger.info("DELETE ACCOUNT " + account);

		if (!account.isDirectory()) {
			logger.info("NOT DIRECTORY DELETE " + account);
			deleteFile(account);
			logger.info("DELETE SUCCESS? " + account.exists());

		} else {
			File[] directories = account.listFiles();
			for (int i = 0; i < directories.length; i++) {
				File fileDirectories = directories[i];
				logger.info("DIRECTORY DELETE " + fileDirectories.getName());
				logger.info(account.getName());

				deleteAccount(account.getName() + "/" + fileDirectories.getName());
				logger.info("DELETE SUCCESS? " + fileDirectories.exists());

			}
		}

	}

	@Override
	public void reset() throws AccountException {
		logger.info("RESETTING FOLDER");
		File dir = new File(ACCOUNTS_DIR);
		logger.info("FILE DIRECTORY FOLDER " + dir);
		if (!dir.isDirectory()) {
			try {
				throw new IOException("Not a directory " + dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];

			if (file.isDirectory()) {

				logger.info("DELETE FILE " + file.getName());
				deleteAccount(file.getName());

			} else {
				boolean deleted = file.delete();
				if (!deleted) {
					try {
						throw new IOException("Unable to delete file" + file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void deleteFile(File file) {
		logger.info("DELETE FILE ");

		if (file.exists()) {
			if (!file.isDirectory()) {
				logger.info("DELETE FILE " + file);

				file.delete();

				logger.info("DELETED FILE EXISTS " + (file.exists()));

			}
		}
	}

	@Override
	public void close() throws AccountException {

	}

}
