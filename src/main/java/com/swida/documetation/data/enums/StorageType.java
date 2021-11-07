package com.swida.documetation.data.enums;

public enum StorageType {
    TREE("Приход леса"),
    RAW ("Сирой склад"),
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
}
