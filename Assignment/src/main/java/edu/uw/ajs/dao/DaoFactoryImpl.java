package main.java.edu.uw.ajs.dao;

import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.dao.DaoFactoryException;

public class DaoFactoryImpl implements DaoFactory {
	@Override
	public AccountDao getAccountDao() throws DaoFactoryException {

		return new AccountDaoImpl();
	}

}
