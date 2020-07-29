package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.TreeStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseTreeStorageToXLS {
    private List<TreeStorage> treeStorage;

    public String parse(){
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of TreeStorage");
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        for (int i = 0; i<5;i++){
            sheet.setColumnWidth(i,7000);
        }

        int rowCount = 0;
        Row rowHeader = sheet.createRow(rowCount++);
        rowHeader.setHeight((short)400);
        rowHeader.createCell(0).setCellValue("Код партии");
        rowHeader.createCell(1).setCellValue("Порода");
        rowHeader.createCell(2).setCellValue("Описание");
        rowHeader.createCell(3).setCellValue("Поставщик");
        rowHeader.createCell(4).setCellValue("Кубатура,м3");

        for(TreeStorage ts: treeStorage){
            Row row = sheet.createRow(rowCount++);
            row.setHeight((short)400);
            row.createCell(0).setCellValue(ts.getCodeOfProduct());
            row.createCell(1).setCellValue(ts.getBreedOfTree().getBreed());
            row.createCell(2).setCellValue(ts.getBreedDescription());
            row.createCell(3).setCellValue(ts.getContrAgent().getNameOfAgent());
            row.createCell(4).setCellValue(ts.getExtent());
        }

        try {
            String filePath = System.getProperty("user.home")+File.separator+"treeStorage.xls";
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
