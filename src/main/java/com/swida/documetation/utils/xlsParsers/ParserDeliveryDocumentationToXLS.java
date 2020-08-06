package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import lombok.AllArgsConstructor;
import lombok.Data;
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

@Data
@AllArgsConstructor
public class ParserDeliveryDocumentationToXLS {
    private DeliveryDocumentation deliveryDocumentation;

    public String parse() {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of RawStorage");
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);


        sheet.setColumnWidth(0,5000);
        sheet.setColumnWidth(1,4500);
        sheet.setColumnWidth(2,2000);
        sheet.setColumnWidth(4,1500);
        sheet.setColumnWidth(5,1500);
        sheet.setColumnWidth(6,3000);
        sheet.setColumnWidth(7,2000);
        sheet.setColumnWidth(8,2500);
        sheet.setColumnWidth(9,2000);
        sheet.setColumnWidth(10,2000);
        sheet.setColumnWidth(11,2000);
        sheet.setColumnWidth(12,2300);


        int rowCount = 0;
        float sumExtent = 0;
        int sumCount = 0;

        for(int i =0; i<12;i++){
            sheet.createRow(i).setHeight((short) 400);
            for(int j=0;j<13;j++){
                sheet.getRow(i).createCell(j).setCellValue("");
                sheet.getRow(i).getCell(j).setCellStyle(style);
            }
        }



        Row rowHeader = sheet.getRow(0);

        rowHeader.setHeight((short) 400);
        rowHeader.getCell(0);
        rowHeader.getCell(1).setCellValue("Код");
        rowHeader.getCell(2).setCellValue("Порода");
        rowHeader.getCell(3).setCellValue("Описание");
        rowHeader.getCell(4).setCellValue("Тол, мм");
        rowHeader.getCell(5).setCellValue("Шир, мм");
        rowHeader.getCell(6).setCellValue("Длина, мм");
        rowHeader.getCell(7).setCellValue("Доски, шт");
        rowHeader.getCell(8).setCellValue("Кубатура,м3");
        rowHeader.getCell(9).setCellValue("Выс,шт");
        rowHeader.getCell(10).setCellValue("Шир,шт");
        rowHeader.getCell(11).setCellValue("Дл-ФТ");
        rowHeader.getCell(12).setCellValue("Вис/Шир");

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
        rowHeader.getCell(12).setCellStyle(style);

        for(int i = 0; i<deliveryDocumentation.getProductList().size();i++){
            rowCount++;
            PackagedProduct product = deliveryDocumentation.getProductList().get(i);
            sumCount+=Integer.parseInt(product.getCountOfDesk());
            sumExtent+=Float.parseFloat(product.getExtent());
            Row row;
            if (i<12){
                row = sheet.getRow(rowCount);
                row.getCell(1).setCellValue(product.getCodeOfPackage());
                row.getCell(2).setCellValue(product.getBreedOfTree().getBreed());
                row.getCell(3).setCellValue(product.getBreedDescription());
                row.getCell(4).setCellValue(product.getSizeOfHeight());
                row.getCell(5).setCellValue(product.getSizeOfWidth());
                row.getCell(6).setCellValue(product.getSizeOfLong());
                row.getCell(7).setCellValue(product.getCountOfDesk());
                row.getCell(8).setCellValue(product.getExtent());
                row.getCell(9).setCellValue(product.getCountDeskInHeight());
                row.getCell(10).setCellValue(product.getCountDeskInWidth());
                row.getCell(11).setCellValue(product.getLongFact());
                row.getCell(12).setCellValue(product.getHeight_width());
            }else{
                row = sheet.createRow(rowCount);
                row.setHeight((short)400);
                row.createCell(0);
                row.createCell(1).setCellValue(product.getCodeOfPackage());
                row.createCell(2).setCellValue(product.getBreedOfTree().getBreed());
                row.createCell(3).setCellValue(product.getBreedDescription());
                row.createCell(4).setCellValue(product.getSizeOfHeight());
                row.createCell(5).setCellValue(product.getSizeOfWidth());
                row.createCell(6).setCellValue(product.getSizeOfLong());
                row.createCell(7).setCellValue(product.getCountOfDesk());
                row.createCell(8).setCellValue(product.getExtent());
                row.createCell(9).setCellValue(product.getCountDeskInHeight());
                row.createCell(10).setCellValue(product.getCountDeskInWidth());
                row.createCell(11).setCellValue(product.getLongFact());
                row.createCell(12).setCellValue(product.getHeight_width());
            }

            for(int j=0;j<13;j++){
                row.getCell(j).setCellStyle(style);
            }
        }

        sheet.getRow(0).getCell(0).setCellValue(deliveryDocumentation.getDriverInfo().getIdOfTruck());
        sheet.getRow(1).getCell(0).setCellValue("A/M");
        sheet.getRow(2).getCell(0).setCellValue(deliveryDocumentation.getDriverInfo().getNumberOfTruck());
        sheet.getRow(3).getCell(0).setCellValue("П/П");
        sheet.getRow(4).getCell(0).setCellValue(deliveryDocumentation.getDriverInfo().getNumberOfTrailer());
        sheet.getRow(5).getCell(0).setCellValue("ДАТА ВЫГРУЗКИ:");
        sheet.getRow(6).getCell(0).setCellValue(deliveryDocumentation.getDateOfUnloading());
        sheet.getRow(7).getCell(0).setCellValue("ВРЕМЯ ВЫГРУЗКИ:");
        sheet.getRow(8).getCell(0).setCellValue(deliveryDocumentation.getTimeOfUnloading());
        sheet.getRow(9).getCell(0).setCellValue("Водитель:");
        sheet.getRow(10).getCell(0).setCellValue(deliveryDocumentation.getDriverInfo().getFullName());
        sheet.getRow(11).getCell(0).setCellValue(deliveryDocumentation.getDriverInfo().getPhone());


        Row rowFooter = sheet.createRow((rowCount>11)?rowCount:12);
        rowFooter.setHeight((short) 400);
        for(int i=0;i<13;i++){
            rowFooter.createCell(i).setCellValue("");
            rowFooter.getCell(i).setCellStyle(style);
        }
        rowFooter.getCell(7).setCellValue(sumCount);
        rowFooter.getCell(8).setCellValue(String.format("%6.3f",sumExtent));


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
