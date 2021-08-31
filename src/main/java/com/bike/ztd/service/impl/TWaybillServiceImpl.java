package com.bike.ztd.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.bike.ztd.dto.ImportDTO;
import com.bike.ztd.dto.WaybillExportDTO;
import com.bike.ztd.entity.TExcel;
import com.bike.ztd.entity.TWaybill;
import com.bike.ztd.entity.TWaybillCar;
import com.bike.ztd.entity.TWaybillPhoto;
import com.bike.ztd.enums.CarTypeEnum;
import com.bike.ztd.enums.ExcelType;
import com.bike.ztd.enums.WaybillEnum;
import com.bike.ztd.enums.WaybillInfoEnum;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.mapper.TWaybillMapper;
import com.bike.ztd.qo.*;
import com.bike.ztd.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bike.ztd.util.DateUtils;
import com.bike.ztd.util.RedisUtils;
import com.bike.ztd.util.SystemDefine;
import com.bike.ztd.util.UUIDUtils;
import com.bike.ztd.vo.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bike.ztd.util.DateUtils.TIMESTAMP_FORMAT;
import static com.bike.ztd.util.JWTUtils.getUserIdByToken;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@Slf4j
@Service
public class TWaybillServiceImpl extends ServiceImpl<TWaybillMapper, TWaybill> implements TWaybillService {
    @Autowired
    private TWaybillCarService waybillCarService;

    @Autowired
    private TWaybillPhotoService waybillPhotoService;

    @Autowired
    private TUserService userService;

    @Autowired
    private TExcelService excelService;

    @Autowired
    private OSSService ossService;

    @Autowired
    private TUserRoleService userRoleService;

    @Override
    public String addWaybill(WaybillAddQO qo) {
        String userId = getUserIdByToken();
        String uuid = UUIDUtils.timeBasedStr();
        TWaybill waybill = new TWaybill();
        BeanUtils.copyProperties(qo, waybill);
        waybill.setPkId(uuid);
        waybill.setCreateTime(new Date());
        waybill.setUserId(userId);
        waybill.setWaybillStatus(WaybillEnum.DOING);
        baseMapper.insert(waybill);
        log.info("Waybill add success:" + uuid);
        return uuid;
    }

    @Override
    public void completeWaybill(WaybillCompleteQO qo) {
        TWaybill waybill = baseMapper.selectById(qo.getWaybillId());
        if (waybill == null) {
            throw new BusinessException("WAYBILL_NOT_EXIST", "运单不存在");
        }
        String userId = getUserIdByToken();
        if(userId != null && !userId.equals(waybill.getUserId())){
            throw new BusinessException("NO_PERMISSION", "不是运单创建人没有操作权限");
        }
        waybill.setWaybillStatus(WaybillEnum.COMPLETE);
        waybill.setLatitudeComplete(qo.getLatitude());
        waybill.setLocalComplete(qo.getLocal());
        waybill.setLongitudeComplete(qo.getLongitude());
        waybill.setCompleteAt(new Date());
        waybill.setCityComplete(qo.getCity());
        waybill.setRemarks(qo.getRemake());
        baseMapper.updateById(waybill);
        log.info("Waybill complete success:" + waybill.getPkId());
    }

    @Override
    public void deleteWaybill(String waybillId) {
        if (StringUtils.isEmpty(waybillId)) {
            return;
        }
        String userId = getUserIdByToken();
        //删除运单
        TWaybill waybill = baseMapper.selectById(waybillId);
        if (userId != null && waybill != null) {
            if(!userId.equals(waybill.getUserId())){
                throw new BusinessException("NO_PERMISSION", "不是运单创建人没有操作权限");
            }
            waybill.setWaybillStatus(WaybillEnum.DELETE);
            baseMapper.updateById(waybill);
        }
        //删除运单记录
        waybillCarService.deleteByWaybillId(waybillId, null);
        waybillPhotoService.deleteByWaybillId(waybillId, null);
        log.info("Waybill delete success:" + waybillId);
    }

