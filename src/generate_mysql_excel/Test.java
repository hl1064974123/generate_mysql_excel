/**
 * 
 */
package generate_mysql_excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author HELEI
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	public static void generate() {
		
	}
	public static void generateT(){
		XSSFWorkbook  workbook = new XSSFWorkbook();
		try {
		//新创建的xls需要新创建新的工作簿，offine默认创建的时候会默认生成三个sheet
		Sheet sheet = workbook.createSheet("first sheet");
		FileOutputStream out = new FileOutputStream("createWorkBook.xlsx");
		workbook.write(out);
		out.close();
		System.out.println("createWorkBook success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			generate();
	}

}
