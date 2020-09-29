package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.TreeStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseTreeStorageToXLS {
    private List<TreeStorage> treeStorage;

    public String parse(String startDate, String endDate ) throws ParseException {
        Date after = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date before = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of TreeStorage");
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        for (int i = 0; i<7;i++){
            sheet.setColumnWidth(i,4500);
        }

        int rowCount = 0;
        Row rowHeader = sheet.createRow(rowCount++);
        rowHeader.setHeight((short)400);
        rowHeader.createCell(0).setCellValue("Код партии");
        rowHeader.createCell(1).setCellValue("Порода");
        rowHeader.createCell(2).setCellValue("Описание");
        rowHeader.createCell(3).setCellValue("Поставщик");
        rowHeader.createCell(4).setCellValue("На складе,м3");
        rowHeader.createCell(5).setCellValue("Было,м3");
        rowHeader.createCell(6).setCellValue("Дата");
        rowHeader.getCell(0).setCellStyle(style);
        rowHeader.getCell(1).setCellStyle(style);
        rowHeader.getCell(2).setCellStyle(style);
        rowHeader.getCell(3).setCellStyle(style);
        rowHeader.getCell(4).setCellStyle(style);
        rowHeader.getCell(5).setCellStyle(style);
        rowHeader.getCell(6).setCellStyle(style);

        for(TreeStorage ts: treeStorage){
           if (ts.getDate()!=null) {
               Date dateOfInsert = new SimpleDateFormat("yyyy-MM-dd").parse(ts.getDate());
               if (dateOfInsert.before(before) && dateOfInsert.after(after)) {
                   Row row = sheet.createRow(rowCount++);
                   row.setHeight((short) 400);
                   row.createCell(0).setCellValue(ts.getCodeOfProduct());
                   row.createCell(1).setCellValue(ts.getBreedOfTree().getBreed());
                   row.createCell(2).setCellValue(ts.getBreedDescription());
                   row.createCell(3).setCellValue(ts.getContrAgent().getNameOfAgent());

                   BigDecimal extent = new BigDecimal(Float.parseFloat(ts.getExtent())).setScale(3,BigDecimal.ROUND_HALF_UP);
                   row.createCell(4).setCellValue(extent.doubleValue());

                   if(ts.getMaxExtent()!=null) {
                       BigDecimal maxExtent = new BigDecimal(Float.parseFloat(ts.getMaxExtent())).setScale(3, BigDecimal.ROUND_HALF_UP);
                       row.createCell(5).setCellValue(maxExtent.doubleValue());
                   }else {
                       row.createCell(5).setCellValue("");
                   }
                   row.createCell(6).setCellValue(ts.getDate());
                   row.getCell(0).setCellStyle(style);
                   row.getCell(1).setCellStyle(style);
                   row.getCell(2).setCellStyle(style);
                   row.getCell(3).setCellStyle(style);
                   row.getCell(4).setCellStyle(style);
                   row.getCell(5).setCellStyle(style);
                   row.getCell(6).setCellStyle(style);
               }
           }
        }

        try {
            String filePath = System.getProperty("user.home")+File.separator+"Кругляк.xlsx";
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            book.write(fos);
            fos.close();
            return filePath;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
