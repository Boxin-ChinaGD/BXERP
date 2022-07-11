package wpos.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** 操作Excel表格的功能类 */
public class PoiUtils {
    // 读取excel表用一个对象即可，否则要new很多对象，读取excel很慢
    protected Workbook wb;

    private PoiUtils() {
    }

    public PoiUtils(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        if (filePath.endsWith(".xls")) {
            wb = new HSSFWorkbook(inputStream); // 处理以.xls结尾的excel
        } else {
            wb = new XSSFWorkbook(inputStream);// 处理以.xlsx结尾的excel
        }
    }

    /** @return -1 读取文件发生意外 */
    public int readExcelSheetNO(String file) {
        try {
            int sheetNO = wb.getNumberOfSheets();
            wb.close();
            return sheetNO;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /** @return 空串，读取文件发生意外 */
    public String readExcelSheetName(String file, int sheetAtNO) {
        try {
            String sheetName = wb.getSheetName(sheetAtNO);
            wb.close();
            return sheetName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /** 读取Excel表格一行内容
     *
     * @param file
     *            要读取的文件路径
     * @param sheetAtNO
     *            读取第n+1个表格
     * @param rowNO
     *            读取第n+1行
     * @return null，读取文件发生意外 */
    public List<String> readExcelRow(String file, int sheetAtNO, int rowNO) {
        try {
            // 解析公式结果
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = wb.getSheetAt(sheetAtNO);
            List<String> listRow = new ArrayList<String>();
            listRow = readRow(rowNO, evaluator, sheet);
            wb.close();
            return listRow;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** @param file
     *            要读取的文件路径
     * @param sheetAtNO
     *            读取名称为sheetName的表格
     * @param rowNO
     *            读取第n+1行
     * @return null，读取文件发生意外 */
    public List<String> readExcelRow(String file, String sheetName, int rowNO) {
        // 解析公式结果
        try {
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = wb.getSheet(sheetName);
            List<String> listRow = new ArrayList<String>();
            listRow = readRow(rowNO, evaluator, sheet);
            wb.close();
            return listRow;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 读取一行数据 */
    protected List<String> readRow(int rowNO, FormulaEvaluator evaluator, Sheet sheet) {
        Row titleRow = sheet.getRow(0);// 第一行的条数
        Row row = sheet.getRow(rowNO);
        // 首行总列数
        int titleColNum = titleRow.getPhysicalNumberOfCells();
        // System.out.println("首行总列数titleColNum:" + titleColNum);
        // // 这行总列数
        // int colNum = row.getPhysicalNumberOfCells();
        // System.out.println("总列数colNum:" + colNum);
        List<String> listRow = new ArrayList<String>();
        for (int i = 0; i < titleColNum; i++) {
            Cell cell = row.getCell((short) i);
            CellValue cellValue = evaluator.evaluate(cell);
            if (cellValue == null) {
                listRow.add(null);
                continue;
            }
            listRow.add(getCellFormatValue(rowNO, i, cellValue));
        }
        return listRow;
    }

    /** 读取Excel表格一列内容
     *
     * @return null，读取文件发生意外 */
    public List<String> readExcelCell(String file, int sheetAtNO, int cellNO) {
        List<String> listRow = new ArrayList<String>();
        // 解析公式结果
        try {
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = wb.getSheetAt(sheetAtNO);
            listRow = doReadExcelCell(cellNO, evaluator, sheet);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return listRow;
    }

    /** 读取Excel表格一列内容 */
    public List<String> readExcelCell(String file, String sheetName, int cellNO) {
        List<String> listRow = new ArrayList<String>();
        // 解析公式结果
        try {
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = wb.getSheet(sheetName);
            listRow = doReadExcelCell(cellNO, evaluator, sheet);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return listRow;
    }

    private List<String> doReadExcelCell(int cellNO, FormulaEvaluator evaluator, Sheet sheet) {
        int maxRow = sheet.getPhysicalNumberOfRows(); // 总行数
        System.out.println("总行数maxRow:" + maxRow);
        List<String> listRow = new ArrayList<String>();
        for (int i = 0; i < maxRow; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell((short) cellNO);
            CellValue cellValue = evaluator.evaluate(cell);
            if (cellValue == null) {
                listRow.add(null);
                continue;
            }
            listRow.add(getCellFormatValue(i, cellNO, cellValue));
        }
        return listRow;
    }

    /** 根据Cell类型设置数据
     *
     * @param i
     * @param rowNO
     *
     * @param cellValue
     * @return */
    private String getCellFormatValue(int rowNO, int column, CellValue cellValue) {
        String cellStringValueOf = "";
        if (cellValue != null) {
            // 经过公式转换，函数只剩下数值型、布尔型、字符串型
            // 判断当前Cell的Type
            switch (cellValue.getCellTypeEnum()) {
                case NUMERIC: // 数值型
                    if (String.valueOf(cellValue.getNumberValue()).contains(".")) {
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        numberFormat.setGroupingUsed(false);
                        cellStringValueOf = numberFormat.format(cellValue.getNumberValue());
                    } else {
                        cellStringValueOf = String.valueOf(cellValue.getNumberValue());
                    }
                    break;
                case BOOLEAN: // 布尔型
                    cellStringValueOf = String.valueOf(cellValue.getBooleanValue());
                    break;
                case ERROR: // 错误
                    System.out.println("读取错误" + ",行：" + rowNO + ",列：" + column);
                    break;
                case BLANK: // 空值
                    break;
                case FORMULA: // 公式型
                    break;
                default: // 字符串型
                    cellStringValueOf = cellValue.getStringValue();
                    break;
            }
        } else {
            System.out.println("读取错误");
        }
        return cellStringValueOf;
    }
}