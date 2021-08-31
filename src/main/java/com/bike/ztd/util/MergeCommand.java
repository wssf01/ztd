package com.bike.ztd.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiCellData;
import org.jxls.transform.poi.PoiTransformer;


@EqualsAndHashCode(callSuper = true)
@Data
public class MergeCommand extends AbstractCommand {
    private String cols;    //合并的列数
    private String rows;    //合并的行数
    private Area area;
    private CellStyle cellStyle;    //第一个单元格的样式

    @Override
    public String getName() {
        return "merge";
    }

    @Override
    public Command addArea(Area area) {
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'merge' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        int rows = 1, cols = 1;
        if(StringUtils.isNotBlank(this.rows)){
            Object rowsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.rows, context.toMap());
            if(rowsObj != null && NumberUtils.isDigits(rowsObj.toString())){
                rows = NumberUtils.toInt(rowsObj.toString());
            }
        }
        if(StringUtils.isNotBlank(this.cols)){
            Object colsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.cols, context.toMap());
            if(colsObj != null && NumberUtils.isDigits(colsObj.toString())){
                cols = NumberUtils.toInt(colsObj.toString());
            }
        }

        if (rows > 1 || cols > 1) {
            Transformer transformer = this.getTransformer();
            return poiMerge(cellRef, context, (PoiTransformer) transformer, rows, cols);
        }
        area.applyAt(cellRef, context);
        return new Size(1, 1);
    }

    private Size poiMerge(CellRef cellRef, Context context, PoiTransformer transformer, int rows, int cols){
        Sheet sheet = transformer.getWorkbook().getSheet(cellRef.getSheetName());
        CellRangeAddress region = new CellRangeAddress(
                cellRef.getRow(),
                cellRef.getRow() + rows - 1,
                cellRef.getCol(),
                cellRef.getCol() + cols - 1);
        sheet.addMergedRegion(region);

        //合并之后单元格样式会丢失，以下操作将合并后的单元格恢复成合并前第一个单元格的样式
        area.applyAt(cellRef, context);
        if(cellStyle == null){
            PoiCellData cellData = (PoiCellData)transformer.getCellData(cellRef);
            if(cellData != null) {
                cellStyle = cellData.getCellStyle();
            }
        }
        setRegionStyle(cellStyle, region, sheet);
        return new Size(cols, rows);
    }

    private static void setRegionStyle(CellStyle cs, CellRangeAddress region, Sheet sheet) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                if (cs == null) {
                    cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                    cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                    cell.getCellStyle().setBorderBottom(BorderStyle.THIN);
                    cell.getCellStyle().setBorderLeft(BorderStyle.THIN);
                    cell.getCellStyle().setBorderRight(BorderStyle.THIN);
                    cell.getCellStyle().setBorderTop(BorderStyle.THIN);
                    cell.getCellStyle().setBottomBorderColor(IndexedColors.BLACK.index);
                    cell.getCellStyle().setLeftBorderColor(IndexedColors.BLACK.index);
                    cell.getCellStyle().setRightBorderColor(IndexedColors.BLACK.index);
                    cell.getCellStyle().setTopBorderColor(IndexedColors.BLACK.index);
                } else {
                    cell.setCellStyle(cs);
                }
                cell.setCellStyle(cs);
            }
        }
    }
}
