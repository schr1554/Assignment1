package main.java.edu.uw.ajs.dao;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import main.java.edu.uw.ajs.account.AccountImpl;
import main.java.edu.uw.ajs.account.AddressImpl;
import main.java.edu.uw.ajs.account.CreditCardImpl;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.dao.AccountDao;

public class JsonAccountDao implements AccountDao {

	String ACCOUNTFILENAME = "_Account.json";
	ObjectMapper mapper = new ObjectMapper();
	final File accountDir = new File("target", "/accounts");
	private static final Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

	JsonAccountDao() {
		final SimpleModule module = new SimpleModule();
		module.addAbstractTypeMapping(Account.class, AccountImpl.class);
		module.addAbstractTypeMapping(Address.class, AddressImpl.class);
		module.addAbstractTypeMapping(CreditCard.class, CreditCardImpl.class);
		mapper = new ObjectMapper();
		mapper.registerModule(module);

	}

	public Account getAccount(final String accountInput) {

		Account account = null;

		if (accountDir.exists() && this.accountDir.isDirectory()) {
			try {
				final File accountNameInput = new File(this.accountDir, accountInput + ACCOUNTFILENAME);

				account = mapper.readValue(accountNameInput, Account.class);

			} catch (final IOException ex) {
				logger.info("Account unable to be created");
			}
		}
		return account;
	}

	public void setAccount(Account account) {

		String accountName = account.getName();

		if (!this.accountDir.exists()) {
			this.accountDir.mkdirs();
		}

		final File accountOutput = new File(this.accountDir, accountName + ACCOUNTFILENAME);

		if (accountOutput.exists()) {
			logger.info("DELETE FILE " + accountOutput);
			deleteFile(accountOutput);

		}

		try {
			mapper.writeValue(accountOutput, account);
		} catch (JsonGenerationException e) {
			logger.info("File unable to be written to ");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.info("File unable to be mapped ");
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("File unable to be created ");
			e.printStackTrace();
		}

		// Create Account Name
		// Create a file with Account name
		// Check to see if directory exists
		// If it doesn't then try to create one
		// If fails throw exception
		// Check if file exits/if so delte it
		// If fails throw exception
		// Write with prettyPrinter
		// Write with mapper to output file
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
		File dir = accountDir;
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

	@Override
	public void close() throws AccountException {
		// TODO Auto-generated method stub

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
}
