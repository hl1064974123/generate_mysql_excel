/**
 * 
 */
package generate_mysql_excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author HELEI
 *
 */
public class Generate {

	/**
	 * 
	 */
	public Generate() {
		// TODO Auto-generated constructor stub
	}

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/ai?useUnicode=true&characterEncoding=utf8";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";

	private static final String SQL = "SELECT * FROM ";// ���ݿ����

	/**
	 * ��ȡ���ݿ�����
	 *
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.println("get connection failure");
		}
		return conn;
	}

	/**
	 * �ر����ݿ�����
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("get connection failure");
			}
		}
	}

	/**
	 * ��ȡ���ݿ��µ����б���
	 */
	public static List<String> getTableNames() {
		List<String> tableNames = new ArrayList<>();
		Connection conn = getConnection();
		ResultSet rs = null;
		try {
			// ��ȡ���ݿ��Ԫ����
			DatabaseMetaData db = conn.getMetaData();
			// ��Ԫ�����л�ȡ�����еı���
			rs = db.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				tableNames.add(rs.getString(3));
			}
		} catch (SQLException e) {
			System.out.println("get connection failure");
		} finally {
			try {
				rs.close();
				closeConnection(conn);
			} catch (SQLException e) {
				System.out.println("get connection failure");
			}
		}
		return tableNames;
	}

	/**
	 * ��ȡ���������ֶ�����
	 * 
	 * @param tableName
	 *            ����
	 * @return
	 */
	public static List<String> getColumnNames(String tableName) {
		List<String> columnNames = new ArrayList<>();
		// �����ݿ������
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		try {
			pStemt = conn.prepareStatement(tableSql);
			// �����Ԫ����
			ResultSetMetaData rsmd = pStemt.getMetaData();
			// ������
			int size = rsmd.getColumnCount();
			for (int i = 0; i < size; i++) {
				columnNames.add(rsmd.getColumnName(i + 1));
			}
		} catch (SQLException e) {
			System.out.println("get connection failure");
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					System.out.println("getColumnNames close pstem and connection failure");
				}
			}
		}
		return columnNames;
	}

	/**
	 * ��ȡ���������ֶ�����
	 * 
	 * @param tableName
	 * @return
	 */
	public static List<String> getColumnTypes(String tableName) {
		List<String> columnTypes = new ArrayList<>();
		// �����ݿ������
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		try {
			pStemt = conn.prepareStatement(tableSql);
			// �����Ԫ����
			ResultSetMetaData rsmd = pStemt.getMetaData();
			// ������
			int size = rsmd.getColumnCount();
			for (int i = 0; i < size; i++) {
				columnTypes.add(rsmd.getColumnTypeName(i + 1));
			}
		} catch (SQLException e) {
			System.out.println("get connection failure");
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					System.out.println("getColumnTypes close pstem and connection failure");
				}
			}
		}
		return columnTypes;
	}

	/**
	 * ��ȡ�����ֶε�����ע��
	 * 
	 * @param tableName
	 * @return
	 */
	public static List<String> getColumnComments(String tableName) {
		List<String> columnTypes = new ArrayList<>();
		// �����ݿ������
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		List<String> columnComments = new ArrayList<>();// ����ע�ͼ���
		ResultSet rs = null;
		try {
			pStemt = conn.prepareStatement(tableSql);
			rs = pStemt.executeQuery("show full columns from " + tableName);
			while (rs.next()) {
				columnComments.add(rs.getString("Comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
					closeConnection(conn);
				} catch (SQLException e) {
					System.out.println("getColumnComments close ResultSet and connection failure");
				}
			}
		}
		return columnComments;
	}

	/**
	 * ��ȡ�����ֶεĳ���
	 * 
	 * @param tableName
	 * @param charLen
	 * @return
	 */
	public static List<String> getColumnLength(String tableName, String charLen) {
		List<String> columnTypes = new ArrayList<>();
		// �����ݿ������
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = "SELECT CHARACTER_MAXIMUM_LENGTH FROM " + tableName + "where COLUMN_NAME =" + charLen;
		List<String> columnComments = new ArrayList<>();// ����ע�ͼ���
		ResultSet rs = null;
		try {
			pStemt = conn.prepareStatement(tableSql);
			rs = pStemt.executeQuery("show full columns from " + tableName);
			while (rs.next()) {
				columnComments.add(rs.getString("Comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
					closeConnection(conn);
				} catch (SQLException e) {
					System.out.println("getColumnComments close ResultSet and connection failure");
				}
			}
		}
		return columnComments;
	}

	public static void saveFile(String fileName, String text)
			throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File(fileName);
		PrintWriter pWriter = new PrintWriter(fileName, "utf-8");
		pWriter.write("sss");
		pWriter.close();
	}

	/**
	 * ÿ�����ݱ�����һ��������excel�ļ�
	 */
	public static void generateOne() {
		List<String> tableNames = getTableNames();
		System.out.println("tableNames:" + tableNames);
		for (String tableName : tableNames) {
			String[] title = { "�ֶ�����", "�ֶ�����", "�ֶ�ע��" };
			// ����excel������
			XSSFWorkbook workbook = new XSSFWorkbook();
			// ����������sheet
			Sheet sheet = workbook.createSheet("new sheet");
			// ������һ��
			Row row = sheet.createRow(0);
			Cell cell = null;
			// �����һ�����ݵı�ͷ
			for (int i = 0; i < title.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(title[i]);
			}
			// д������
			for (int i = 1; i <= 10; i++) {

			}
			List<String> columnNames = getColumnNames(tableName);
			for (int i = 0; i < columnNames.size(); i++) {
				Row nrow = sheet.createRow(i);
				Cell ncell = nrow.createCell(0);
				ncell.setCellValue(columnNames.get(i));
				ncell = nrow.createCell(1);
				ncell.setCellValue(getColumnTypes(tableName).get(i));
				ncell = nrow.createCell(2);
				ncell.setCellValue(getColumnComments(tableName).get(i));
			}

			// ����excel�ļ�
			try {
				// ��excelд��
				FileOutputStream stream = new FileOutputStream("f://" + tableName + ".xlsx");
				workbook.write(stream);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("ok");
			// System.out.println("ColumnNames:" + getColumnNames(tableName).size());
			// System.out.println("ColumnTypes:" + getColumnTypes(tableName));
			// System.out.println("ColumnComments:" + getColumnComments(tableName));
		}
	}

	/**
	 * ��������ݱ�������һ��excel������
	 * 
	 * @param fileName Ҫ���ɵĹ�����������
	 */
	public static void generateTwo(String fileName) {
		List<String> tableNames = getTableNames();
		System.out.println("tableNames:" + tableNames);
		XSSFWorkbook workbook = new XSSFWorkbook();
		String[] title = { "�ֶ�����", "�ֶ�����", "�ֶ�ע��" };
		for (String tableName : tableNames) {
			// ����excel������
			// ����������sheet
			Sheet sheet = workbook.createSheet(tableName);
			// ������һ��
			Row row = sheet.createRow(0);
			Cell cell = null;
			// �����һ�����ݵı�ͷ
			for (int i = 0; i < title.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(title[i]);
			}
			// д������
			List<String> columnNames = getColumnNames(tableName);
			for (int i = 0; i < columnNames.size(); i++) {
				Row nrow = sheet.createRow(i);
				Cell ncell = nrow.createCell(0);
				ncell.setCellValue(columnNames.get(i));
				ncell = nrow.createCell(1);
				ncell.setCellValue(getColumnTypes(tableName).get(i));
				ncell = nrow.createCell(2);
				ncell.setCellValue(getColumnComments(tableName).get(i));
			}
		}
		// ����excel�ļ�
		try {
			// ��excelд��
			FileOutputStream stream = new FileOutputStream("f://" + fileName + ".xlsx");
			workbook.write(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ok");
		// System.out.println("ColumnNames:" + getColumnNames(tableName).size());
		// System.out.println("ColumnTypes:" + getColumnTypes(tableName));
		// System.out.println("ColumnComments:" + getColumnComments(tableName));
	}

	public static void main(String[] args) {
		generateTwo("����ӿ�");
	}
}
