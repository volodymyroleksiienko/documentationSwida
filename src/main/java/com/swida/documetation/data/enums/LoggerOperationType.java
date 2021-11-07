package com.swida.documetation.data.enums;

public enum LoggerOperationType {
    GROUP_ITEMS ("Групировка"),
    UNGROUP_ITEMS ("Разгрупировка"),
    UPDATING ("Редактирование записи"),

    RETURNING ("Возврат записи"),
    CUTTING_TREE ("Распиловка"),
    SENDING ("Отправка в "),

    RETURN_TO_ZERO ("Обнуление"),
    CREATING ("Созднание записи"),
    DELETING ("Удаление записи"),
    TOTAL_DELETING ("Полностью удалить");

    private final String meaning;

    LoggerOperationType(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }
}
