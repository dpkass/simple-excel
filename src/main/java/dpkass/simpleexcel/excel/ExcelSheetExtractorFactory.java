package dpkass.simpleexcel.excel;

import dpkass.simpleexcel.SheetExtractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelSheetExtractorFactory {

  public enum Filetype {XLSX, XLS}

  public static SheetExtractor sheetExtractor(File file, Filetype filetype, String sheetName)
      throws FileNotFoundException {
    if (!file.exists())
      throw new FileNotFoundException();

    Workbook wb = selectWorkbook(file, filetype);

    Sheet s = wb.getSheet(sheetName);
    return new ExcelSheetExtractor(s);
  }

  private static Workbook selectWorkbook(File file, Filetype filetype) {
    try (var is = new FileInputStream(file)) {
      return switch (filetype) {
        case XLSX -> new XSSFWorkbook(is);
        case XLS -> new HSSFWorkbook(is);
      };
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
