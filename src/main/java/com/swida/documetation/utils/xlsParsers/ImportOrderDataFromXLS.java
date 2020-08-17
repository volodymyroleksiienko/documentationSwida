package com.swida.documetation.utils.xlsParsers;

import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.File;
import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImportOrderDataFromXLS {
    private MultipartFile multipartFile;

    public List<DeliveryDocumentation> importData() throws IOException, InvalidFormatException {
        File mainFile = new File(System.getProperty("user.home") +File.separator+multipartFile.getOriginalFilename());
        multipartFile.transferTo(mainFile);
        Workbook workbook = WorkbookFactory.create(mainFile);

        List<DeliveryDocumentation> docList = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        sheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                if (cellValue.contains("машина")){
                    docList.add(getDeliveryDoc(cell.getRowIndex(),cell.getColumnIndex(),sheet));
                }
            });
        });

        return docList;
    }

    public List<PackagedProduct> getListOfPackages(int startRow, int startCell, Sheet sheet){
        List<PackagedProduct> list = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        for(int i=startRow+1;;i++){
            Row row = sheet.getRow(i);
            if (dataFormatter.formatCellValue(row.getCell(startCell+9)).isEmpty()){
                break;
            }
            PackagedProduct product = new PackagedProduct();
            product.setCodeOfPackage(dataFormatter.formatCellValue(row.getCell(startCell+1)));
            product.setSizeOfHeight(parseDataToMM(dataFormatter.formatCellValue(row.getCell(startCell+2))));
            product.setSizeOfWidth(parseDataToMM(dataFormatter.formatCellValue(row.getCell(startCell+3))));
            product.setSizeOfLong(parseDataToMM(dataFormatter.formatCellValue(row.getCell(startCell+4))));
            product.setCountOfDesk(dataFormatter.formatCellValue(row.getCell(startCell+5)));
            product.setExtent(dataFormatter.formatCellValue(row.getCell(startCell+6)));

            product.setCountDeskInHeight(dataFormatter.formatCellValue(row.getCell(startCell+7)));
            product.setCountDeskInWidth(dataFormatter.formatCellValue(row.getCell(startCell+8)));
            product.setCodeOfDeliveryCompany(dataFormatter.formatCellValue(row.getCell(startCell+9)));
//            product.setCodeOfPackage(dataFormatter.formatCellValue(row.getCell(startCell+1)));
            product.setHeight_width(dataFormatter.formatCellValue(row.getCell(startCell+11)));

            product.setStatusOfProduct(StatusOfProduct.IN_DELIVERY);
            System.out.println(product);
            list.add(product);
        }
        return list;
    }

    private String parseDataToMM(String str){
        float size = Float.parseFloat(str.replace(",","."))*1000;
        return String.format("%.0f",size);
    }

    private DeliveryDocumentation getDeliveryDoc(int startRow, int startCell, Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();
        DeliveryDocumentation deliveryDocumentation = new DeliveryDocumentation();
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setIdOfTruck(dataFormatter.formatCellValue(sheet.getRow(startRow).getCell(startCell))
                .replace("машина","").replace(" ",""));
        driverInfo.setNumberOfTruck(dataFormatter.formatCellValue(sheet.getRow(startRow+2).getCell(startCell)));
        driverInfo.setNumberOfTrailer(dataFormatter.formatCellValue(sheet.getRow(startRow+4).getCell(startCell)));

        String date = dataFormatter.formatCellValue(sheet.getRow(startRow+6).getCell(startCell));
        String[] dateArr= date.split("\".\"");
        if (dateArr.length==3){
            date = dateArr[2]+"-"+dateArr[1]+"-"+dateArr[0];
        }

        deliveryDocumentation.setDateOfUnloading(date);

        String timeInfo = dataFormatter.formatCellValue(sheet.getRow(startRow+8).getCell(startCell))+" "
                +dataFormatter.formatCellValue(sheet.getRow(startRow+9).getCell(startCell));
        deliveryDocumentation.setTimeOfUnloading(timeInfo);


        driverInfo.setPhone(dataFormatter.formatCellValue(sheet.getRow(startRow+10).getCell(startCell))
                .replace("Водитель","").replace(":",""));
        driverInfo.setFullName(dataFormatter.formatCellValue(sheet.getRow(startRow+11).getCell(startCell)));

        deliveryDocumentation.setProductList(getListOfPackages(startRow,startCell,sheet));
        deliveryDocumentation.setDestinationType(DeliveryDestinationType.MULTIMODAL);
        deliveryDocumentation.setDriverInfo(driverInfo);
        return deliveryDocumentation;
    }
}