    @Override
    public Page<MyWaybillListVO> myList(int current, int size, Long startAt, Long endAt) {
        //组装查询条件
        Map<String, Object> map = Maps.newHashMap();
        String userId = getUserIdByToken();
        map.put("userId", userId);
        if (startAt != null) {
            map.put("startAt", new Date(startAt));
        }
        if (endAt != null) {
            map.put("endAt", new Date(endAt));
        }
        //查询相关信息
        Page<TWaybill> page = new Page<>(current, Math.min(size, SystemDefine.MAX_PAGE_SIZE));
        List<TWaybill> list = this.baseMapper.pageList(map, page);
        //组装数据
        Page<MyWaybillListVO> resultPage = new Page<>(current, Math.min(size, SystemDefine.MAX_PAGE_SIZE));
        List<MyWaybillListVO> resultList = Lists.newArrayList();
        list.forEach(waybill -> {
            MyWaybillListVO vo = new MyWaybillListVO();
            BeanUtils.copyProperties(waybill, vo);
            //收车数
            vo.setNumberCollect(waybillCarService.getNumberCollectByWaybillId(waybill.getPkId()));
            resultList.add(vo);
        });
        resultPage.setRecords(resultList);
        resultPage.setTotal(page.getTotal());
        return resultPage;
    }

    @Override
    public Page<WaybillListVO> list(String userId, String userPhone, String local, int current, int size, Long startAt, Long endAt) {
        //组装查询条件
        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", userId);
        map.put("userPhone", userPhone);
        map.put("local", local);
        if (startAt != null) {
            map.put("startAt", new Date(startAt));
        }
        if (endAt != null) {
            map.put("endAt", new Date(endAt));
        }
        //查询相关信息
        Page<TWaybill> page = new Page<>(current, Math.min(size, SystemDefine.MAX_PAGE_SIZE));
        List<TWaybill> list = this.baseMapper.pageList(map, page);
        //组装数据
        Page<WaybillListVO> resultPage = new Page<>(current, Math.min(size, SystemDefine.MAX_PAGE_SIZE));
        List<WaybillListVO> resultList = assembleResult(list);
        //结果设置
        resultPage.setRecords(resultList);
        resultPage.setTotal(page.getTotal());
        return resultPage;
    }

    private List<WaybillListVO> assembleResult(List<TWaybill> list) {
        List<WaybillListVO> resultList = Lists.newArrayList();
        Set<String> userIds = list.stream().map(TWaybill::getUserId).collect(Collectors.toSet());
        HashMap<String, String> userMap = userService.findPhoneByUserIds(userIds);
        list.forEach(waybill -> {
            //运单信息补充
            WaybillListVO vo = new WaybillListVO();
            BeanUtils.copyProperties(waybill, vo);
            vo.setLocal(waybill.getCity());
            //用户信息补充
            vo.setUserPhone(userMap.get(waybill.getUserId()) == null ? "" : userMap.get(waybill.getUserId()));
            //运单车辆信息补充
            List<TWaybillCar> waybillCarList = waybillCarService.getCarListByWaybillId(waybill.getPkId());
            int disboardNum = 0;
            for (TWaybillCar car : waybillCarList) {
                if (WaybillInfoEnum.COLLECT.equals(car.getWaybillType())) {
                    //收车
                    if (CarTypeEnum.BICYCLE.equals(car.getCarType())) {
                        vo.setNumberBicycle(getCarNumbers(car).size());
                        vo.setBicycleList(getCarNumbers(car));
                    }
                    if (CarTypeEnum.ELECTRIC.equals(car.getCarType())) {
                        vo.setNumberElectric(getCarNumbers(car).size());
                        vo.setElectricList(getCarNumbers(car));
                    }
                }else{
                    disboardNum += getCarNumbers(car).size();
                }
            }
            vo.setNumberDisboard(disboardNum);
            vo.setNumberCollect(vo.getNumberBicycle() + vo.getNumberElectric());
            //图片信息
            List<TWaybillPhoto> photoList = waybillPhotoService.selectListByWaybillId(waybill.getPkId());
            List<PhotoInfoVO> photoInfoList = Lists.newArrayList();
            photoList.forEach(photo -> {
                PhotoInfoVO photoInfoVO = new PhotoInfoVO();
                BeanUtils.copyProperties(photo, photoInfoVO);
                photoInfoList.add(photoInfoVO);
            });
            vo.setPhotoInfoList(photoInfoList);
            resultList.add(vo);
        });
        return resultList;
    }

