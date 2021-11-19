package com.swida.documetation.data.enums;

import java.util.ArrayList;
import java.util.List;

public enum StorageType {
    TREE_STATISTIC("Приход леса, история"),
    TREE("Приход леса"),
    RAW ("Сырой склад"),
    DRYING ("Сушка"),
    DRY ("Сухой склад"),
    PACKAGE ("Пачки"),
    DELIVERY ("Транспортировка");

    private final String meaning;

    StorageType(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }

    public static List<StorageType> convert(String[] types){
        List<StorageType> list = new ArrayList<>();
        for(String item : types){
            switch (item){
                case "TREE_STATISTIC": list.add(StorageType.TREE_STATISTIC); break;
                case "TREE": list.add(StorageType.TREE); break;
                case "RAW": list.add(StorageType.RAW); break;
                case "DRYING": list.add(StorageType.DRYING); break;
                case "DRY": list.add(StorageType.DRY); break;
                case "PACKAGE": list.add(StorageType.PACKAGE); break;
                case "DELIVERY": list.add(StorageType.DELIVERY); break;
            }
        }
        return list;
    }
}
