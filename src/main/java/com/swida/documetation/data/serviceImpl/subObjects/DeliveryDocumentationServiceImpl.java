package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.jpa.subObjects.DeliveryDocumentationJPA;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.storages.DescriptionDeskOakService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.data.service.subObjects.DriverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class DeliveryDocumentationServiceImpl implements DeliveryDocumentationService {
    private DeliveryDocumentationJPA documentationJPA;
    private PackagedProductService packagedProductService;
    private DriverInfoService driverInfoService;
    private OrderInfoService orderInfoService;
    private BreedOfTreeService breedOfTreeService;
    private DescriptionDeskOakService deskOakService;

    @Autowired
    public DeliveryDocumentationServiceImpl(DeliveryDocumentationJPA documentationJPA, PackagedProductService packagedProductService,
                                            DriverInfoService driverInfoService, OrderInfoService orderInfoService,
                                            BreedOfTreeService breedOfTreeService, DescriptionDeskOakService deskOakService) {
        this.documentationJPA = documentationJPA;
        this.packagedProductService = packagedProductService;
        this.driverInfoService = driverInfoService;
        this.orderInfoService = orderInfoService;
        this.breedOfTreeService = breedOfTreeService;
        this.deskOakService = deskOakService;
    }

    @Override
    public void save(DeliveryDocumentation doc) {
        documentationJPA.save(doc);
    }

    @Override
    public void checkInfoFromImport(List<DeliveryDocumentation> docList, OrderInfo orderInfo) {

        for (DeliveryDocumentation doc:docList) {
            DeliveryDocumentation docDB = documentationJPA.getDeliveryDocumentationByIdOfTruckByOrder(doc.getDriverInfo().getIdOfTruck(),orderInfo.getId());
            if (docDB==null){
                driverInfoService.save(doc.getDriverInfo());
                List<PackagedProduct> packList = new ArrayList<>();
                packList.addAll(doc.getProductList());
                doc.setProductList(new ArrayList<>());
                documentationJPA.save(doc);

                float mainExtent = 0;
                doc.setOrderInfo(orderInfo);

                for(PackagedProduct product: packList){
                    product.setOrderInfo(orderInfo);
                    product.setBreedOfTree(orderInfo.getBreedOfTree());
                    mainExtent+=Float.parseFloat(packagedProductService.countExtent(product));
                    product.setDeliveryDocumentation(doc);
                    doc.getProductList().add(product);
                    packagedProductService.save(product);

                }
                doc.setBreedOfTree(orderInfo.getBreedOfTree());
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
                                productDB.getCountDeskInWidth().equals(productXLS.getCountDeskInWidth()) &&
                                productDB.getCodeOfPackage().equals(productXLS.getCodeOfPackage())){

                            productDB.setCodeOfDeliveryCompany(productXLS.getCodeOfDeliveryCompany());
                            productDB.setHeight_width(productXLS.getHeight_width());
                            doc.getProductList().remove(productXLS);
                            packagedProductService.save(productDB);
                            break;
                        }
                    }

                }
//                adding pack
                if(doc.getProductList().size()>0){
                    List<PackagedProduct> packWithoutMultimodal = new ArrayList<>();
                    for(PackagedProduct product:docDB.getProductList()){
                        if(product.getCodeOfDeliveryCompany().isEmpty() || product.getCodeOfDeliveryCompany()==null){
                            packWithoutMultimodal.add(product);
                        }
                    }

                    for(int i=0; i< doc.getProductList().size();i++){
                        if(packWithoutMultimodal.size()>(i)){
                            doc.getProductList().get(i).setId(
                                    packWithoutMultimodal.get(i).getId()
                            );
                            packagedProductService.editPackageProduct(doc.getProductList().get(i));
                        }else {
                            PackagedProduct product = doc.getProductList().get(i);
                            product.setOrderInfo(orderInfo);
                            product.setBreedOfTree(orderInfo.getBreedOfTree());
                            product.setDeliveryDocumentation(docDB);
                            docDB.getProductList().add(product);
                            packagedProductService.save(product);
                        }
                    }
                    save(docDB);
                }
            }

        }
    }

    @Override
    public void checkHeightUnicValue(DeliveryDocumentation doc) {
        String list= doc.getProductList()
                .stream()
                .map(PackagedProduct::getSizeOfHeight)
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(TreeSet::new)).toString();

        list = list.replace("[","").replace("]","");
        doc.setSizeOfHeightList(list);
        documentationJPA.save(doc);
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
    public void checkInfoFromImportOak(List<DeliveryDocumentation> docList, OrderInfo orderInfo) {
        for(DeliveryDocumentation doc:docList){
            DeliveryDocumentation docDB = documentationJPA.getDeliveryDocumentationByIdOfTruckByOrder(doc.getDriverInfo().getIdOfTruck(),orderInfo.getId());
            if(docDB==null){
                driverInfoService.save(doc.getDriverInfo());
                List<PackagedProduct> packList = new ArrayList<>();
                packList.addAll(doc.getProductList());
                doc.setProductList(new ArrayList<>());
                documentationJPA.save(doc);
                float mainExtent = 0;
                doc.setOrderInfo(orderInfo);

                for(PackagedProduct product: packList){
                    List<DescriptionDeskOak> list = new ArrayList<>();
                            list.addAll(product.getDeskOakList());
                    for(DescriptionDeskOak deskOak:list){
                        if (deskOak.getSizeOfWidth().equals("-") || deskOak.getCountOfDesk().equals("-")){
                            product.getDeskOakList().remove(deskOak);
                        }else{
                            deskOakService.save(deskOak);
                        }
                    }
                    product.setDeliveryDocumentation(doc);
                    doc.getProductList().add(product);

                    product.setOrderInfo(orderInfo);
                    product.setBreedOfTree(orderInfo.getBreedOfTree());

                    mainExtent+=Float.parseFloat(product.getExtent().replace(",","."));
                    packagedProductService.save(product);
                }
                doc.setBreedOfTree(orderInfo.getBreedOfTree());
                doc.setContrAgent(orderInfo.getContrAgent());
                doc.setPackagesExtent(String.format("%.3f",mainExtent).replace(",","."));
                driverInfoService.save(doc.getDriverInfo());
                documentationJPA.save(doc);
            }

        }
    }


    @Override
    public DeliveryDocumentation findById(int id) {
        return documentationJPA.getOne(id);
    }

    @Override
    public List<DeliveryDocumentation> findAll() {
        return documentationJPA.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    @Override
    public List<DeliveryDocumentation> getListByUserByBreed(int breedId,int userId) {
        return documentationJPA.getListByUserByBreed(breedId,userId)
                .stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryDocumentation getDeliveryDocumentationByIdOfTruck(String idOfTruck) {
        return documentationJPA.getDeliveryDocumentationByIdOfTruck(idOfTruck);
    }

    @Override
    public DeliveryDocumentation getDeliveryDocumentationByIdOfTruckByOrder(String idOfTruck, int orderId) {
        return documentationJPA.getDeliveryDocumentationByIdOfTruckByOrder(idOfTruck,orderId);
    }

    @Override
    public List<DeliveryDocumentation> getListByDistributionContractsId(List<Integer> contractId) {
        return documentationJPA.getListByDistributionContractsId(contractId)
                .stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryDocumentation> getListByDestinationType(DeliveryDestinationType type) {
        return documentationJPA.getListByDestinationType(type)
                .stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
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
    public DeliveryDocumentation editDeliveryDoc(DeliveryDocumentation documentation) {
        DeliveryDocumentation docDB = documentationJPA.getOne(documentation.getId());
        documentation.getDriverInfo().setId(docDB.getDriverInfo().getId());
        driverInfoService.save(documentation.getDriverInfo());
        docDB.setDriverInfo(documentation.getDriverInfo());

        int oldOrderInfoId =docDB.getOrderInfo().getId();
        OrderInfo orderInfo = orderInfoService.findById(documentation.getOrderInfo().getId());
        if (oldOrderInfoId!=orderInfo.getId()){
            docDB.setOrderInfo(orderInfo);
            documentationJPA.save(docDB);
            reloadOrdersExtent(orderInfoService.findById(oldOrderInfoId));
        }
        docDB.setDateOfUnloading(documentation.getDateOfUnloading());
        docDB.setTimeOfUnloading(documentation.getTimeOfUnloading());
//        docDB.setDestinationType(documentation.getDestinationType());
        docDB.setPackagesExtent(documentation.getPackagesExtent());
        docDB.setDescription(documentation.getDescription());

        documentationJPA.save(docDB);



        return docDB;
    }
    public void reloadOrdersExtent(OrderInfo orderInfo){
        List<Integer> list = new ArrayList<>();
        list.add(orderInfo.getId());
        List<DeliveryDocumentation> docList = documentationJPA.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(orderInfo,docList);
        orderInfoService.reloadMainOrderExtent(orderInfo.getMainOrder());
    }


    @Override
    public PackagedProduct addPackageProductToDeliveryDoc(String docId, PackagedProduct product) {
        DeliveryDocumentation documentation = documentationJPA.getOne(Integer.parseInt(docId));
        product.setBreedOfTree(breedOfTreeService.getObjectByName(product.getBreedOfTree().getBreed()));
        product.setExtent(String.format("%.3f",Float.parseFloat(product.getExtent())).replace(",","."));
        product.setStatusOfProduct(StatusOfProduct.IN_DELIVERY);
        product.setDeliveryDocumentation(documentation);
        packagedProductService.saveWithoutCalculating(product);
        documentation.getProductList().add(product);
        documentationJPA.save(documentation);
        return product;
    }

    @Override
    public void reloadExtentOfAllPack(DeliveryDocumentation documentation) {
        float fullExtent = 0;
        for(PackagedProduct product:documentation.getProductList()){
            fullExtent+=Float.parseFloat(product.getExtent());
        }
        documentation.setPackagesExtent(String.format("%.3f",fullExtent).replace(",","."));
        documentationJPA.save(documentation);
    }

    @Override
    public void deletePackage(String id, String deliveryId) {
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(id));
        DeliveryDocumentation deliveryDocumentation = documentationJPA.getOne(Integer.parseInt(deliveryId));
        if (product.getDryStorage()==null){
            deliveryDocumentation.getProductList().remove(product);
            packagedProductService.deleteByID(product.getId());
            documentationJPA.save(deliveryDocumentation);
        }else{
            deliveryDocumentation.getProductList().remove(product);
            documentationJPA.save(deliveryDocumentation);
            product.setStatusOfProduct(StatusOfProduct.ON_STORAGE);
            product.setDeliveryDocumentation(null);
            packagedProductService.saveWithoutCalculating(product);
        }
    }

    @Override
    public void deleteDeliveryDoc(int deliveryId){
        DeliveryDocumentation documentation = documentationJPA.getOne(deliveryId);
        for(PackagedProduct product:documentation.getProductList()){
            if (product.getBreedOfTree().getId()==2){
                for(DescriptionDeskOak desc:product.getDeskOakList()){
                    deskOakService.deleteByID(desc.getId());
                }
            }
            packagedProductService.deleteByID(product.getId());
        }
        documentationJPA.deleteById(documentation.getId());
    }

    @Override
    public void deleteByID(int id) {
        documentationJPA.deleteById(id);
    }
}
