package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.dto.StorageItem;
import com.swida.documetation.data.dto.TreeStorageListDto;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.jpa.storages.RawStorageJPA;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.QualityStatisticInfoService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class RawStorageServiceImpl implements RawStorageService {
    private final RawStorageJPA rawStorageJPA;
    private final TreeStorageService treeStorageService;
    private final BreedOfTreeService breedOfTreeService;
    private final UserCompanyService userCompanyService;
    private final QualityStatisticInfoService statisticInfoService;
    private final EntityManager entityManager;

    public QualityStatisticInfo checkQualityInfo(RawStorage rawStorage, String extent){
        QualityStatisticInfo info = checkQualityInfo(rawStorage);
        info.setExtent(extent);
        return statisticInfoService.save(info);
    }

    @Override
    public QualityStatisticInfo checkQualityInfo(RawStorage rawStorage) {
        QualityStatisticInfo info;
        rawStorage = findById(rawStorage.getId());
        if(rawStorage.getStatisticInfo()==null) {
            info = new QualityStatisticInfo();
        }else {
            info = rawStorage.getStatisticInfo();
        }
        if(info.getCodeOfTeam().isEmpty() || info.getCodeOfTeam().equals("0")){
            info.setCodeOfTeam(rawStorage.getCodeOfProduct());
        }
        info.setTreeStorage(treeStorageService.getMainTreeStorage(rawStorage.getBreedOfTree().getId(),rawStorage.getUserCompany().getId()));
        info.setHeight(rawStorage.getSizeOfHeight());
        info.setExtent(rawStorage.getExtent());
        info.setFirstExtent(rawStorage.getUsedExtent());
        info.setBreedDescription(rawStorage.getBreedDescription());
        info.setSizeOfWidth(rawStorage.getSizeOfWidth());
        info.setSizeOfLong(rawStorage.getSizeOfLong());

        info.setCountOfDesk(rawStorage.getCountOfDesk());

        if(rawStorage.getExtent().isEmpty() || rawStorage.getUsedExtent().isEmpty()){
            return null;
        }
        float percent = Float.parseFloat(rawStorage.getExtent())/Float.parseFloat(rawStorage.getUsedExtent()) * 100;
        info.setPercent(
                String.format("%.3f",percent).replace(",",".")
        );
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        info.setDate(dateFormat.format(date));
        info.setRawStorage(rawStorage);
        if(Float.parseFloat(rawStorage.getExtent())==0 &&
                Float.parseFloat(info.getExtent())==0 && info.getId()>0){
            rawStorage.setStatisticInfo(null);
            save(rawStorage);
            statisticInfoService.deleteByID(info.getId());
        }else {
            statisticInfoService.save(info);
            rawStorage.setStatisticInfo(info);
            save(rawStorage);
        }
        return info;
    }



    public QualityStatisticInfo create(RawStorage rawStorage, StorageItem item,double factExtent) {
        QualityStatisticInfo info =new QualityStatisticInfo();
        rawStorage = findById(rawStorage.getId());

        info.setTreeStorage(treeStorageService.getMainTreeStorage(rawStorage.getBreedOfTree().getId(),rawStorage.getUserCompany().getId()));
        info.setHeight(rawStorage.getSizeOfHeight());
        if(item!=null ) {
            info.setExtent(
                    String.format("%.3f",item.getExtent()).replace(",","."));
            info.setFirstExtent(
                    String.format("%.3f",factExtent).replace(",","."));
            info.setCountOfDesk(item.getCountOfDesk());
        }else {
            info.setExtent(rawStorage.getExtent());
            info.setFirstExtent(rawStorage.getUsedExtent());
            info.setCountOfDesk(rawStorage.getCountOfDesk());
        }
        info.setBreedDescription(rawStorage.getBreedDescription());
        info.setSizeOfWidth(rawStorage.getSizeOfWidth());
        info.setSizeOfLong(rawStorage.getSizeOfLong());

        float percent = Float.parseFloat(info.getExtent())/Float.parseFloat(info.getFirstExtent()) * 100;
        info.setPercent(
                String.format("%.3f",percent).replace(",",".")
        );
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        info.setDate(dateFormat.format(date));
        info.setRawStorage(rawStorage);
        if(Float.parseFloat(rawStorage.getExtent())==0 &&
                Float.parseFloat(info.getExtent())==0 && info.getId()>0){
            rawStorage.setStatisticInfo(null);
            save(rawStorage);
            statisticInfoService.deleteByID(info.getId());
        }else {
            statisticInfoService.save(info);
            rawStorage.setStatisticInfo(info);
            save(rawStorage);
        }
        return info;
    }

    @Override
    public RawStorage save(RawStorage rs) {
        if (rs.getSizeOfWidth()!=null && !rs.getSizeOfWidth().equals("0")){
            float width = Float.parseFloat(rs.getSizeOfWidth())/1000;
            float height = Float.parseFloat(rs.getSizeOfHeight())/1000;
            float longSize = Float.parseFloat(rs.getSizeOfLong())/1000;
            int count = rs.getCountOfDesk();
            float extent = width*height*longSize*count;
            rs.setExtent(String.format("%.3f",extent).replace(',','.'));
        }
        rs.setExtent(String.format("%.3f", Float.parseFloat(rs.getExtent())).replace(',', '.'));
        Date date = new Date(System.currentTimeMillis());
        rs.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        if(rs.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            rs.setBreedDescription("");
        }
        return rawStorageJPA.save(rs);
    }

    @Override
    public RawStorage findById(int id) {
        return rawStorageJPA.getOne(id);
    }

    @Override
    public List<RawStorage> findAll() {
        return rawStorageJPA.findAll();
    }

    @Override
    public List<RawStorage> findAllByTreeStorageId(int id) {
        return rawStorageJPA.findAllByTreeStorageId(id);
    }

    @Override
    public void analyzeOfCutting(TreeStorageListDto dto) {
        List<StorageItem> items = dto.getStorageItems();
        double factExtent = dto.getStorageItems().stream().mapToDouble(StorageItem::getExtent).reduce(Double::sum).getAsDouble();
        double qualityCoef = factExtent/dto.getExtent();
        for (StorageItem item : items) {
            String heights = String.valueOf(item.getSizeOfHeight());
            String widths = String.valueOf(item.getSizeOfWidth());
            String longs = String.valueOf(item.getSizeOfLong());
            RawStorage rawDB = findEqualRaw(dto.getBreedId(), dto.getUserId(), item.getDescription(), heights, widths, longs);
            if (rawDB == null) {
                rawDB = new RawStorage();
                rawDB.setGetBuCutting(true);
                rawDB.setCodeOfProduct(dto.getCodeOfProduct()+"-"+item.getSizeOfHeight());
                rawDB.setBreedDescription(item.getDescription());
                rawDB.setBreedOfTree(breedOfTreeService.findById(dto.getBreedId()));
                rawDB.setUserCompany(userCompanyService.findById(dto.getUserId()));
                rawDB.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

                rawDB.setSizeOfHeight(String.valueOf(item.getSizeOfHeight()));
                rawDB.setTreeStorage(treeStorageService.getMainTreeStorage(dto.getBreedId(),dto.getUserId()));

                rawDB.setSizeOfLong(String.valueOf(item.getSizeOfLong()));
                if(dto.getBreedId()==2) {
                    rawDB.setMaxExtent(
                            String.valueOf(item.getExtent()).replace(",",".")
                    );
                    rawDB.setExtent(rawDB.getMaxExtent());
                }else {
                    rawDB.setSizeOfWidth(String.valueOf(item.getSizeOfWidth()));
                    rawDB.setCountOfDesk(item.getCountOfDesk());
                    rawDB.setMaxCountOfDesk(rawDB.getCountOfDesk());
                    rawDB.setMaxExtent(
                            String.format("%.3f",item.getExtent()).replace(",",".")
                    );
                }
            }else {
                if (dto.getBreedId() == 2) {
                    rawDB.setExtent(
                            String.format("%.3f",
                                    Double.parseDouble(rawDB.getExtent()) + item.getExtent()
                            ).replace(",", ".")
                    );
                }else{
                    rawDB.setCountOfDesk(
                            rawDB.getCountOfDesk()+item.getCountOfDesk()
                    );
                }
            }
            rawDB.setUsedExtent(
                    String.format("%.3f",Double.parseDouble(rawDB.getUsedExtent())
                            +Double.parseDouble(rawDB.getExtent())/qualityCoef).replace(",",".")
            );
            QualityStatisticInfo info = create(save(rawDB),item,item.getExtent()/qualityCoef);
            info.setCodeOfTeam(dto.getCodeOfProduct()+"-"+rawDB.getSizeOfHeight());
            statisticInfoService.save(info);
        }

        TreeStorage main = treeStorageService.getMainTreeStorage(dto.getBreedId(),dto.getUserId());
        entityManager.refresh(main);
        main.setExtent(
                String.format("%.3f",(Double.parseDouble(main.getExtent())-dto.getExtent())).replace(",",".")
        );
        treeStorageService.save(main);
    }

    @Override
    public RawStorage findEqualRaw(int breedId, int userId, String desc, String heights, String widths, String longs) {
        Predicate<? super RawStorage> func;
        if(breedId==2){
            func = rawStorage -> rawStorage.getGetBuCutting()!=null && rawStorage.getGetBuCutting()==true &&
                    (rawStorage.getDeskOakList()==null || rawStorage.getDeskOakList().size()==0);
        } else {
            func = rawStorage -> rawStorage.getGetBuCutting()==true;
        }
        return rawStorageJPA.findEqualRaw(breedId,userId,desc,heights,widths,longs).stream()
                .filter(func).findAny().orElse(null);
    }

    @Override
    public List<RawStorage> getListByUserByBreed(int breedId, int userId) {
        if (breedId==2){
            return rawStorageJPA.getListByUserByBreedOak(breedId,userId);
        }
        return rawStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public List<RawStorage> getListByUserByBreedByStatusOfTree(int breedId, int userId, StatusOfTreeStorage status) {
        return rawStorageJPA.getListByUserByBreedByStatusOfTree(breedId,userId,status)
                .stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<RawStorage> getFilteredList(int breedId, int userId, String[] descriptions, String[] heights, String[] longs, String[] widths) {
        List<String> descList, heightList,widthList, longList;
        if (descriptions==null || descriptions.length==0){
            descList = getListOfUnicBreedDescription(breedId);
        }else {
            descList = Arrays.asList(descriptions);
        }
        if (heights==null || heights.length==0){
            heightList = getListOfUnicSizeOfHeight(breedId);
        }else {
            heightList = Arrays.asList(heights);
        }
        if (widths==null || widths.length==0){
            widthList = getListOfUnicSizeOfWidth(breedId);
        }else {
            widthList = Arrays.asList(widths);
        }
        if (longs==null || longs.length==0){
            longList = getListOfUnicSizeOfLong(breedId);
        }else {
            longList = Arrays.asList(longs);
        }
        System.out.println(descList);
        System.out.println(heightList);
        System.out.println(widthList);
        System.out.println(longList);
        if(breedId!=2) {
            return rawStorageJPA.getListByUserByBreed(breedId, userId, descList, heightList, widthList, longList);
        }else {
            return rawStorageJPA.getListByUserByBreedOak(breedId, userId, descList, heightList, longList);
        }
    }

    @Override
    public void countExtentRawStorageWithDeskDescription(RawStorage rawStorage) {
        entityManager.refresh(rawStorage);
        if(rawStorage.getDeskOakList()==null || rawStorage.getDeskOakList().size()==0){
            return;
        }
        float extent = 0;
        for(DescriptionDeskOak deskOak:  rawStorage.getDeskOakList()){
            extent+= (Double.parseDouble(deskOak.getSizeOfWidth())
                    *Double.parseDouble(deskOak.getCountOfDesk())
                    *Double.parseDouble(rawStorage.getSizeOfHeight())
                    *Double.parseDouble(rawStorage.getSizeOfLong())
                    /1000000000);
        }
        if (Double.parseDouble(rawStorage.getExtent())!=extent){
            TreeStorage treeStorage = rawStorage.getTreeStorage();
            treeStorage.setExtent(
                    String.format("%.3f",
                            Double.parseDouble(treeStorage.getExtent())+
                                    (Double.parseDouble(rawStorage.getExtent())-extent))
                            .replace(",",".")

            );
            checkQualityInfo(rawStorage);
            treeStorageService.save(treeStorage);
        }
        rawStorage.setExtent(
                String.format("%.3f",extent).replace(",",".")
        );
        checkQualityInfo(rawStorage);
        rawStorageJPA.save(rawStorage);
    }

    @Override
    public BigDecimal countExtent(List<RawStorage> rawStorages) {
        double sum=0;
        for(RawStorage storage : rawStorages){
            sum += Double.parseDouble(storage.getExtent());
        }
        return new BigDecimal(sum);
    }

    public void collectToOnePineEntity(RawStorage rawStorage,Integer[] arrOfEntity,int userId,int breedId){
        int countOfDesk = 0;
        double extend=0;
        List<RawStorage> groupedList = new ArrayList<>();
        for(Integer id:arrOfEntity){
            RawStorage temp =rawStorageJPA.findById(id).orElse(null);
            if(temp!=null) {
                if(breedId==1) {
                    countOfDesk += temp.getCountOfDesk();
                }else{
                    extend+=Double.parseDouble(temp.getExtent());
                }
                groupedList.add(temp);
                temp.setStatusOfEntity(StatusOfEntity.GROUPED_BY_PROPERTIES);
                rawStorageJPA.save(temp);
            }
        }

        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setGroupedElements(groupedList);
        rawStorage.setCountOfDesk(countOfDesk);
        System.out.println(rawStorage);
        String mainExtend = save(rawStorage).getExtent();
        rawStorage.setMaxExtent(mainExtend);
        save(rawStorage);
    }

    @Override
    public void uncollectFromOnePineEntity(RawStorage rawStorage, int userId, int breedId) {
        if(rawStorage.getGroupedElements()!=null && rawStorage.getGroupedElements().size()>0){
            for(RawStorage grouped:rawStorage.getGroupedElements()){
                grouped.setStatusOfEntity(StatusOfEntity.ACTIVE);
                save(grouped);
            }
            deleteByID(rawStorage.getId());
        }
    }

    @Override
    public void collectToOneOakEntity(RawStorage rawStorage, Integer[] arrOfEntity, int userId, int breedId) {
        Set<String> width = new TreeSet<>();
        List<RawStorage> rawsFromDBList=rawStorageJPA.findAllById(Arrays.asList(arrOfEntity));
        if(rawsFromDBList.size()>0 && rawsFromDBList.get(0).getDeskOakList().size()==0){
            System.out.println(rawsFromDBList.get(0).getDeskOakList());
            collectToOnePineEntity(rawStorage,arrOfEntity,userId,breedId);
            return;
        }
        for(RawStorage temp:rawsFromDBList){
            if(temp!=null && temp.getDeskOakList().size()>0){
                for(DescriptionDeskOak deskOak:temp.getDeskOakList()) {
                    width.add(deskOak.getSizeOfWidth());
                }
                temp.setStatusOfEntity(StatusOfEntity.GROUPED_BY_PROPERTIES);
            }
        }
        rawStorageJPA.saveAll(rawsFromDBList);
        List<DescriptionDeskOak> deskOakList=new ArrayList<>();
        for(String widthOfDesk:width) {
            int countOfDesk = 0;
            for(RawStorage temp:rawsFromDBList){
                if (temp != null && temp.getDeskOakList().size() > 0) {
                    for (DescriptionDeskOak deskOak : temp.getDeskOakList()) {
                        if(deskOak.getSizeOfWidth().equals(widthOfDesk)){
                            countOfDesk+=Integer.parseInt(deskOak.getCountOfDesk());
                        }
                    }
                }
            }
            DescriptionDeskOak desk = new DescriptionDeskOak(widthOfDesk,String.valueOf(countOfDesk));
            desk.setRawStorage(rawStorage);
            deskOakList.add(desk);
        }

        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setGroupedElements(rawsFromDBList);
        rawStorage.setDeskOakList(deskOakList);
        String extend = save(rawStorage).getExtent();
        rawStorage.setMaxExtent(extend);
        save(rawStorage);
    }


    @Override
    public void deleteByID(int id) {
        rawStorageJPA.deleteById(id);
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return rawStorageJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfHeight(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfHeight(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfWidth(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfWidth(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfLong(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfLong(breedId);
    }

    @Override
    public List<String> getExtent(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId) {
        if(breedId==2){
            return rawStorageJPA.getExtentOak(breedId,breedDesc,sizeHeight,agentId);
        }
        return rawStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }

    public List<RawStorage> sortedBy(List<RawStorage> list,String sortedField, String sortedType){
        Comparator<RawStorage> comparator = (o1, o2) -> o2.getId()-o1.getId();
        System.out.println("field "+sortedField);
        System.out.println(sortedType);
        if(sortedField!=null && sortedType!=null) {
            switch (sortedField) {
                case "date":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparing(RawStorage::getDate);
                    } else {
                        comparator = (o1, o2) -> o2.getDate().compareTo(o1.getDate());
                    }
                    break;
                case "code":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparing(RawStorage::getCodeOfProduct);
                    } else {
                        comparator = (o1, o2) -> o2.getCodeOfProduct().compareTo(o1.getCodeOfProduct());
                    }
                    break;
                case "breedDescription":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparing(RawStorage::getBreedDescription);
                    } else {
                        comparator = (o1, o2) -> o2.getBreedDescription().compareTo(o1.getBreedDescription());
                    }
                    break;
                case "height":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparingDouble(o -> Double.parseDouble(o.getSizeOfHeight()));
                    } else {
                        comparator = (o1, o2) -> Double.compare(Double.parseDouble(o2.getSizeOfHeight()), Double.parseDouble(o1.getSizeOfHeight()));
                    }
                    break;
                case "long":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparingDouble(o -> Double.parseDouble(o.getSizeOfLong()));
                    } else {
                        comparator = (o1, o2) -> Double.compare(Double.parseDouble(o2.getSizeOfLong()), Double.parseDouble(o1.getSizeOfLong()));
                    }
                    break;
                case "extent":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparingDouble(o -> Double.parseDouble(o.getExtent()));
                    } else {
                        comparator = (o1, o2) -> Double.compare(Double.parseDouble(o2.getExtent()), Double.parseDouble(o1.getExtent()));
                    }
                    break;
            }
        }
        list.sort(comparator);
        return list;
    }
}
