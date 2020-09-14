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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;

@AllArgsConstructor
@Data
public class ParseMultimodalPackagesByContract {
    private List<DeliveryDocumentation> deliveryDocumentation;

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


        sheet.setColumnWidth(0,3000);
        sheet.setColumnWidth(1,3500);
        sheet.setColumnWidth(2,2000);
        sheet.setColumnWidth(3,2000);
        sheet.setColumnWidth(4,1500);
        sheet.setColumnWidth(5,1500);
        sheet.setColumnWidth(6,3000);
        sheet.setColumnWidth(7,2000);
        sheet.setColumnWidth(8,2500);
        sheet.setColumnWidth(9,2000);
        sheet.setColumnWidth(10,2000);
        sheet.setColumnWidth(11,2300);
        sheet.setColumnWidth(12,2300);
        sheet.setColumnWidth(13,3500);


        int rowCount = 0;
        float sumExtent = 0;
        int sumCount = 0;



        Row rowHeader = sheet.createRow(0);

        rowHeader.setHeight((short) 400);
        rowHeader.createCell(0).setCellValue("Машина");
        rowHeader.createCell(1).setCellValue("Код");
        rowHeader.createCell(2).setCellValue("Тол, мм");
        rowHeader.createCell(3).setCellValue("Шир, мм");
        rowHeader.createCell(4).setCellValue("Длина, мм");
        rowHeader.createCell(5).setCellValue("Доски, шт");
        rowHeader.createCell(6).setCellValue("Кубатура,м3");
        rowHeader.createCell(7).setCellValue("Выс,шт");
        rowHeader.createCell(8).setCellValue("Шир,шт");
        rowHeader.createCell(9).setCellValue("№ пачки");
        rowHeader.createCell(10).setCellValue("Дл-ФТ");
        rowHeader.createCell(11).setCellValue("Вис/Шир");
        rowHeader.createCell(12).setCellValue("Контракт");
        rowHeader.createCell(13).setCellValue("Контейнер");

        for(int i=0; i<14;i++){
            rowHeader.getCell(i).setCellStyle(style);
        }


        for(int j=0; j<deliveryDocumentation.size();j++){
            for(int i = 0; i<deliveryDocumentation.get(j).getProductList().size();i++){
                rowCount++;
                PackagedProduct product = deliveryDocumentation.get(j).getProductList().get(i);
                sumCount+=Integer.parseInt(product.getCountOfDesk());
                sumExtent+=Float.parseFloat(product.getExtent());
                Row row;
                    row = sheet.createRow(rowCount);
                    row.createCell(0).setCellValue(deliveryDocumentation.get(j).getDriverInfo().getIdOfTruck());
                    row.createCell(1).setCellValue(product.getCodeOfPackage());
                    row.createCell(2).setCellValue(Integer.parseInt(product.getSizeOfHeight()));
                    row.createCell(3).setCellValue(Integer.parseInt(product.getSizeOfWidth()));
                    row.createCell(4).setCellValue(Integer.parseInt(product.getSizeOfLong()));
                    row.createCell(5).setCellValue(Integer.parseInt(product.getCountOfDesk()));

                    BigDecimal roundfinalPrice = new BigDecimal(Float.parseFloat(product.getExtent())).setScale(3,BigDecimal.ROUND_HALF_UP);
                    row.createCell(6).setCellValue(roundfinalPrice.doubleValue());

                    try {
                        row.createCell(7).setCellValue(Integer.parseInt(product.getCountDeskInHeight()));
                        row.createCell(8).setCellValue(Integer.parseInt(product.getCountDeskInWidth()));
                    }catch (Exception e){
                        row.createCell(7).setCellValue(product.getCountDeskInHeight());
                        row.createCell(8).setCellValue(product.getCountDeskInWidth());
                    }

                    row.createCell(9).setCellValue(product.getCodeOfDeliveryCompany());
                    row.createCell(10).setCellValue(product.getLongFact());
                    row.createCell(11).setCellValue(product.getHeight_width());
                    row.createCell(12).setCellValue((product.getOrderInfo()!=null)?product.getOrderInfo().getCodeOfOrder():"");
                    row.createCell(13).setCellValue((product.getContainer()!=null)?product.getContainer().getCode():"");

                for(int k=0;k<14;k++){
                    row.getCell(k).setCellStyle(style);
                }
            }
        }



        Row rowFooter = sheet.createRow(++rowCount);
        rowFooter.setHeight((short) 400);
        for(int i=0;i<14;i++){
            rowFooter.createCell(i).setCellValue("");
            rowFooter.getCell(i).setCellStyle(style);
        }

        BigDecimal extent = new BigDecimal(sumExtent).setScale(3,BigDecimal.ROUND_HALF_UP);
        rowFooter.getCell(5).setCellValue(sumCount);
        rowFooter.getCell(6).setCellValue(extent.doubleValue());


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
