package com.swida.documetation.data.enums;

import java.util.ArrayList;
import java.util.List;

public enum LoggerOperationType {
    GROUP_ITEMS ("Групировка"),
    UNGROUP_ITEMS ("Разгрупировка"),
    UPDATING ("Редактирование записи"),

    RETURNING ("Возврат записи"),
//    CUTTING_TREE ("Распиловка"),
    SENDING ("Перевод с предыдущего склада"),

    RETURN_TO_ZERO ("Обнуление"),
    CREATING ("Созднание записи"),
    DELETING ("Удаление записи");
//    TOTAL_DELETING ("Полностью удалить");

    private final String meaning;

    LoggerOperationType(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }

    public static List<LoggerOperationType> convert(String[] types){
        List<LoggerOperationType> list = new ArrayList<>();
        for(String item : types){
            switch (item){
                case "GROUP_ITEMS": list.add(LoggerOperationType.GROUP_ITEMS); break;
                case "UNGROUP_ITEMS": list.add(LoggerOperationType.UNGROUP_ITEMS); break;
                case "UPDATING": list.add(LoggerOperationType.UPDATING); break;
                case "RETURNING": list.add(LoggerOperationType.RETURNING); break;
//                case "CUTTING_TREE": list.add(LoggerOperationType.CUTTING_TREE); break;
                case "SENDING": list.add(LoggerOperationType.SENDING); break;
                case "RETURN_TO_ZERO": list.add(LoggerOperationType.RETURN_TO_ZERO); break;
                case "CREATING": list.add(LoggerOperationType.CREATING); break;
                case "DELETING": list.add(LoggerOperationType.DELETING); break;
//                case "TOTAL_DELETING": list.add(LoggerOperationType.TOTAL_DELETING); break;
            }
        }
        return list;
    }
}
