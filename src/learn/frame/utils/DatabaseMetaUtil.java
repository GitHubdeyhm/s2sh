package learn.frame.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库分析工具
 * @Date 2017-1-8下午8:53:11
 */
public class DatabaseMetaUtil {
	
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/s2shframe";//mysql数据库语法
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, "root","root");
			//得到分析数据库对象
			DatabaseMetaData dbMeta = conn.getMetaData();
			//获取同时支持的最多Statement对象总数。为0意味着没有限制或限制是未知的 
			System.out.println(dbMeta.getMaxStatements());
			//获取此数据库是否支持事务。如果不支持，则调用 commit 方法是无操作 (noop)，并且隔离级别是 TRANSACTION_NONE。 
			System.out.println(dbMeta.supportsTransactions());
			//事务的隔离级别
			System.out.println(dbMeta.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE));
			//事务保存点
			System.out.println(dbMeta.supportsSavepoints());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
