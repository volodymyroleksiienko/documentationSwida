package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.storages.RawStorage;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseRawStorageToXLS {
    private List<RawStorage> rawStorages;

    public String parse(String startDate, String endDate, int breedId) throws ParseException {
        Date after = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date before = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        List<RawStorage> filtered = new ArrayList<>();
        for(RawStorage p:rawStorages) {
            if(p.getDate()!=null) {
                Date dateOfInsert = new SimpleDateFormat("yyyy-MM-dd").parse(p.getDate());
                if (dateOfInsert.before(before) && dateOfInsert.after(after)) {
                    filtered.add(p);
                }
            }else {
                filtered.add(p);
            }
        }
        rawStorages = filtered;
        if(breedId==2){
            return parseOAK();
        }else {
            return parse();
        }
    }

    public String parse() throws ParseException {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("List of RawStorage");
        sheet.getPrintSetup().setLandscape(true);
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        for (int i = 0; i<12;i++){
            sheet.setColumnWidth(i,2300);
        }

        sheet.setColumnWidth(0,3500);
        sheet.setColumnWidth(10,3500);


        int rowCount = 0;
        Row rowHeader = sheet.createRow(rowCount++);
        rowHeader.setHeight((short)400);
        rowHeader.createCell(0).setCellValue("Код партии");
        rowHeader.createCell(1).setCellValue("Порода");
        rowHeader.createCell(2).setCellValue("Описание");
        rowHeader.createCell(3).setCellValue("Тол, мм");
        rowHeader.createCell(4).setCellValue("Шир, мм");
        rowHeader.createCell(5).setCellValue("Дл, мм");
        rowHeader.createCell(6).setCellValue("На складе, шт");
        rowHeader.createCell(7).setCellValue("Кубатура,м3");
        rowHeader.createCell(8).setCellValue("Было, шт");
        rowHeader.createCell(9).setCellValue("Кубатура,м3");
        rowHeader.createCell(10).setCellValue("Поставщик");
        rowHeader.createCell(11).setCellValue("Дата");
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

        for(RawStorage rs: rawStorages){
            if (rs.getDate()!=null) {
                Date dateOfInsert = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getDate());
                    Row row = sheet.createRow(rowCount++);
                    row.setHeight((short) 400);
                    row.createCell(0).setCellValue(rs.getCodeOfProduct());
                    row.createCell(1).setCellValue(rs.getBreedOfTree().getBreed());
                    row.createCell(2).setCellValue(rs.getBreedDescription());
                    row.createCell(3).setCellValue(Integer.parseInt(rs.getSizeOfHeight()));
                    row.createCell(4).setCellValue(Integer.parseInt(rs.getSizeOfWidth()));
                    row.createCell(5).setCellValue(Integer.parseInt(rs.getSizeOfLong()));
                    row.createCell(6).setCellValue(rs.getCountOfDesk());

                    BigDecimal extent = new BigDecimal(Float.parseFloat(rs.getExtent())).setScale(3,BigDecimal.ROUND_HALF_UP);
                    row.createCell(7).setCellValue(extent.doubleValue());

                    row.createCell(8).setCellValue(rs.getMaxCountOfDesk());
                    BigDecimal maxExtent = new BigDecimal(
                            Float.parseFloat(rs.getSizeOfHeight())*Float.parseFloat(rs.getSizeOfWidth())*Float.parseFloat(rs.getSizeOfLong())*rs.getMaxCountOfDesk()/1000000000)
                            .setScale(3,BigDecimal.ROUND_HALF_UP);
                    row.createCell(9).setCellValue(maxExtent.doubleValue());

                    if(rs.getTreeStorage()==null || rs.getTreeStorage().getContrAgent()==null){
                        row.createCell(10).setCellValue("");
                    }else {
                        row.createCell(10).setCellValue(rs.getTreeStorage().getContrAgent().getNameOfAgent());
                    }
                    row.createCell(11).setCellValue(rs.getDate());


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

    public String parseOAK() throws ParseException {
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

        for(RawStorage rs: rawStorages){
            if (rs.getDate()!=null) {
                Date dateOfInsert = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getDate());
                    Row row = sheet.createRow(rowCount++);
                    row.setHeight((short) 400);
                    row.createCell(0).setCellValue(rs.getCodeOfProduct());
                    row.createCell(1).setCellValue(rs.getBreedOfTree().getBreed());
                    row.createCell(2).setCellValue(rs.getBreedDescription());
                    row.createCell(3).setCellValue(Integer.parseInt(rs.getSizeOfHeight()));

                    BigDecimal extent = new BigDecimal(Float.parseFloat(rs.getExtent())).setScale(3,BigDecimal.ROUND_HALF_UP);
                    row.createCell(4).setCellValue(extent.doubleValue());
                    row.createCell(5).setCellValue(rs.getDescription());
                    row.getCell(0).setCellStyle(style);
                    row.getCell(1).setCellStyle(style);
                    row.getCell(2).setCellStyle(style);
                    row.getCell(3).setCellStyle(style);
                    row.getCell(4).setCellStyle(style);
                    row.getCell(5).setCellStyle(style);
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
