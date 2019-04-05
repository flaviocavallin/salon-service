package com.example.salon.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum FileType {

    CLIENTS("clients"), APPOINTMENTS("appointments"), PURCHASES("purchases"), SERVICES("services");


    private static Map<String, FileType> map = new HashMap<>();

    static {

        for (FileType fileType : FileType.values()) {
            map.put(fileType.getName(), fileType);
        }
    }

    private String name;

    FileType(String name) {
        this.name = Objects.requireNonNull(name, "name can not be null");
    }


    public static FileType getByName(String name) {
        FileType fileType = map.get(name);
        if (fileType == null) {
            throw new IllegalArgumentException("FileName not found=" + name);
        }
        return fileType;
    }

    public String getName() {
        return name;
    }
}
