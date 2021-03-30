package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.jpa.storages.PackagedProductJPA;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.DescriptionDeskOakService;
import com.swida.documetation.data.service.storages.DryStorageService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContainerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PackagedProductServiceImpl implements PackagedProductService {
    private DryStorageService dryStorageService;
    private RawStorageService rawStorageService;
    private PackagedProductJPA productJPA;
    private DescriptionDeskOakService deskOakService;
    private BreedOfTreeService breedOfTreeService;
    private UserCompanyService userCompanyService;
    private ContainerService containerService;
    private OrderInfoService orderInfoService;

    @Autowired
    public PackagedProductServiceImpl(DryStorageService dryStorageService, PackagedProductJPA productJPA,
                                      DescriptionDeskOakService deskOakService, RawStorageService rawStorageService,
                                      BreedOfTreeService breedOfTreeService,UserCompanyService userCompanyService,
                                      ContainerService containerService,OrderInfoService orderInfoService) {
        this.dryStorageService = dryStorageService;
        this.productJPA = productJPA;
        this.deskOakService = deskOakService;
        this.rawStorageService = rawStorageService;
        this.breedOfTreeService = breedOfTreeService;
        this.userCompanyService = userCompanyService;
        this.containerService = containerService;
        this.orderInfoService = orderInfoService;
    }

    @Override
    public void save(PackagedProduct packProd) {
        packProd.setExtent(String.format("%.3f", Float.parseFloat(packProd.getExtent().replace(',', '.'))).replace(',', '.'));
        Date date = new Date(System.currentTimeMillis());
        packProd.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        productJPA.save(packProd);
    }

    @Override
    public void saveWithoutCalculating(PackagedProduct packProd) {
        productJPA.save(packProd);
    }


    @Override
    public void createPackages(String dryStorageId, String codeOfProduct,String breedDescription,String countOfDesk, String countHeight, String countWidth,
                               String countOfPack, String longFact,String heightWidth, UserCompany userCompany) {
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(dryStorageId));
        for (int i=1; i<=Integer.parseInt(countOfPack);i++){
            PackagedProduct product = new PackagedProduct();
            product.setCodeOfPackage(codeOfProduct+"-"+i);
            product.setBreedOfTree(dryStorage.getBreedOfTree());
            product.setBreedDescription(breedDescription);
            product.setUserCompany(userCompany);
            product.setDryStorage(dryStorage);

            product.setSizeOfHeight(dryStorage.getSizeOfHeight());
            product.setSizeOfWidth(dryStorage.getSizeOfWidth());
            product.setSizeOfLong(dryStorage.getSizeOfLong());


            product.setCountOfDesk(countOfDesk);
            product.setCountDeskInHeight(countHeight);
            product.setCountDeskInWidth(countWidth);

            product.setLongFact(longFact);
            product.setExtent(countOfExtent(product));


            product.setHeight_width(heightWidth);


            dryStorage.setCountOfDesk(dryStorage.getCountOfDesk()-Integer.parseInt(product.getCountOfDesk()));
            Date date = new Date(System.currentTimeMillis());
            product.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
            if(product.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
                product.setBreedDescription("");
            }
            productJPA.save(product);
        }
        dryStorageService.save(dryStorage);
    }

    @Override
    public void createPackagesWithoutHistory(PackagedProduct product, String countOfPacks,int breedId, int userId) {
        int countPack = Integer.parseInt(countOfPacks);
        String codeOfPack = product.getCodeOfPackage();

        for(int i=0;i<countPack;i++){
            PackagedProduct newProduct = new PackagedProduct();
            if (countPack>1){
                newProduct.setCodeOfPackage(codeOfPack+"-"+(i+1));
            }else {
                newProduct.setCodeOfPackage(codeOfPack);
            }
            newProduct.setBreedOfTree(breedOfTreeService.findById(breedId));
            newProduct.setUserCompany(userCompanyService.findById(userId));

            newProduct.setBreedDescription(product.getBreedDescription());
            newProduct.setSizeOfHeight(product.getSizeOfHeight());
            newProduct.setSizeOfWidth(product.getSizeOfWidth());
            newProduct.setSizeOfLong(product.getSizeOfLong());
            newProduct.setCountOfDesk(product.getCountOfDesk());
            newProduct.setCountDeskInHeight(product.getCountDeskInHeight());
            newProduct.setCountDeskInWidth(product.getCountDeskInWidth());

            newProduct.setLongFact(product.getLongFact());
            newProduct.setHeight_width(product.getHeight_width());

            newProduct.setExtent(countExtent(newProduct));
            productJPA.save(newProduct);
        }
    }


    @Override
    public PackagedProduct createPackageOak(String[][] arrayOfDesk, String idOfDryStorage, String codeOfPackage, String quality, String sizeOfHeight, String length,int userID,int breedID) {
        DryStorage dryStorage = (idOfDryStorage.isEmpty())?null:dryStorageService.findById(Integer.parseInt(idOfDryStorage));

        UserCompany company = new UserCompany();
        company.setId(userID);
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(breedID);

        PackagedProduct product = new PackagedProduct();
        if(company.getId()!=0) {
            product.setUserCompany(company);
        }
        product.setBreedOfTree(breedOfTree);
        product.setDryStorage(dryStorage);
        product.setCodeOfPackage(codeOfPackage);
        product.setQuality(quality);
        product.setSizeOfHeight(sizeOfHeight);
        product.setSizeOfLong(length);

        List<DescriptionDeskOak> deskOakList = new ArrayList<>();
        float cofExtent = Float.parseFloat(sizeOfHeight)*Float.parseFloat(length)/1000000;
        int sumWidth = 0;
        int countOfAllDesk = 0;
        float extent = 0;

        //i = 1 skip test obj
        for (int i=1; i<arrayOfDesk.length;i++){
            deskOakList.add(new DescriptionDeskOak(arrayOfDesk[i][0],arrayOfDesk[i][1]));
            sumWidth += (Integer.parseInt(arrayOfDesk[i][0])*Integer.parseInt(arrayOfDesk[i][1]));
            countOfAllDesk += Integer.parseInt(arrayOfDesk[i][1]);
            extent += (cofExtent*Float.parseFloat(arrayOfDesk[i][0])*Float.parseFloat(arrayOfDesk[i][1])/1000);
        }

        product.setDeskOakList(deskOakList);
        product.setSumWidthOfAllDesk(String.valueOf(sumWidth));
        product.setCountOfDesk(String.valueOf(countOfAllDesk));
        product.setExtent(String.format("%.3f",extent).replace(',','.'));
        if(dryStorage!=null) {
            dryStorage.setExtent(String.format("%.3f", Float.parseFloat(dryStorage.getExtent()) - extent).replace(',', '.'));
            dryStorageService.save(dryStorage);
        }
        deskOakService.saveAll(deskOakList);
        Date date = new Date(System.currentTimeMillis());
        product.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        if(product.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            product.setBreedDescription("");
        }
        productJPA.save(product);

        return product;
    }

    @Override
    public PackagedProduct createPackageOak(List<DescriptionDeskOak> deskOakList, String idOfDryStorage, String codeOfPackage, String quality, String sizeOfHeight, String length,int userID,int breedID) {
        DryStorage dryStorage = (idOfDryStorage.isEmpty())?null:dryStorageService.findById(Integer.parseInt(idOfDryStorage));

        UserCompany company = new UserCompany();
        company.setId(userID);
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(breedID);

        PackagedProduct product = new PackagedProduct();
        if(company.getId()!=0) {
            product.setUserCompany(company);
        }
        product.setBreedOfTree(breedOfTree);
        product.setDryStorage(dryStorage);
        product.setCodeOfPackage(codeOfPackage);
        product.setQuality(quality);
        product.setSizeOfHeight(sizeOfHeight);
        product.setSizeOfLong(length);

        productJPA.save(product);

        float cofExtent = Float.parseFloat(sizeOfHeight)*Float.parseFloat(length)/1000000;
        int sumWidth = 0;
        int countOfAllDesk = 0;
        float extent = 0;

        for (DescriptionDeskOak desk:deskOakList){
            sumWidth += (Integer.parseInt(desk.getSizeOfWidth())*Integer.parseInt(desk.getCountOfDesk()));
            countOfAllDesk += Integer.parseInt(desk.getCountOfDesk());
            extent += (cofExtent*Float.parseFloat(desk.getSizeOfWidth())*Float.parseFloat(desk.getCountOfDesk())/1000);
            desk.setPackagedProduct(product);
            desk.setDryStorage(null);
            deskOakService.save(desk);
        }

//        product.setDeskOakList(deskOakList);
        product.setSumWidthOfAllDesk(String.valueOf(sumWidth));
        product.setCountOfDesk(String.valueOf(countOfAllDesk));
        product.setExtent(String.format("%.3f",extent).replace(',','.'));
        Date date = new Date(System.currentTimeMillis());
        product.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        if(product.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            product.setBreedDescription("");
        }
        productJPA.save(product);
        if(dryStorage!=null) {
            dryStorage.setExtent(String.format("%.3f", Float.parseFloat(dryStorage.getExtent()) - extent).replace(',', '.'));
            dryStorageService.save(dryStorage);
        }
        return product;
    }

    @Override
    public PackagedProduct getProductByDryStorage(int dryStorageId) {
        List<PackagedProduct> productList = productJPA.getProductByDryStorage(dryStorageId);
        if (productList.size()==0){
            return null;
        }else{
            return productList.get(0);
        }
    }

    @Override
    public PackagedProduct findById(int id) {
        return productJPA.getOne(id);
    }

    @Override
    public String countExtent(PackagedProduct product) {
        return String.format("%.3f",
                Float.parseFloat(product.getCountOfDesk())
                        *Float.parseFloat(product.getSizeOfHeight())
                        *Float.parseFloat(product.getSizeOfWidth())
                        *Float.parseFloat(product.getSizeOfLong())
                        /1000000000).replace(",",".");
    }

    @Override
    public void countExtentOak(PackagedProduct product) {
        int sumWidth = 0;
        int countOfAllDesk = 0;
        float extent = 0;

        float cofExtent = Float.parseFloat(product.getSizeOfHeight())*Float.parseFloat(product.getSizeOfLong())/1000000000;

        for(DescriptionDeskOak deskOak: product.getDeskOakList()){
            sumWidth+=(Integer.parseInt(deskOak.getSizeOfWidth())* Integer.parseInt(deskOak.getCountOfDesk()));
            countOfAllDesk+= Integer.parseInt(deskOak.getCountOfDesk());
            extent += (cofExtent* Float.parseFloat(deskOak.getSizeOfWidth())* Float.parseFloat(deskOak.getCountOfDesk()));
        }


        product.setSumWidthOfAllDesk(String.valueOf(sumWidth));
        product.setCountOfDesk(String.valueOf(countOfAllDesk));
        product.setExtent(String.format("%.3f",extent).replace(',','.'));
        if(product.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            product.setBreedDescription("");
        }
        productJPA.save(product);
    }

    @Override
    public String countDesk(PackagedProduct product) {
        return String.valueOf(Integer.parseInt(product.getCountDeskInWidth())*Integer.parseInt(product.getCountDeskInHeight())).replace(",",".");
    }

    @Override
    public List<PackagedProduct> findAll() {
        return productJPA.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    @Override
    public List<PackagedProduct> getListByUserByBreed(int breedId, int userId, StatusOfProduct status) {
        return productJPA.getListByUserByBreed(breedId,userId,status);
    }

    @Override
    public void deleteByID(int id) {
        productJPA.deleteById(id);
    }



    @Override
    public PackagedProduct editPackageProduct(PackagedProduct product) {
        PackagedProduct productDB = productJPA.getOne(product.getId());

        productDB.setCodeOfPackage(product.getCodeOfPackage());
        if (product.getBreedOfTree() != null) {
            productDB.setBreedOfTree(breedOfTreeService.getObjectByName(product.getBreedOfTree().getBreed()));
        }
        productDB.setBreedDescription(product.getBreedDescription());

        if(product.getContainer()!=null && productDB.getContainer()==null){
            productDB.setContainer(product.getContainer());
            productJPA.save(productDB);
            containerService.save(
                    containerService.findById(product.getContainer().getId())
            );
            orderInfoService.reloadExtentInContainer(productDB.getDeliveryDocumentation().getOrderInfo().getMainOrder());
        } else if(product.getContainer()!=null && productDB.getContainer().getId()!=product.getContainer().getId()){
            Container prevContainer = productDB.getContainer();
            productDB.setContainer(product.getContainer());
            productJPA.save(productDB);
            containerService.save(prevContainer);
            containerService.save(
                containerService.findById(product.getContainer().getId())
            );
            orderInfoService.reloadExtentInContainer(productDB.getDeliveryDocumentation().getOrderInfo().getMainOrder());
        } else if (product.getContainer()==null && productDB.getContainer()!=null){
            Container prevContainer = productDB.getContainer();
            productDB.setContainer(null);
            productJPA.save(productDB);
            containerService.save(prevContainer);

            orderInfoService.reloadExtentInContainer(productDB.getDeliveryDocumentation().getOrderInfo().getMainOrder());
        }

        productDB.setSizeOfHeight(product.getSizeOfHeight());
        productDB.setSizeOfWidth(product.getSizeOfWidth());
        productDB.setSizeOfLong(product.getSizeOfLong());
        productDB.setCodeOfDeliveryCompany(product.getCodeOfDeliveryCompany());

        productDB.setCountOfDesk(product.getCountOfDesk());
        System.out.println(productDB.getCountOfDesk());
        System.out.println(productDB.getId());
        productDB.setExtent(product.getExtent());
        productDB.setCountDeskInHeight(product.getCountDeskInHeight());
        productDB.setCountDeskInWidth(product.getCountDeskInWidth());

        productDB.setLongFact(product.getLongFact());
        productDB.setHeight_width(product.getHeight_width());
        if(productDB.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            productDB.setBreedDescription("");
        }
        productJPA.save(productDB);
        System.out.println(productDB.toString());
        return productDB;
    }

    @Override
    public PackagedProduct editPackageProductForImport(PackagedProduct product) {
        PackagedProduct productDB = productJPA.getOne(product.getId());

        productDB.setCodeOfPackage(product.getCodeOfPackage());
        if (product.getBreedOfTree() != null) {
            productDB.setBreedOfTree(breedOfTreeService.getObjectByName(product.getBreedOfTree().getBreed()));
        }
        productDB.setBreedDescription(product.getBreedDescription());

        if(product.getContainer()!=null && productDB.getContainer()==null){
            productDB.setContainer(product.getContainer());
            containerService.save(
                    containerService.findById(product.getContainer().getId())
            );
            orderInfoService.reloadExtentInContainer(productDB.getDeliveryDocumentation().getOrderInfo().getMainOrder());
        } else if(product.getContainer()!=null && productDB.getContainer().getId()!=product.getContainer().getId()){
            containerService.save(productDB.getContainer());
            productDB.setContainer(product.getContainer());
            containerService.save(
                    containerService.findById(product.getContainer().getId())
            );
            orderInfoService.reloadExtentInContainer(productDB.getDeliveryDocumentation().getOrderInfo().getMainOrder());
        } else if (product.getContainer()==null){
            productDB.setContainer(null);
            orderInfoService.reloadExtentInContainer(productDB.getDeliveryDocumentation().getOrderInfo().getMainOrder());

        }

        productDB.setSizeOfHeight(product.getSizeOfHeight());
        productDB.setSizeOfWidth(product.getSizeOfWidth());
        productDB.setSizeOfLong(product.getSizeOfLong());
        productDB.setCodeOfDeliveryCompany(product.getCodeOfDeliveryCompany());

        productDB.setCountOfDesk(product.getCountOfDesk());
        productDB.setCountDeskInHeight(product.getCountDeskInHeight());
        productDB.setCountDeskInWidth(product.getCountDeskInWidth());

        productDB.setLongFact(product.getLongFact());
        productDB.setHeight_width(product.getHeight_width());
        productDB.setExtent(countExtent(productDB));
        if(productDB.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            productDB.setBreedDescription("");
        }
        productJPA.save(productDB);
        return productDB;
    }


    @Override
    public PackagedProduct editPackageProductOak(PackagedProduct product) {
        PackagedProduct productDB = productJPA.getOne(product.getId());
        float oldPackExtent = Float.parseFloat(findById(productDB.getId()).getExtent());

        productDB.setCodeOfPackage(product.getCodeOfPackage());
        if (product.getBreedOfTree() != null) {
            productDB.setBreedOfTree(breedOfTreeService.getObjectByName(product.getBreedOfTree().getBreed()));
        }
        productDB.setSizeOfHeight(product.getSizeOfHeight());
        productDB.setSizeOfLong(product.getSizeOfLong());
        productDB.setQuality(product.getQuality());
        if(productDB.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            productDB.setBreedDescription("");
        }
        productJPA.save(productDB);
        countExtentOak(productDB);

        if (productDB.getDryStorage()!=null){
            DryStorage dryStorage = dryStorageService.findById(productDB.getDryStorage().getId());

            float oldExtent = Float.parseFloat(dryStorage.getExtent());
            dryStorage.setExtent(String.format("%.3f",oldExtent-(Float.parseFloat(productDB.getExtent())-oldPackExtent)).replace(",","."));
            dryStorageService.save(dryStorage);
        }
        return productDB;
    }

    @Override
    public void addDescriptionOak(String packId, String width, String count) {
        DescriptionDeskOak deskOak = new DescriptionDeskOak();
        deskOak.setCountOfDesk(count);
        deskOak.setSizeOfWidth(width);
        deskOakService.save(deskOak);
        PackagedProduct product = productJPA.getOne(Integer.parseInt(packId));
        product.getDeskOakList().add(deskOak);
        productJPA.save(product);
        editPackageProductOak(product);
    }



    @Override
    public void setContainer(String[] arrayOfPackagesId, String containerId,String containerName) {
        Set<Container> unicContainers = new HashSet<>();

        if (arrayOfPackagesId.length==0){
            return;
        }
        Container container;
        if(containerId.isEmpty()){
            container=null;
        }else if (StringUtils.isNumeric(containerId) && containerService.findById(Integer.parseInt(containerId))!=null) {
            container = containerService.findById(Integer.parseInt(containerId));
        }else {
            container = new Container();
            container.setCode(containerName);
            container.setContrAgent(
                    productJPA.getOne(Integer.parseInt(arrayOfPackagesId[0]))
                    .getDeliveryDocumentation().getOrderInfo().getMainOrder().getContrAgent()
            );
            container.setCodeOfOrderInfo(
                    productJPA.getOne(Integer.parseInt(arrayOfPackagesId[0]))
                            .getDeliveryDocumentation().getOrderInfo().getMainOrder().getCodeOfOrder()
            );
            containerService.save(container);
        }
        for (String id:arrayOfPackagesId) {
            PackagedProduct product = productJPA.getOne(Integer.parseInt(id));
            if(product.getContainer()!=null){
                unicContainers.add(product.getContainer());
            }
            product.setContainer(container);
            productJPA.save(product);
            orderInfoService.reloadExtentInContainer(product.getDeliveryDocumentation().getOrderInfo().getMainOrder());
        }
        if(!containerId.isEmpty()){
            containerService.save(container);
        }

        for (Container containerDB:unicContainers){
            containerService.save(containerDB);
        }
    }



    @Override
    public void deleteDescriptionOak(String packId, String deskId) {
        PackagedProduct product = productJPA.getOne(Integer.parseInt(packId));
        DescriptionDeskOak deskOak = deskOakService.findById(Integer.parseInt(deskId));

        product.getDeskOakList().remove(deskOak);
        productJPA.save(product);
        deskOakService.deleteByID(deskOak.getId());
        editPackageProductOak(product);
    }

    @Override
    public void unformPackageProduct(int breedId,int userId,Integer[] idArray) {
        for(Integer id:idArray) {
            PackagedProduct product = findById(id);

            if (product.getDryStorage() == null) {
                deleteByID(product.getId());
                continue;
            }

            DryStorage dryStorage = product.getDryStorage();

            if (breedId != 2) {
                dryStorage.setCountOfDesk(dryStorage.getCountOfDesk() + Integer.parseInt(product.getCountOfDesk()));
            } else {
                if(product.getDeskOakList().size()==0) {
                    dryStorage.setExtent(String.format("%.3f", Float.parseFloat(dryStorage.getExtent()) + Float.parseFloat(product.getExtent())).replace(",", "."));
                }else {
                    for(DescriptionDeskOak deskOak:product.getDeskOakList()){
                        deskOak.setPackagedProduct(null);
                        deskOak.setDryStorage(dryStorage);
                        deskOakService.save(deskOak);
                    }
                }
            }
            dryStorageService.save(dryStorage);
            deleteByID(id);
        }
    }

    public String countOfExtent(PackagedProduct packagedProduct){
        float width = Float.parseFloat(packagedProduct.getSizeOfWidth())/1000;
        float height = Float.parseFloat(packagedProduct.getSizeOfHeight())/1000;
        float longSize = Float.parseFloat(packagedProduct.getSizeOfLong())/1000;
        int count = Integer.parseInt(packagedProduct.getCountOfDesk());
        float extent = width*height*longSize*count;
        return String.format("%.3f",extent).replace(',','.');
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return productJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfHeight(int breedId) {
        return productJPA.getListOfUnicSizeOfHeight(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfWidth(int breedId) {
        return productJPA.getListOfUnicSizeOfWidth(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfLong(int breedId) {
        return productJPA.getListOfUnicSizeOfLong(breedId);
    }

    @Override
    public List<String> getExtent(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId, StatusOfProduct statusProduct) {
      if(breedId==2){
          return productJPA.getExtentOak(breedId,breedDesc,sizeHeight,sizeLong,agentId,statusProduct);
      }
      return productJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId,statusProduct);
    }

    @Override
    public List<String> getExtentByOrder(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId, DeliveryDestinationType type) {
        if(breedId==2){
            return productJPA.getExtentByOrderOak(breedId,breedDesc,sizeHeight,sizeLong,agentId,type);
        }
        return productJPA.getExtentByOrder(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId,type);
    }
}
