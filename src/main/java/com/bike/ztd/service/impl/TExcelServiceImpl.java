package com.bike.ztd.service.impl;

import com.bike.ztd.dto.ImportDTO;
import com.bike.ztd.entity.TExcel;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.mapper.TExcelMapper;
import com.bike.ztd.service.OSSService;
import com.bike.ztd.service.TExcelService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bike.ztd.util.CheckUtils;
import com.bike.ztd.util.IoFileUtils;
import com.bike.ztd.util.JxlsUtils;
import com.bike.ztd.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.google.common.io.Files.createTempDir;

/**
 * <p>
 * excel导出文件表 服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Slf4j
@Service
public class TExcelServiceImpl extends ServiceImpl<TExcelMapper, TExcel> implements TExcelService {

    @Autowired
    private OSSService ossService;

    @Override
    public void setExcel(ImportDTO vo) {
        try {
            String fileName = vo.getType().getValue();
            int i = fileName.lastIndexOf(".");
            fileName = fileName.substring(0, i);

            String TimeNow = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

            final String name = fileName + TimeNow + ".xlsx";
            File parentPath = createTempDir();
            File tempFile = new File(parentPath, name);
            ClassPathResource resource = new ClassPathResource("template/" + vo.getType().getValue());
            InputStream is = resource.getInputStream();
            JxlsUtils.exportExcel(is, tempFile, vo.getModel());
            //oss
            String ossPath;
            try {
                ossPath = ossService.upload(name, tempFile);
            }catch (Exception e) {
                RedisUtils.set(vo.getKey(), "upload fail："+e.getMessage(), 300000);
                throw new BusinessException("export_error", e.getMessage());
            }
            //指定数据生成后的文件输入流
            FileInputStream fileInputStream = new FileInputStream(tempFile);
            String content = CheckUtils.getBase64FromOutputStream(fileInputStream);
            //存入数据库
            TExcel excel = new TExcel();
            excel.setPkId(vo.getModelId());
            excel.setExcelContent(content);
            excel.setFileName(vo.getFileName());
            excel.setCreateTime(new Date());
            excel.setExcelType(vo.getType());
            excel.setOssPath(ossPath);
            this.insert(excel);
            fileInputStream.close();
            IoFileUtils.deleteDir(tempFile);
        } catch (Exception e) {
            RedisUtils.set(vo.getKey(), e.getMessage(), 300000);
            e.printStackTrace();
            throw new BusinessException("export_error", e.getMessage());
        }
    }
}
