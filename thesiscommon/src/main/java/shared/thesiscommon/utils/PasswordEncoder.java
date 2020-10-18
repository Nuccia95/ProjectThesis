package shared.thesiscommon.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordEncoder
{
	private static final String ALGORITHM = "MD5"; //$NON-NLS-1$

	public static byte[] encode(final String password)
	{
		try{
			return MessageDigest.getInstance(ALGORITHM).digest(TextUtils.encode(password));
		} catch (final NoSuchAlgorithmException e)
		{
			//throw new RiducoException(e);
		}
		return null;
	}

	/*
	 * public static void main(final String[] args) throws Exception { /*byte[]
	 * encode = PasswordEncoder.encode("cbipr.app");
	 *
	 * Connection conn = null; // STEP 2: Register JDBC driver
	 * Class.forName("com.mysql.jdbc.Driver");
	 *
	 * // STEP 3: Open a connection
	 * System.out.println("Connecting to database..."); // conn =
	 * DriverManager.getConnection(
	 * "jdbc:mysql://localhost:3306/crystalball_test?rewriteBatchedStatements=true",
	 * "root", "toor"); conn =
	 * DriverManager.getConnection("jdbc:mysql://iprdbprod/crystalball",
	 * "sviluppo", "sviluppo");
	 *
	 * // STEP 4: Execute a query System.out.println("Creating statement...");
	 *
	 * // creates the sql statement that inserts or updates according to the
	 * primary keys id_ref and picture_num
	 *
	 * String sql =
	 * "insert into trusted_organizator values(2,'info@infopowerresearch.it',?,'infopower');"
	 * ; PreparedStatement ps = conn.prepareStatement(sql);
	 * ps.setBinaryStream(1, new ByteArrayInputStream(encode));
	 * ps.executeUpdate(); conn.close();
	 *
	 * // String s = "cbipr"; //
	 * System.out.println(bytesToHex(s.getBytes("UTF-8"))); // byte[] bytes =
	 * s.getBytes("UTF-16"); //
	 * System.out.println(bytesToHex(Arrays.copyOfRange(bytes, 2,
	 * bytes.length))); // System.out.println("UTF-16: " +
	 * bytesToHex(MessageDigest.getInstance(ALGORITHM).digest(Arrays.copyOfRange
	 * (bytes, 2, bytes.length)))); // // // System.out.println(new
	 * String(text.getBytes(CHARSET_NAME), "UTF-8")); // //
	 * System.out.println(bytesToHex(MessageDigest.getInstance(ALGORITHM).digest
	 * (s.getBytes("UTF-8")))); // System.out.println(bytesToHex(encode(s))); }
	 */
	
	 public static void main(final String[] args) {
	
	 String pass = "admin";
	
	 byte[] b = PasswordEncoder.encode(pass);
	 System.out.println(b);
	
	 String s = TextUtils.decode(b);
	 System.out.println(s);
	
	 byte[] b2 = TextUtils.encode(s);
	 System.out.println(b2);
	 }

	private PasswordEncoder()
	{
	}
}
