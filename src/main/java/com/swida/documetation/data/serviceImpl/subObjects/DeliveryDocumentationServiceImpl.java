package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.jpa.subObjects.DeliveryDocumentationJPA;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.data.service.subObjects.DriverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class DeliveryDocumentationServiceImpl implements DeliveryDocumentationService {
    private DeliveryDocumentationJPA documentationJPA;
    private PackagedProductService packagedProductService;
    private DriverInfoService driverInfoService;
    private OrderInfoService orderInfoService;
    private BreedOfTreeService breedOfTreeService;

    @Autowired
    public DeliveryDocumentationServiceImpl(DeliveryDocumentationJPA documentationJPA, PackagedProductService packagedProductService,
                                            DriverInfoService driverInfoService, OrderInfoService orderInfoService,
                                            BreedOfTreeService breedOfTreeService) {
        this.documentationJPA = documentationJPA;
        this.packagedProductService = packagedProductService;
        this.driverInfoService = driverInfoService;
        this.orderInfoService = orderInfoService;
        this.breedOfTreeService = breedOfTreeService;
    }

    @Override
    public void save(DeliveryDocumentation doc) {
        documentationJPA.save(doc);
    }

    @Override
    public void checkInfoFromImport(List<DeliveryDocumentation> docList, OrderInfo orderInfo) {

        for (DeliveryDocumentation doc:docList) {
            DeliveryDocumentation docDB = documentationJPA.getDeliveryDocumentationByIdOfTruck(doc.getDriverInfo().getIdOfTruck());
            if (docDB==null){
                float mainExtent = 0;
                doc.setOrderInfo(orderInfo);

                for(PackagedProduct product: doc.getProductList()){
                    product.setOrderInfo(orderInfo);
                    product.setBreedOfTree(orderInfo.getBreedOfTree());
                    mainExtent+=Float.parseFloat(packagedProductService.countExtent(product));
                    packagedProductService.save(product);
                }
                doc.setContrAgent(orderInfo.getContrAgent());
                doc.setPackagesExtent(String.format("%.3f",mainExtent).replace(",","."));
                driverInfoService.save(doc.getDriverInfo());
                documentationJPA.save(doc);
            } else {
                int startIndex = 0;
                for(int i=0; i<docDB.getProductList().size();i++){

                    PackagedProduct productDB = docDB.getProductList().get(i);
                    for(PackagedProduct productXLS: doc.getProductList()){
                        if(docDB.getDriverInfo().getIdOfTruck().equals(doc.getDriverInfo().getIdOfTruck())){
                            docDB.setDateOfUnloading(doc.getDateOfUnloading());
                        }
                        if (productDB.getSizeOfHeight().equals(productXLS.getSizeOfHeight()) &&
                                productDB.getSizeOfWidth().equals(productXLS.getSizeOfWidth()) &&
                                productDB.getSizeOfLong().equals(productXLS.getSizeOfLong()) &&
                                productDB.getCountOfDesk().equals(productXLS.getCountOfDesk()) &&
                                productDB.getCountDeskInHeight().equals(productXLS.getCountDeskInHeight()) &&
                                productDB.getCountDeskInWidth().equals(productXLS.getCountDeskInWidth())){

                            productDB.setCodeOfDeliveryCompany(productXLS.getCodeOfDeliveryCompany());
                            productDB.setHeight_width(productXLS.getHeight_width());
                            doc.getProductList().remove(productXLS);
                            packagedProductService.save(productDB);
                            break;
                        }
                    }

                }
            }

        }
    }

    @Override
    public void deletePackageFromDeliveryDoc(PackagedProduct product) {
        DeliveryDocumentation deliveryDocumentation = documentationJPA.getDeliveryDocumentationPackagedProduct(product);
        deliveryDocumentation.getProductList().remove(product);
        product.setStatusOfProduct(StatusOfProduct.ON_STORAGE);
        packagedProductService.saveWithoutCalculating(product);
        documentationJPA.save(deliveryDocumentation);
    }


    @Override
    public DeliveryDocumentation findById(int id) {
        return documentationJPA.getOne(id);
    }

    @Override
    public List<DeliveryDocumentation> findAll() {
        return documentationJPA.findAll();
    }

    @Override
    public List<DeliveryDocumentation> getListByUserByBreed(int breedId,int userId) {
        return documentationJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public DeliveryDocumentation getDeliveryDocumentationByIdOfTruck(String idOfTruck) {
        return documentationJPA.getDeliveryDocumentationByIdOfTruck(idOfTruck);
    }

    @Override
    public List<DeliveryDocumentation> getListByDistributionContractsId(List<Integer> contractId) {
        return documentationJPA.getListByDistributionContractsId(contractId);
    }

    @Override
    public List<DeliveryDocumentation> getListByDestinationType(DeliveryDestinationType type) {
        return documentationJPA.getListByDestinationType(type);
    }

    @Override
    public List<String> getAllTruckIdList(List<DeliveryDocumentation> docList) {
        List<String> truckIdList = new ArrayList<>();
        for (DeliveryDocumentation doc:docList) {
            truckIdList.add(doc.getDriverInfo().getIdOfTruck());
        }
        return truckIdList;
    }

    @Override
    public void editDeliveryDoc(DeliveryDocumentation documentation) {
        DeliveryDocumentation docDB = documentationJPA.getOne(documentation.getId());
        documentation.getDriverInfo().setId(docDB.getDriverInfo().getId());
        driverInfoService.save(documentation.getDriverInfo());
        docDB.setDriverInfo(documentation.getDriverInfo());

        OrderInfo orderInfo = orderInfoService.findByCodeOfOrder(documentation.getOrderInfo().getCodeOfOrder());
        docDB.setOrderInfo(orderInfo);
        docDB.setDateOfUnloading(documentation.getDateOfUnloading());
        docDB.setTimeOfUnloading(documentation.getTimeOfUnloading());
        docDB.setDestinationType(documentation.getDestinationType());
        docDB.setDescription(documentation.getDescription());


        documentationJPA.save(docDB);
    }

    @Override
    public void addPackageProductToDeliveryDoc(String docId, PackagedProduct product) {
        DeliveryDocumentation documentation = documentationJPA.getOne(Integer.parseInt(docId));
        product.setBreedOfTree(breedOfTreeService.getObjectByName(product.getBreedOfTree().getBreed()));
        product.setExtent(String.format("%.3f",Float.parseFloat(product.getExtent())).replace(",","."));
        product.setStatusOfProduct(StatusOfProduct.IN_DELIVERY);
        packagedProductService.saveWithoutCalculating(product);
        documentation.getProductList().add(product);
        documentationJPA.save(documentation);
    }

    @Override
    public void reloadExtentOfAllPack(DeliveryDocumentation documentation) {
        float fullExtent = 0;
        for(PackagedProduct product:documentation.getProductList()){
            fullExtent=+Float.parseFloat(product.getExtent());
        }
        documentation.setPackagesExtent(String.format("%.3f",fullExtent).replace(",","."));
    }

    @Override
    public void deletePackage(String id, String deliveryId) {
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(id));
        if (product.getUserCompany()==null){
            packagedProductService.deleteByID(product.getId());
        }else{
            DeliveryDocumentation deliveryDocumentation = documentationJPA.getOne(Integer.parseInt(deliveryId));
            deliveryDocumentation.getProductList().remove(product);
            product.setStatusOfProduct(StatusOfProduct.ON_STORAGE);
            packagedProductService.saveWithoutCalculating(product);
            documentationJPA.save(deliveryDocumentation);
        }
    }
    @Override
    public void deleteByID(int id) {
        documentationJPA.deleteById(id);
    }
}
