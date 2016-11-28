package mio.bmt.atomikos;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.transaction.UserTransaction;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;

public class ProvaNonXA {

	public static void main(String[] args) throws Exception {
		
		ProvaNonXA p = new ProvaNonXA();
		p.setUp();

		UserTransaction ut = new UserTransactionImp();
		ut.begin();

		Connection c1 = p.ds1.getConnection();
		Connection c2 = p.ds2.getConnection();

		PreparedStatement statement1 = c1.prepareStatement("insert into acidrest (rest,temp) values (?,?)");
		statement1.setInt(1, 1);
		statement1.setInt(2, 1);
		statement1.executeUpdate();

		PreparedStatement statement2 = c2.prepareStatement("insert into canale (id) values (?)");
		statement2.setInt(1, 3);
		statement2.executeUpdate();

		c1.close();
		c2.close();

		ut.commit();
	}

	public AtomikosNonXADataSourceBean ds1 = new AtomikosNonXADataSourceBean();
	public AtomikosNonXADataSourceBean ds2 = new AtomikosNonXADataSourceBean();

	private void setUp() throws Exception {
		ds1.setUniqueResourceName("uno");
		ds1.setDriverClassName("com.mysql.jdbc.Driver");
		ds1.setUser("root");
		ds1.setPassword("root");
		ds1.setUrl("jdbc:mysql://localhost:3306/mio");
		ds1.setPoolSize(5);

		ds2.setUniqueResourceName("due");
		ds2.setDriverClassName("com.mysql.jdbc.Driver");
		ds2.setUser("root");
		ds2.setPassword("root");
		ds2.setUrl("jdbc:mysql://localhost:3306/crm");
		ds2.setPoolSize(5);
	}

}
