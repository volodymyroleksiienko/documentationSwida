package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParserOakPackagerProductXLS {
    private List<PackagedProduct> productList;
    private List<String> unicWidthList;
    private Map<String,Integer> sumWidthList;
    private String mainSumWidthOfAllDesk;
    private String countOfAllDesk;
    private String mainExtent;

    public ParserOakPackagerProductXLS(List<PackagedProduct> productList) {
        this.productList = productList;
        unicWidthList = new ArrayList<>();
        sumWidthList = new HashMap<>();
        //initialize unic list
        getUnicWidth();
        //initialize map
        getSumByWidth();
    }
    public String parse(String startDate, String endDate ) throws ParseException {
        Date after = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date before = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        List<PackagedProduct> filtered = new ArrayList<>();
        for(PackagedProduct p:productList) {
            if(p.getDate()!=null) {
                Date dateOfInsert = new SimpleDateFormat("yyyy-MM-dd").parse(p.getDate());
                if (dateOfInsert.before(before) && dateOfInsert.after(after)) {
                    filtered.add(p);
                }
            }else {
                filtered.add(p);
            }
        }
        System.out.println(filtered);
        System.out.println(filtered.size());
        productList = filtered;
        return parse();
    }

    public String parse() {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of PackagedProduct");
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


        Row rowHeader1 = sheet.createRow(countRow++);
        rowHeader1.setHeight((short)600);
        rowHeader1.createCell(0).setCellValue("Quality");
        rowHeader1.createCell(1).setCellValue("SIZE");
        rowHeader1.createCell(2).setCellValue("PKG");
        rowHeader1.createCell(3).setCellValue("Lenght");
        rowHeader1.getCell(0).setCellStyle(style);
        rowHeader1.getCell(1).setCellStyle(style);
        rowHeader1.getCell(2).setCellStyle(style);
        rowHeader1.getCell(3).setCellStyle(style);

        Row rowHeader2 = sheet.createRow(countRow++);
        rowHeader2.setHeight((short)300);
        rowHeader2.createCell(0);
        rowHeader2.createCell(1);
        rowHeader2.createCell(2);
        rowHeader2.createCell(3);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));


//        create width block

        int widthColCount = unicWidthList.size();
        int lastIndex = widthColCount+6;
        System.out.println("Col    " + widthColCount);
        rowHeader1.createCell(4).setCellValue("Width, mm");
        createEmptyRow(rowHeader1,5,lastIndex);
        rowHeader1.getCell(4).setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, lastIndex));

        for(int i=0;i<unicWidthList.size();i++){
            rowHeader2.createCell(i+4).setCellValue(Integer.parseInt(unicWidthList.get(i)));
        }
        rowHeader2.createCell(lastIndex).setCellValue("m3");
        rowHeader2.createCell(lastIndex-1).setCellValue("К-во");
        rowHeader2.createCell(lastIndex-2).setCellValue("Сум шир");




        for (PackagedProduct packagedProduct: productList) {
            Row row = sheet.createRow(countRow++);
            row.setHeight((short)400);
            row.createCell(0).setCellValue(packagedProduct.getQuality());
            row.createCell(1).setCellValue(Integer.parseInt(packagedProduct.getSizeOfHeight()));
            row.createCell(2).setCellValue(packagedProduct.getCodeOfPackage());
            row.createCell(3).setCellValue(Integer.parseInt(packagedProduct.getSizeOfLong()));

            for(int i=0;i<unicWidthList.size();i++){
                row.createCell(i+4);
                sheet.setColumnWidth(i+4,1000);
            }

            for(DescriptionDeskOak deskOak: packagedProduct.getDeskOakList()){
                row.getCell(getIndexOfWidthColumn(deskOak.getSizeOfWidth())+4)
                        .setCellValue(Integer.parseInt(deskOak.getCountOfDesk()));
            }


            row.createCell(lastIndex-2).setCellValue(Integer.parseInt(packagedProduct.getSumWidthOfAllDesk()));
            row.createCell(lastIndex-1).setCellValue(Integer.parseInt(packagedProduct.getCountOfDesk()));

            BigDecimal extent = new BigDecimal(Float.parseFloat(packagedProduct.getExtent())).setScale(3,BigDecimal.ROUND_HALF_UP);
            row.createCell(lastIndex).setCellValue(extent.doubleValue());
        }


        Row rowFooter = sheet.createRow(countRow++);
        rowFooter.setHeight((short)400);
        createEmptyRow(rowFooter,0,3);

        rowFooter.createCell(lastIndex-2).setCellValue(Integer.parseInt(mainSumWidthOfAllDesk));
        rowFooter.createCell(lastIndex-1).setCellValue(Integer.parseInt(countOfAllDesk));

        rowFooter.createCell(lastIndex).setCellValue(
                new BigDecimal(Float.parseFloat(mainExtent)).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue()
        );


        for(int i=0;i<unicWidthList.size();i++){
            rowFooter.createCell(i+4).setCellValue(sumWidthList.get(unicWidthList.get(i)));
            sheet.setColumnWidth(i+4,1000);
        }

        for(int i=0;i<countRow;i++){
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



        String filePath="";
        try {
            filePath = System.getProperty("user.home") + File.separator + "delivery.xlsx";
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            book.write(fos);
            fos.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private void getUnicWidth(){
        for (PackagedProduct product: productList) {
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

        for (PackagedProduct product: productList) {
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
        mainExtent = String.format("%.3f",extent).replace(",",".");
    }

    private void createEmptyRow(Row row,int first,int last){
        for(int i=first;i<=last;i++){
            row.createCell(i);
        }
    }
}