    private List<String> getCarNumbers(TWaybillCar car) {
        List<String> carNumberList = Lists.newArrayList();
        String carNumber = car.getCarNumber();
        if (StringUtils.isEmpty(carNumber)) {
            return carNumberList;
        }
        return Arrays.asList(carNumber.split(","));
    }

    @Override
    public WaybillDetailsVO details(String waybillId) {
        //运单信息
        TWaybill waybill = baseMapper.selectById(waybillId);
        if (waybill == null) {
            throw new BusinessException("WAYBILL_NOT_EXIST", "运单不存在");
        }
        String userId = getUserIdByToken();
        if(userId != null){
            List<String> role = userRoleService.findRoleIdsByUserId(userId);
            if (!role.contains(SystemDefine.ROLE_ADMIN_ID)) {
                //非管理员
                if(!userId.equals(waybill.getUserId())){
                    throw new BusinessException("NO_PERMISSION", "不是运单创建人没有操作权限");
                }
            }
        }

        WaybillDetailsVO vo = new WaybillDetailsVO();
        BeanUtils.copyProperties(waybill, vo);
        //车辆信息
        List<TWaybillCar> carList = waybillCarService.getCarListByWaybillId(waybillId);
        int collectBicycle = 0;
        int collectElectric = 0;
        int disboardBicycle = 0;
        int disboardElectric = 0;
        List<CarInfoVO> carInfoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(carList)) {
            for (TWaybillCar waybillCar : carList) {
                CarInfoVO carInfoVO = new CarInfoVO();
                carInfoVO.setCarNumbers(Arrays.asList(waybillCar.getCarNumber().split(",")));
                if (WaybillInfoEnum.COLLECT.equals(waybillCar.getWaybillType())) {
                    carInfoVO.setType(WaybillInfoEnum.COLLECT);
                    if (CarTypeEnum.ELECTRIC.equals(waybillCar.getCarType())) {
                        collectElectric = getCarNumbers(waybillCar).size();
                        carInfoVO.setCarType(CarTypeEnum.ELECTRIC);
                    }
                    if (CarTypeEnum.BICYCLE.equals(waybillCar.getCarType())) {
                        collectBicycle = getCarNumbers(waybillCar).size();
                        carInfoVO.setCarType(CarTypeEnum.BICYCLE);
                    }
                }
                if (WaybillInfoEnum.DISBOARD.equals(waybillCar.getWaybillType())) {
                    carInfoVO.setType(WaybillInfoEnum.DISBOARD);
                    if (CarTypeEnum.ELECTRIC.equals(waybillCar.getCarType())) {
                        disboardElectric = getCarNumbers(waybillCar).size();
                        carInfoVO.setCarType(CarTypeEnum.ELECTRIC);
                    }
                    if (CarTypeEnum.BICYCLE.equals(waybillCar.getCarType())) {
                        disboardBicycle = getCarNumbers(waybillCar).size();
                        carInfoVO.setCarType(CarTypeEnum.BICYCLE);
                    }
                }
                carInfoList.add(carInfoVO);
            }
        }
        //图片信息
        List<TWaybillPhoto> photoList = waybillPhotoService.selectListByWaybillId(waybill.getUserId());
        List<PhotoInfoVO> collectList = Lists.newArrayList();
        List<PhotoInfoVO> disdoardList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(photoList)) {
            photoList.forEach(photo -> {
                PhotoInfoVO photoInfoVO = new PhotoInfoVO();
                BeanUtils.copyProperties(photo, photoInfoVO);
                if (WaybillInfoEnum.COLLECT.equals(photo.getWaybillType())) {
                    collectList.add(photoInfoVO);
                }
                if (WaybillInfoEnum.DISBOARD.equals(photo.getWaybillType())) {
                    disdoardList.add(photoInfoVO);
                }
            });
        }
        vo.setCollectList(collectList);
        vo.setCollectBicycle(collectBicycle);
        vo.setCollectElectric(collectElectric);
        vo.setDisdoardList(disdoardList);
        vo.setDisboardBicycle(disboardBicycle);
        vo.setDisboardElectric(disboardElectric);
        vo.setCarInfoList(carInfoList);
        return vo;
    }

    @Override
    public void listExport(String key, Map<String, Object> map) {
        List<TWaybill> list = this.baseMapper.listExport(map);
        if (CollectionUtils.isEmpty(list)) {
            if (StringUtils.isBlank(key)) {
                throw new BusinessException("list_empty", "无可导出数据");
            } else {
                RedisUtils.set(key + "error", "无可导出数据", 300000);
                return;
            }
        }
        //组装数据
        List<WaybillListVO> resultList = assembleResult(list);
        List<WaybillExportDTO> waybills = assembleExport(resultList);
        //导出
        Map<String, Object> model = new HashMap<>(2);
        model.put("waybills", waybills);
        model.put("date", new Date());
        //要导出的Excel暂存表中
        String modelId = UUIDUtils.timeBasedStr();
        ImportDTO dto = new ImportDTO();
        dto.setModelId(modelId);
        dto.setFileName("运单列表导出");
        dto.setType(ExcelType.WAYBILL_LIST_IMPORT);
        dto.setModel(model);
        dto.setKey(key);
        excelService.setExcel(dto);
        RedisUtils.set(key, modelId, 300000);
        log.info("Waybill export success");
    }

    private List<WaybillExportDTO> assembleExport(List<WaybillListVO> resultList) {
        List<WaybillExportDTO> waybills = Lists.newArrayList();
        resultList.forEach(vo -> {
            WaybillExportDTO dto = new WaybillExportDTO();
            dto.setCity(vo.getLocal());
            dto.setCreatedAt(DateUtils.getDate(vo.getCreatedAt(), TIMESTAMP_FORMAT));
            dto.setNumberCollect(vo.getNumberCollect());
            dto.setNumberDisboard(vo.getNumberDisboard());
            dto.setUserPhone(vo.getUserPhone());
            if (CollectionUtils.isNotEmpty(vo.getBicycleList())) {
                dto.setBicycleList(StringUtils.join(vo.getBicycleList().toArray(), ","));
            }
            if (CollectionUtils.isNotEmpty(vo.getElectricList())) {
                dto.setElectricList(StringUtils.join(vo.getElectricList().toArray(), ","));
            }
            StringBuilder local = new StringBuilder();
            StringBuilder situation = new StringBuilder();
            StringBuilder outLocal = new StringBuilder();
            StringBuilder outSituation = new StringBuilder();
            List<PhotoInfoVO> list = vo.getPhotoInfoList();
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(info -> {
                    if (WaybillInfoEnum.COLLECT.equals(info.getType())) {
                        local.append(info.getLocal()).append(";\r\n");
                        situation.append("{\"lat\":").append(info.getLatitude()).append(",\"lng\":").append(info.getLongitude()).append("}\r\n");
                    } else if (WaybillInfoEnum.DISBOARD.equals(info.getType())) {
                        outLocal.append(info.getLocal()).append(";\r\n");
                        outSituation.append("{\"lat\":").append(info.getLatitude()).append(",\"lng\":").append(info.getLongitude()).append("}\r\n");
                    }
                });
            }
            dto.setLocal(local.toString());
            dto.setSituation(situation.toString());
            dto.setOutLocal(outLocal.toString());
            dto.setOutSituation(outSituation.toString());
            waybills.add(dto);
        });
        return waybills;
    }

    @Override
    public String checkExport(String key) {
        Object modelId = RedisUtils.get(key);
        if (modelId == null) {
            throw new BusinessException("export_key_expire", "key值已失效");
        }
        String msg = String.valueOf(modelId);
        if (msg.contains("upload fail")) {
            throw new BusinessException("export_excel_fail", msg);
        }
        TExcel excel = excelService.selectById(msg);
        if (excel == null) {
            throw new BusinessException("export_excel_doing", "excel还在制作中");
        }
        return ossService.downLoad(excel.getOssPath());
    }

    @Override
    public void addInfo(WaybillInfoAddQO qo) {
        TWaybill waybill = baseMapper.selectById(qo.getWaybillId());
        if (waybill == null) {
            throw new BusinessException("WAYBILL_NOT_EXIST", "运单不存在");
        }
        if (WaybillEnum.COMPLETE.equals(waybill.getWaybillStatus())) {
            throw new BusinessException("WAYBILL_COMPLETE", "运单已完成不允许此操作");
        }
        Date date = new Date();
        WaybillInfoEnum type = qo.getType();
        //车辆信息
        waybillCarService.deleteByWaybillId(qo.getWaybillId(), qo.getType());
        List<CarInfoQO> carInfoList = qo.getCarInfoList();
        List<TWaybillCar> waybillCarList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(carInfoList)) {
            carInfoList.forEach(carInfo -> {
                List<String> carNumberList = carInfo.getCarNumbers();
                if (CollectionUtils.isNotEmpty(carNumberList)) {
                    String carNumbers = StringUtils.join(carNumberList.toArray(), ",");
                    TWaybillCar waybillCar = new TWaybillCar();
                    waybillCar.setPkId(UUIDUtils.timeBasedStr());
                    waybillCar.setCarNumber(carNumbers);
                    waybillCar.setCarType(carInfo.getCarType());
                    waybillCar.setCreateTime(date);
                    waybillCar.setWaybillId(qo.getWaybillId());
                    waybillCar.setWaybillType(type);
                    waybillCarList.add(waybillCar);
                }
            });
        }
        if (CollectionUtils.isNotEmpty(waybillCarList)) {
            waybillCarService.insertBatch(waybillCarList);
        }
        //照片
        List<PhotoInfoQO> photoList = qo.getPhotoList();
        List<TWaybillPhoto> waybillPhotoList = Lists.newArrayList();
        waybillPhotoService.deleteByWaybillId(qo.getWaybillId(), qo.getType());
        if (CollectionUtils.isNotEmpty(photoList)) {
            photoList.forEach(photoInfo -> {
                TWaybillPhoto waybillPhoto = new TWaybillPhoto();
                waybillPhoto.setPkId(UUIDUtils.timeBasedStr());
                waybillPhoto.setCreateTime(date);
                waybillPhoto.setLatitude(photoInfo.getLatitude());
                waybillPhoto.setWaybillLocal(photoInfo.getLocal());
                waybillPhoto.setLongitude(photoInfo.getLongitude());
                waybillPhoto.setPhoto(photoInfo.getPhoto());
                waybillPhoto.setWaybillType(type);
                waybillPhoto.setWaybillId(qo.getWaybillId());
                waybillPhotoList.add(waybillPhoto);
            });
        }
        if (CollectionUtils.isNotEmpty(waybillPhotoList)) {
            waybillPhotoService.insertBatch(waybillPhotoList);
        }
        log.info("WaybillInfo add success");
    }

}
