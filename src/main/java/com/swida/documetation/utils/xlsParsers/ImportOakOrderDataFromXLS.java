package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.StatusOfProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImportOakOrderDataFromXLS {
    private MultipartFile multipartFile;

    public List<DeliveryDocumentation> importData() throws IOException, InvalidFormatException {
        String currentDate = new SimpleDateFormat("yyMMddHHmmssZ").format(new Date());
        File mainFile = new File(System.getProperty("user.home") +File.separator+currentDate+multipartFile.getOriginalFilename());
        multipartFile.transferTo(mainFile);
        Workbook workbook = WorkbookFactory.create(mainFile);

        List<DeliveryDocumentation> docList = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        sheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                if (cellValue.contains("Quality")){
//                    docList.add(getDeliveryDoc(cell.getRowIndex(),cell.getColumnIndex(),sheet));
                    docList.add(getDeliveryDoc(cell.getRowIndex(),cell.getColumnIndex(),sheet));
//                    getPackage(cell.getRowIndex(),cell.getColumnIndex(),sheet);
                }
            });
        });

        return docList;
    }

    DeliveryDocumentation getDeliveryDoc(int startRow, int startCell, Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();
        DeliveryDocumentation documentation = new DeliveryDocumentation();

        String deliveryDateInfo = dataFormatter.formatCellValue(sheet.getRow(startRow-2).getCell(0));
        String date = deliveryDateInfo.substring(0,deliveryDateInfo.lastIndexOf("Время выгрузки"))
                                        .replace("Дата выгрузки","")
                                        .replace(":","")
                                        .trim();
        String time = deliveryDateInfo.substring(deliveryDateInfo.lastIndexOf("Время выгрузки"))
                                        .replace("Время выгрузки:","");


        documentation.setDriverInfo(getDriver(startRow,startCell,sheet));
        documentation.setProductList(getPackage(startRow,startCell,sheet));
        documentation.setDateOfUnloading(date);
        documentation.setTimeOfUnloading(time);

        return documentation;
    }


    private DriverInfo getDriver(int startRow, int startCell, Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();

        String driverInfo = dataFormatter.formatCellValue(sheet.getRow(startRow-1).getCell(0));
        String driverName = driverInfo.substring(0,driverInfo.lastIndexOf("Телефон"))
                .replace("Водитель","")
                .replace(":","");
        String driverPhone = driverInfo.substring(driverInfo.lastIndexOf("Телефон"))
                .replace("Телефон","")
                .replace(":","");

        String driverNum = dataFormatter.formatCellValue(sheet.getRow(startRow-3).getCell(0));
        String truckNum = driverNum.substring(0,driverNum.lastIndexOf("П/П"))
                .replace("А/М","").replace(" ","")
                .replace(":","");
        String trailerNum = driverNum.substring(driverNum.lastIndexOf("П/П"))
                .replace("П/П","").replace(" ","")
                .replace(":","");

        String idOfTruck = dataFormatter.formatCellValue(sheet.getRow(startRow-4).getCell(0))
                .replace("№","").replace(" ","")
                .replace("Выгрузки","")
                .replace(":","");

        DriverInfo driver = new DriverInfo();
        driver.setIdOfTruck(idOfTruck);
        driver.setNumberOfTruck(truckNum);
        driver.setNumberOfTrailer(trailerNum);
        driver.setPhone(driverPhone);
        driver.setFullName(driverName);
        return driver;
    }

    List<PackagedProduct> getPackage(int startRow, int startCell, Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();
        List<PackagedProduct> productList = new ArrayList<>();
        int startRowPack = startRow+2;


        System.out.println(startRow + "     "+startCell);

        for(;startRowPack<sheet.getLastRowNum();startRowPack++){
            Row row = sheet.getRow(startRowPack);
            int lastIndexCol = row.getLastCellNum();
            PackagedProduct product = new PackagedProduct();

            product.setQuality(dataFormatter.formatCellValue(row.getCell(startCell)));
            product.setSizeOfHeight(dataFormatter.formatCellValue(row.getCell(startCell+1)));
            product.setCodeOfPackage(dataFormatter.formatCellValue(row.getCell(startCell+2)));
            product.setSizeOfLong(dataFormatter.formatCellValue(row.getCell(startCell+3)));

            product.setExtent(dataFormatter.formatCellValue(row.getCell(lastIndexCol-1)));
            product.setCountOfDesk(dataFormatter.formatCellValue(row.getCell(lastIndexCol-2)));
            product.setSumWidthOfAllDesk(dataFormatter.formatCellValue(row.getCell(lastIndexCol-3)));
            product.setStatusOfProduct(StatusOfProduct.IN_DELIVERY);

            product.setDeskOakList(getDeskDescription(startRow+1,startCell+4,startRowPack,sheet));

            productList.add(product);
        }

        System.out.println();
        System.out.println(productList);
        System.out.println();
        return productList;
    }

    List<DescriptionDeskOak> getDeskDescription(int startRow, int startCell, int packRow,Sheet sheet){
        int lastIndexDesc = sheet.getRow(startRow).getLastCellNum()-3;
        List<DescriptionDeskOak> descList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();


        for (int i=startCell;i<lastIndexDesc;i++){
            if (dataFormatter.formatCellValue(sheet.getRow(packRow).getCell(i)).isEmpty()){
                continue;
            }else{
                DescriptionDeskOak deskOak = new DescriptionDeskOak();
                deskOak.setSizeOfWidth(dataFormatter.formatCellValue(sheet.getRow(startRow).getCell(i)));
                deskOak.setCountOfDesk(dataFormatter.formatCellValue(sheet.getRow(packRow).getCell(i)));
                descList.add(deskOak);
            }
        }

        return descList;
    }

}
