package com.bike.ztd.util;

import com.bike.ztd.exception.BusinessException;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JxlsUtils {

    static{
        //添加自定义指令
        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
    }

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) {
        Context context = new Context();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer  = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<>(1);
        funcs.put("utils", new JxlsUtils());    //添加自定义功能
        evaluator.getJexlEngine().setFunctions(funcs);
        try {
            jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null) {
                    is.close();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportExcel(InputStream is, File out, Map<String, Object> model) {
        try {
            exportExcel(is, new FileOutputStream(out), model);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException("Excel 模板未找到。");
        }
    }

    // 小数点格式化后两位
    public static String douRound2(Double v) {
        if (v == null) {
            return "";
        }
        try {
            return String.valueOf(PrecisionUtils.round(v, 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // 日期格式化(勿删,已用)
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // boolean型显示中文
    public static String boolFmt(Boolean flag) {
        if (flag == null) {
            return "";
        }
        return flag ? "是" : "否";
    }
}
