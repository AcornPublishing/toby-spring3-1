package springbook.learningtest.spring.jdbc;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReadOnlyRoutingDataSource extends AbstractRoutingDataSource {
	protected Object determineCurrentLookupKey() {
		boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
		return readOnly ? "READAONLY" : "READWRITE";
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
