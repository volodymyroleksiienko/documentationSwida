package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.jpa.storages.PackagedProductJPA;
import com.swida.documetation.data.jpa.subObjects.ContainerJPA;
import com.swida.documetation.data.service.subObjects.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerServiceImpl implements ContainerService {
    private ContainerJPA containerJPA;
    private PackagedProductJPA productJPA;

    @Autowired
    public ContainerServiceImpl(ContainerJPA containerJPA, PackagedProductJPA productJPA) {
        this.containerJPA = containerJPA;
        this.productJPA = productJPA;
    }

    @Override
    public void save(Container container) {
        float costOfDeliveryPort = Float.parseFloat(container.getCostOfDeliveryToPort());
        float exchangeRate = Float.parseFloat(container.getExchangeRate());
        float costOfWeighing = Float.parseFloat(container.getCostOfWeighing());
        float coefUploading = Float.parseFloat(container.getCoefUploading());
        container.setExtent(getExtentInContainer(container.getId()));
        float extent = Float.parseFloat(getExtentInContainer(container.getId()));
        float costOfUploading = coefUploading * extent;

        container.setExchangeRate(
                String.format("%.2f",exchangeRate).replace(",",".")
        );
        container.setCoefUploading(
                String.format("%.2f",coefUploading).replace(",",".")
        );

        container.setCostOfUploading(
                String.format("%.2f",costOfUploading).replace(",",".")
        );
        container.setEqualsToUAH(
                String.format("%.2f",
                        (costOfDeliveryPort+(coefUploading*extent))
                                *exchangeRate+costOfWeighing
                ).replace(",",".")
        );
        containerJPA.save(container);
    }

    @Override
    public String getExtentInContainer(int containerId) {
        List<String> extentList = productJPA.getExtentInContainer(containerId);
        float extent = 0;
        for(String ex:extentList){
            extent+=Float.parseFloat(ex);
        }
        return String.format("%.3f",extent).replace(",",".");
    }

    @Override
    public void setContainerCurrency(String[] containerId, String currency) {
        for(String id:containerId){
            Container container = containerJPA.getOne(Integer.parseInt(id));
            container.setExchangeRate(currency.replace(",","."));
            save(container);
        }
    }

    @Override
    public Container findById(int id) {
        return containerJPA.getOne(id);
    }

    @Override
    public List<Container> findAll() {
        return containerJPA.findAll();
    }

    @Override
    public List<Container> selectByStatusOfEntity(StatusOfEntity status) {
        return containerJPA.selectByStatusOfEntity(status);
    }

    @Override
    public void deleteByID(int id) {
        containerJPA.deleteById(id);
    }
}
