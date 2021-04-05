package springbook.user.sqlservice;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
