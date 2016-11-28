package mio.bmt.atomikos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

import javax.transaction.UserTransaction;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;

public class ProvaXA {

	public AtomikosDataSourceBean ds1 = new AtomikosDataSourceBean();
	
	public AtomikosDataSourceBean ds2 = new AtomikosDataSourceBean();
	
	public static final int TIMEOUT = 3000000;
	
	
	/**
	 *
	  CREATE TABLE `atomikos_mio`.`acidrest` (
	  `rest` INT NOT NULL,
	  `temp` INT NULL,
	  PRIMARY KEY (`rest`));
	  
	  CREATE TABLE `atomikos_crm`.`canale` (
		  `id` INT NOT NULL,
	  PRIMARY KEY (`id`));	  
	 * 
	 * 
	 * 
	 * @throws Exception
	 */
	private void setUp() throws Exception{
		
		ds1.setUniqueResourceName("uno"); 
		ds1.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
		ds1.setReapTimeout( TIMEOUT );
		
		Properties p1 = new Properties(); 
		p1.setProperty ( "user" , "root" ); 
		p1.setProperty ( "password" , "comedsh006" ); 
		p1.setProperty ( "URL" , "jdbc:mysql://localhost:3306/atomikos_mio" ); 
		ds1.setXaProperties ( p1 ); 
		
		ds1.setPoolSize ( 5 );
		
		ds2.setUniqueResourceName("due"); 
		ds2.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource"); 
		ds2.setReapTimeout( TIMEOUT );
		
		Properties p2 = new Properties(); 
		p2.setProperty ( "user" , "root" ); 
		p2.setProperty ( "password" , "comedsh006" ); 
		p2.setProperty ( "URL" , "jdbc:mysql://localhost:3306/atomikos_crm" );
		
		ds2.setXaProperties ( p2 ); 
		ds2.setPoolSize ( 5 );
		
		// clean up the data
		Utils.execute( Utils.getConnection("atomikos_mio"), "delete from acidrest" );
		Utils.execute( Utils.getConnection("atomikos_crm"), "delete from canale" );
		
	}

	public static void main(String[] args) throws Exception{

		ProvaXA p = new ProvaXA();
		p.setUp();
		
		/**
		 * ut 是如何与 XA Resource 关联起来的？
		 * 
		 * 是通过 Hashtable BaseTransactionManager#threadtoxmap，为每一个当前的线程维护了一份 CompositeTransactionImp，而 CompositeTransactionImp 就相当于 Root Transaction，
		 * 其属性 CoordinatorImp 维护了所有相关的 XA Resources...
		 */
		UserTransaction ut = new UserTransactionImp();
		ut.setTransactionTimeout(TIMEOUT);
		ut.begin();
		
		Connection c1 = p.ds1.getConnection();
		Connection c2 = p.ds2.getConnection();
		
		PreparedStatement statement1 = c1.prepareStatement("insert into acidrest (rest,temp) values (?,?)");
		statement1.setInt(1,1);
		statement1.setInt(2,1);
		statement1.executeUpdate();
		
		PreparedStatement statement12 = c1.prepareStatement("insert into acidrest (rest,temp) values (?,?)");
		statement12.setInt(1,2);
		statement12.setInt(2,2);
		statement12.executeUpdate();
		
		PreparedStatement statement2 = c2.prepareStatement("insert into canale (id) values (?)");
		statement2.setInt(1,2);
		statement2.executeUpdate();		
		
		c1.close();
		c2.close();
		
		System.out.println("trying to commit ~ ");
		
		ut.commit();
	}	
	
}
