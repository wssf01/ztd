package com.bike.ztd.service;

import com.bike.ztd.dto.UserImportDTO;
import com.bike.ztd.dto.UserImportTotalDTO;

import java.util.List;

public interface ImportService {

    default String importTotalKey(String uuid) {
        return String.format("str.user.import.total:%s", uuid);
    }


    default String importFailureKey(String uuid) {
        return String.format("list.user.import.total:%s", uuid);
    }

    default long importKeyExpire() {
        return 1000 * 60 * 60;
    }

    /**
     * 检查文件格式
     *
     * @param name
     */
    void checkFileType(String name);

    void imports(List<UserImportDTO> list, UserImportTotalDTO totalResult, String uuid);

    UserImportTotalDTO importInfo(String uuid);
}
