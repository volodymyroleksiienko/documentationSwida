package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import lombok.AllArgsConstructor;
import lombok.Data;
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

@Data
@AllArgsConstructor
public class ParserDeliveryDocumentationToXLS {
    private DeliveryDocumentation deliveryDocumentation;

    public String parse() {


        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of RawStorage");
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        for (int i = 0; i < 12; i++) {
            sheet.setColumnWidth(i, 3500);
        }

        int rowCount = 0;
        Row rowHeaderDriver = sheet.createRow(rowCount++);
        rowHeaderDriver.createCell(0).setCellValue("№ выгрузки");
        rowHeaderDriver.createCell(1).setCellValue("А/М");
        rowHeaderDriver.createCell(2).setCellValue("П/П");
        rowHeaderDriver.createCell(3).setCellValue("Дата выгрузки");
        rowHeaderDriver.createCell(4).setCellValue("Время выгрузки");
        rowHeaderDriver.createCell(5).setCellValue("Водитель");
        rowHeaderDriver.createCell(6).setCellValue("Телефон");

        rowHeaderDriver.getCell(0).setCellStyle(style);
        rowHeaderDriver.getCell(1).setCellStyle(style);
        rowHeaderDriver.getCell(2).setCellStyle(style);
        rowHeaderDriver.getCell(3).setCellStyle(style);
        rowHeaderDriver.getCell(4).setCellStyle(style);
        rowHeaderDriver.getCell(5).setCellStyle(style);
        rowHeaderDriver.getCell(6).setCellStyle(style);

        Row rowDriver = sheet.createRow(rowCount++);
        rowDriver.createCell(0).setCellValue(deliveryDocumentation.getDriverInfo().getIdOfTruck());
        rowDriver.createCell(1).setCellValue(deliveryDocumentation.getDriverInfo().getNumberOfTruck());
        rowDriver.createCell(2).setCellValue(deliveryDocumentation.getDriverInfo().getNumberOfTrailer());
        rowDriver.createCell(3).setCellValue(deliveryDocumentation.getDateOfUnloading());
        rowDriver.createCell(4).setCellValue(deliveryDocumentation.getTimeOfUnloading());
        rowDriver.createCell(5).setCellValue(deliveryDocumentation.getDriverInfo().getFullName());
        rowDriver.createCell(6).setCellValue(deliveryDocumentation.getDriverInfo().getPhone());

        rowDriver.getCell(0).setCellStyle(style);
        rowDriver.getCell(1).setCellStyle(style);
        rowDriver.getCell(2).setCellStyle(style);
        rowDriver.getCell(3).setCellStyle(style);
        rowDriver.getCell(4).setCellStyle(style);
        rowDriver.getCell(5).setCellStyle(style);
        rowDriver.getCell(6).setCellStyle(style);

        // free space
        sheet.createRow(rowCount++);

        Row rowHeader = sheet.createRow(rowCount++);

        rowHeader.setHeight((short) 400);
        rowHeader.createCell(0).setCellValue("Код");
        rowHeader.createCell(1).setCellValue("Порода");
        rowHeader.createCell(2).setCellValue("Описание");
        rowHeader.createCell(3).setCellValue("Толщина, мм");
        rowHeader.createCell(4).setCellValue("Ширина, мм");
        rowHeader.createCell(5).setCellValue("Длина, мм");
        rowHeader.createCell(6).setCellValue("Кол-во досок, шт");
        rowHeader.createCell(7).setCellValue("Кубатура,м3");
        rowHeader.createCell(8).setCellValue("Высота,шт");
        rowHeader.createCell(9).setCellValue("Ширина,шт");
        rowHeader.createCell(10).setCellValue("Длина-ФАКТ");
        rowHeader.createCell(11).setCellValue("Висота/Ширина");

        rowHeader.getCell(0).setCellStyle(style);
        rowHeader.getCell(1).setCellStyle(style);
        rowHeader.getCell(2).setCellStyle(style);
        rowHeader.getCell(3).setCellStyle(style);
        rowHeader.getCell(4).setCellStyle(style);
        rowHeader.getCell(5).setCellStyle(style);
        rowHeader.getCell(6).setCellStyle(style);
        rowHeader.getCell(7).setCellStyle(style);
        rowHeader.getCell(8).setCellStyle(style);
        rowHeader.getCell(9).setCellStyle(style);
        rowHeader.getCell(10).setCellStyle(style);
        rowHeader.getCell(11).setCellStyle(style);

        for (PackagedProduct product : deliveryDocumentation.getProductList()) {
                    Row row = sheet.createRow(rowCount++);
                    row.setHeight((short) 400);
                    row.createCell(0).setCellValue(product.getCodeOfPackage());
                    row.createCell(1).setCellValue(product.getBreedOfTree().getBreed());
                    row.createCell(2).setCellValue(product.getBreedDescription());
                    row.createCell(3).setCellValue(product.getSizeOfHeight());
                    row.createCell(4).setCellValue(product.getSizeOfWidth());
                    row.createCell(5).setCellValue(product.getSizeOfLong());
                    row.createCell(6).setCellValue(product.getCountOfDesk());
                    row.createCell(7).setCellValue(product.getExtent());
                    row.createCell(8).setCellValue(product.getCountDeskInHeight());
                    row.createCell(9).setCellValue(product.getCountDeskInWidth());
                    row.createCell(10).setCellValue(product.getLongFact());
                    row.createCell(11).setCellValue(product.getHeight_width());
                    row.getCell(0).setCellStyle(style);
                    row.getCell(1).setCellStyle(style);
                    row.getCell(2).setCellStyle(style);
                    row.getCell(3).setCellStyle(style);
                    row.getCell(4).setCellStyle(style);
                    row.getCell(5).setCellStyle(style);
                    row.getCell(6).setCellStyle(style);
                    row.getCell(7).setCellStyle(style);
                    row.getCell(8).setCellStyle(style);
                    row.getCell(9).setCellStyle(style);
                    row.getCell(10).setCellStyle(style);
                    row.getCell(11).setCellStyle(style);
        }

        try {
            String filePath = System.getProperty("user.home") + File.separator + "delivery.xlsx";
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            book.write(fos);
            fos.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
