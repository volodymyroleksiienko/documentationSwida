package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.DryStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  ParseDryStorageToXLS {
    private List<DryStorage> dryStorages;

    public String parse(String startDate, String endDate ) throws ParseException {
        Date after = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date before = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of RawStorage");
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        for (int i = 0; i<8;i++){
            sheet.setColumnWidth(i,3600);
        }

        int rowCount = 0;
        Row rowHeader = sheet.createRow(rowCount++);
        rowHeader.setHeight((short)400);
        rowHeader.createCell(0).setCellValue("Код партии");
        rowHeader.createCell(1).setCellValue("Порода");
        rowHeader.createCell(2).setCellValue("Описание");
        rowHeader.createCell(3).setCellValue("Толщина, мм");
        rowHeader.createCell(4).setCellValue("Ширина, мм");
        rowHeader.createCell(5).setCellValue("Длина, мм");
        rowHeader.createCell(6).setCellValue("Доски, шт");
        rowHeader.createCell(7).setCellValue("Кубатура,м3");
        rowHeader.getCell(0).setCellStyle(style);
        rowHeader.getCell(1).setCellStyle(style);
        rowHeader.getCell(2).setCellStyle(style);
        rowHeader.getCell(3).setCellStyle(style);
        rowHeader.getCell(4).setCellStyle(style);
        rowHeader.getCell(5).setCellStyle(style);
        rowHeader.getCell(6).setCellStyle(style);
        rowHeader.getCell(7).setCellStyle(style);

        for(DryStorage rs: dryStorages){
            if (rs.getDate()!=null) {
                Date dateOfInsert = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getDate());
                if (dateOfInsert.before(before) && dateOfInsert.after(after)) {
                    Row row = sheet.createRow(rowCount++);
                    row.setHeight((short) 400);
                    row.createCell(0).setCellValue(rs.getCodeOfProduct());
                    row.createCell(1).setCellValue(rs.getBreedOfTree().getBreed());
                    row.createCell(2).setCellValue(rs.getBreedDescription());
                    row.createCell(3).setCellValue(rs.getSizeOfHeight());
                    row.createCell(4).setCellValue(rs.getSizeOfWidth());
                    row.createCell(5).setCellValue(rs.getSizeOfLong());
                    row.createCell(6).setCellValue(rs.getCountOfDesk());
                    row.createCell(7).setCellValue(rs.getExtent());
                    row.getCell(0).setCellStyle(style);
                    row.getCell(1).setCellStyle(style);
                    row.getCell(2).setCellStyle(style);
                    row.getCell(3).setCellStyle(style);
                    row.getCell(4).setCellStyle(style);
                    row.getCell(5).setCellStyle(style);
                    row.getCell(6).setCellStyle(style);
                    row.getCell(7).setCellStyle(style);
                }
            }
        }

        try {
            String filePath = System.getProperty("user.home")+ File.separator+"Сырой_склад.xlsx";
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

    public String parseOAK(String startDate, String endDate ) throws ParseException {
        Date after = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date before = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of RawStorage");
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        for (int i = 0; i<6;i++){
            sheet.setColumnWidth(i,7000);
        }

        int rowCount = 0;
        Row rowHeader = sheet.createRow(rowCount++);
        rowHeader.setHeight((short)400);
        rowHeader.createCell(0).setCellValue("Код партии");
        rowHeader.createCell(1).setCellValue("Порода");
        rowHeader.createCell(2).setCellValue("Описание");
        rowHeader.createCell(3).setCellValue("Толщина, мм");
        rowHeader.createCell(4).setCellValue("Кубатура,м3");
        rowHeader.createCell(5).setCellValue("Примечания");
        rowHeader.getCell(0).setCellStyle(style);
        rowHeader.getCell(1).setCellStyle(style);
        rowHeader.getCell(2).setCellStyle(style);
        rowHeader.getCell(3).setCellStyle(style);
        rowHeader.getCell(4).setCellStyle(style);
        rowHeader.getCell(5).setCellStyle(style);

        for(DryStorage rs: dryStorages){
            if (rs.getDate()!=null) {
                Date dateOfInsert = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getDate());
                if (dateOfInsert.before(before) && dateOfInsert.after(after)) {
                    Row row = sheet.createRow(rowCount++);
                    row.setHeight((short) 400);
                    row.createCell(0).setCellValue(rs.getCodeOfProduct());
                    row.createCell(1).setCellValue(rs.getBreedOfTree().getBreed());
                    row.createCell(2).setCellValue(rs.getBreedDescription());
                    row.createCell(3).setCellValue(rs.getSizeOfHeight());
                    row.createCell(4).setCellValue(rs.getExtent());
                    row.createCell(5).setCellValue(rs.getDescription());
                    row.getCell(0).setCellStyle(style);
                    row.getCell(1).setCellStyle(style);
                    row.getCell(2).setCellStyle(style);
                    row.getCell(3).setCellStyle(style);
                    row.getCell(4).setCellStyle(style);
                    row.getCell(5).setCellStyle(style);
                }
            }
        }

        try {
            String filePath = System.getProperty("user.home")+ File.separator+"Сырой_склад.xlsx";
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
