package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class ParseOakDeliveryInfoToXLS {
    DeliveryDocumentation deliveryDocumentation;
    private List<String> unicWidthList;
    private Map<String,Integer> sumWidthList;
    private String mainSumWidthOfAllDesk;
    private String countOfAllDesk;
    private String mainExtent;

    public ParseOakDeliveryInfoToXLS(DeliveryDocumentation deliveryDocumentation) {
        this.deliveryDocumentation = deliveryDocumentation;
        unicWidthList = new ArrayList<>();
        sumWidthList = new HashMap<>();
        //initialize unic list
        getUnicWidth();
        //initialize map
        getSumByWidth();
    }

    public String parse() {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of RawStorage");
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        Font newFont = sheet.getWorkbook().createFont();
        newFont.setFontHeightInPoints((short) 8);
        style.setFont(newFont);

        int countRow = 0;

        //for driver info
        for(int i=0;i<4;i++){
            sheet.createRow(countRow++);
        }

        Row rowHeader1 = sheet.createRow(countRow++);
        rowHeader1.setHeight((short)600);
        rowHeader1.createCell(0).setCellValue("Quality");
        rowHeader1.createCell(1).setCellValue("SIZE");
        rowHeader1.createCell(2).setCellValue("PKG");
        rowHeader1.createCell(3).setCellValue("Lenght");

        Row rowHeader2 = sheet.createRow(countRow++);
        rowHeader2.setHeight((short)300);
        rowHeader2.createCell(0);
        rowHeader2.createCell(1);
        rowHeader2.createCell(2);
        rowHeader2.createCell(3);

        sheet.addMergedRegion(new CellRangeAddress(4, 5, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 3, 3));


//        create width block

        int widthColCount = unicWidthList.size();
        int lastIndex = widthColCount+6;
        System.out.println("Col    " + widthColCount);
        rowHeader1.createCell(4).setCellValue("Width, mm");
        createEmptyRow(rowHeader1,5,lastIndex);
        rowHeader1.getCell(4).setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 4, lastIndex));

        for(int i=0;i<unicWidthList.size();i++){
            rowHeader2.createCell(i+4).setCellValue(unicWidthList.get(i));
        }
        rowHeader2.createCell(lastIndex).setCellValue("m3");
        rowHeader2.createCell(lastIndex-1).setCellValue("К-во");
        rowHeader2.createCell(lastIndex-2).setCellValue("Сум шир");




        for (PackagedProduct packagedProduct: deliveryDocumentation.getProductList()) {
            Row row = sheet.createRow(countRow++);
            row.setHeight((short)400);
            row.createCell(0).setCellValue(packagedProduct.getQuality());
            row.createCell(1).setCellValue(packagedProduct.getSizeOfHeight());
            row.createCell(2).setCellValue(packagedProduct.getCodeOfPackage());
            row.createCell(3).setCellValue(packagedProduct.getSizeOfLong());

            for(int i=0;i<unicWidthList.size();i++){
                row.createCell(i+4);
                sheet.setColumnWidth(i+4,1000);
            }

            for(DescriptionDeskOak deskOak: packagedProduct.getDeskOakList()){
                row.getCell(getIndexOfWidthColumn(deskOak.getSizeOfWidth())+4)
                        .setCellValue(deskOak.getCountOfDesk());
            }


            row.createCell(lastIndex-2).setCellValue(packagedProduct.getSumWidthOfAllDesk());
            row.createCell(lastIndex-1).setCellValue(packagedProduct.getCountOfDesk());
            row.createCell(lastIndex).setCellValue(packagedProduct.getExtent());
        }


        Row rowFooter = sheet.createRow(countRow++);
        rowFooter.setHeight((short)400);
        createEmptyRow(rowFooter,0,3);

        rowFooter.createCell(lastIndex-2).setCellValue(mainSumWidthOfAllDesk);
        rowFooter.createCell(lastIndex-1).setCellValue(countOfAllDesk);
        rowFooter.createCell(lastIndex).setCellValue(mainExtent);


        for(int i=0;i<unicWidthList.size();i++){
            rowFooter.createCell(i+4).setCellValue(sumWidthList.get(unicWidthList.get(i)));
            sheet.setColumnWidth(i+4,1000);
        }

        for(int i=4;i<countRow;i++){
            Row row = sheet.getRow(i);
            for(int j=0;j<=lastIndex;j++){
                row.getCell(j).setCellStyle(style);
            }
        }


        sheet.setColumnWidth(0,1700);
        sheet.setColumnWidth(1,1300);
        sheet.setColumnWidth(3,1300);
        sheet.setColumnWidth(lastIndex,1500);
        sheet.setColumnWidth(lastIndex-1,1200);
        sheet.setColumnWidth(lastIndex-2,2000);

        //driver info
        for(int i=0;i<4;i++){
            for(int j=0;j<=lastIndex;j++){
                sheet.getRow(i).createCell(j).setCellStyle(style);
            }
            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, lastIndex));
        }

        sheet.getRow(0).getCell(0).setCellValue("№ Выгрузки: "+deliveryDocumentation.getDriverInfo().getIdOfTruck());
        sheet.getRow(1).getCell(0).setCellValue("А/М: "+deliveryDocumentation.getDriverInfo().getNumberOfTruck()+
                "  П/П: "+deliveryDocumentation.getDriverInfo().getNumberOfTrailer());
        sheet.getRow(2).getCell(0).setCellValue("Дата выгрузки: "+deliveryDocumentation.getDateOfUnloading()+
                " Время выгрузки: "+deliveryDocumentation.getTimeOfUnloading());
        sheet.getRow(3).getCell(0).setCellValue("Водитель: "+deliveryDocumentation.getDriverInfo().getFullName()+
                " Телефон: "+deliveryDocumentation.getDriverInfo().getPhone());



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

    private void getUnicWidth(){
        for (PackagedProduct product: deliveryDocumentation.getProductList()) {
            for(DescriptionDeskOak deskOak:product.getDeskOakList()){
                if (!unicWidthList.contains(deskOak.getSizeOfWidth())){
                    unicWidthList.add(deskOak.getSizeOfWidth());
                }
            }
            unicWidthList.sort((o1, o2) -> Integer.parseInt(o1)-Integer.parseInt(o2));
        }
    }

    private int getIndexOfWidthColumn(String width){
        for (int i=0; i<unicWidthList.size();i++){
            if (width.equals(unicWidthList.get(i))){
                return i;
            }
        }
        return 0;
    }


    private void getSumByWidth(){
        int sumW=0;
        int count=0;
        float extent=0;
        for (String width: unicWidthList){
            sumWidthList.put(width,0);
        }

        for (PackagedProduct product: deliveryDocumentation.getProductList()) {
            for(DescriptionDeskOak deskOak:product.getDeskOakList()){
                Integer sum = sumWidthList.get(deskOak.getSizeOfWidth());
                sumWidthList.put(deskOak.getSizeOfWidth(),sum+Integer.parseInt(deskOak.getCountOfDesk()));
            }
            sumW+=Integer.parseInt(product.getSumWidthOfAllDesk());
            count+=Integer.parseInt(product.getCountOfDesk());
            extent+=Float.parseFloat(product.getExtent());
        }

        mainSumWidthOfAllDesk = String.valueOf(sumW);
        countOfAllDesk = String.valueOf(count);
        mainExtent = String.format("%6.3f",extent);
    }

    private void createEmptyRow(Row row,int first,int last){
        for(int i=first;i<=last;i++){
            row.createCell(i);
        }
    }

}
