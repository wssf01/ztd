package com.bike.ztd.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserImportDTO {
    private String name;
    private String phone;


    public static UserImportDTO build(List<String> list, Set<String> phoneSet, Set<String> set) {
        String identity = list.get(1);
        if (!phoneSet.add(identity)) {
            set.add(identity);
        }
        return new UserImportDTO(list.get(0), identity);
    }

    public UserImportDTO() {
    }

    public UserImportDTO(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
