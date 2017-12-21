package com.jiarui.znxj.utils;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jiarui.znxj.bean.TaskPreviewData;
import com.jiarui.znxj.pdfhtml.MyFont;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 建立pdf的文件 2017/11/17 0017.
 */

public class CretePDFUtils {

    List<TaskPreviewData> previewDatalist;//任务数据
    String reservoirName;//水库名称
    String reservoirID;//水库id
    String tasktype;//任务类型
    private String water_line;//'水位'
    private String weather;//'天气'
    private String down_water;//'下游水位'
    private String inspection;//'巡检人员'
    private String creator;//''校验人员

    public CretePDFUtils(List<TaskPreviewData> previewDatalist, String reservoirName, String reservoirID, String tasktype, String water_line, String weather, String down_water, String inspection, String creator) {
        this.previewDatalist = previewDatalist;
        this.reservoirName = reservoirName;
        this.reservoirID = reservoirID;
        this.tasktype = tasktype;
        this.water_line = water_line;
        this.weather = weather;
        this.down_water = down_water;
        this.inspection = inspection;
        this.creator = creator;
    }

    /**
     * 创建项目模型
     */
    public String  intbdData( ) {
//        pdf命名：水库码+任务号+完成时间+文件类型
        if (StringUtil.isListNotEmpty(previewDatalist)){
        String pdfFileName=reservoirID+ previewDatalist.get(0).getTaskid()+getEndFileName(true)+".pdf";
        // 1:建立Document对象实例
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        FileOutputStream fos;
        try {
            Log.e("intbdData","intbdDataintbdDataintbdData");
            fos = new FileOutputStream(new File(
                    Environment.getExternalStorageDirectory()
                            + "/"+pdfFileName));
            // 2:建立一个PDF 写入器与document对象关联通过书写器(Writer)可以将文档写入到磁盘中
            PdfWriter.getInstance(document, fos);

            // 3:打开文档
            document.open();

            // 解决中文不显示问题
//            第一种（报错并不认可，因为我这没有iTextAsian.jar包）
//            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
//                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            第二种（报错并不认可，路径搞错也是无法找到的）
//            BaseFont bfChinese = BaseFont.createFont("/C:/windows/fonts/simsun.ttc,1",
//                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);//
            //第三种（根据之前巡检的pdf生成自行创建一个字体）
            Font fontChina18 = new MyFont().getFont("my_font", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED, 18, 0, BaseColor.BLACK);
            Font fontChina14 = new MyFont().getFont("my_font", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED, 14, 0, BaseColor.BLACK);

            // 4:向文档添加内容
            // 标题
            Paragraph titleParagraph = new Paragraph(reservoirName, fontChina18);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);// 居中
            document.add(titleParagraph);

            // 空格换行
            Paragraph blank1 = new Paragraph("\u3000");
            document.add(blank1);

            // 检查类别+检查类别+检查日期
            StringBuilder sb1 = new StringBuilder();
            sb1.append("检查类别：");
            sb1.append(tasktype);
            sb1.append("\u3000\u3000\u3000");
            sb1.append("检查日期：");
            sb1.append(getEndFileName(false));
            Paragraph pE = new Paragraph(sb1.toString(), fontChina14);
            pE.setAlignment(Element.ALIGN_CENTER);
            document.add(pE);

            // 库水位+下游水位+天气
            document.add(blank1);
            StringBuilder sbW = new StringBuilder();
            sbW.append("库水位：");
            sbW.append(water_line);
            sbW.append("m");
            sbW.append("\u3000\u3000\u3000");
            sbW.append("下游水位：");
            sbW.append(down_water);
            sbW.append("m");
            sbW.append("\u3000\u3000\u3000");
            sbW.append("天气：");
            sbW.append(weather);
            Paragraph pW = new Paragraph(sbW.toString(), fontChina14);
            pW.setAlignment(Element.ALIGN_CENTER);
            document.add(pW);

            
            // 表格处理
            document.add(blank1);
            PdfPTable table = new PdfPTable(11);// 11列
            table.setWidthPercentage(100);// 表格宽度为100%
            /*第一栏标题栏*/
            PdfPCell cell8 = new PdfPCell();
            cell8.setBorderWidth(1);
            cell8.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell8.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell8.setPhrase(new Paragraph("点位", fontChina14));
            table.addCell(cell8);

            PdfPCell cell9 = new PdfPCell();
            cell9.setBorderWidth(1);
            cell9.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell8.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell9.setPhrase(new Paragraph("检查部位", fontChina14));
            table.addCell(cell9);

            PdfPCell celc = new PdfPCell();
            celc.setBorderWidth(1);
            celc.setColspan(2);
            celc.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            celc.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
            celc.setPhrase(new Paragraph("检查项", fontChina14));
            table.addCell(celc);

            PdfPCell celcontent = new PdfPCell();
            celcontent.setBorderWidth(1);
            celcontent.setColspan(3);
            celcontent.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            celcontent.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
            celcontent.setPhrase(new Paragraph("检查内容", fontChina14));
            table.addCell(celcontent);

            PdfPCell celsituation = new PdfPCell();
            celsituation.setBorderWidth(1);
            celsituation.setColspan(4);
            celsituation.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            celsituation.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
            celsituation.setPhrase(new Paragraph("检查情况记录", fontChina14));
            table.addCell(celsituation);
            for (int i = 0; i <previewDatalist.size() ; i++) {
                // Row1
                PdfPCell cell14 = new PdfPCell();
                cell14.setBorderWidth(1);
                cell14.setRowspan(previewDatalist.get(i).getDrowpan());
                cell14.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell14.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell14.setPhrase(new Paragraph(previewDatalist.get(i).getName(), fontChina14));
                table.addCell(cell14);
                for (int j = 0; j < previewDatalist.get(i).getItems().size(); j++) {
                    PdfPCell cell15 = new PdfPCell();
                    cell15.setBorderWidth(1);
                    cell15.setRowspan(previewDatalist.get(i).getItems().get(j).getBrowpan());
                    cell15.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell15.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell15.setPhrase(new Paragraph(previewDatalist.get(i).getItems().get(j).getName(), fontChina14));
                    table.addCell(cell15);
                    for (int k = 0; k < previewDatalist.get(i).getItems().get(j).getChild().size(); k++){
                        PdfPCell cell16 = new PdfPCell();
                        cell16.setBorderWidth(1);
                        cell16.setColspan(2);
                        cell16.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        cell16.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                        cell16.setPhrase(new Paragraph(previewDatalist.get(i).getItems().get(j).getChild().get(k).getName(), fontChina14));
                        table.addCell(cell16);

                        PdfPCell cell17 = new PdfPCell();
                        cell17.setBorderWidth(1);
                        cell17.setColspan(3);
                        cell17.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        cell17.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                        cell17.setPhrase(new Paragraph(previewDatalist.get(i).getItems().get(j).getChild().get(k).getContent(), fontChina14));
                        table.addCell(cell17);

                        PdfPCell cell18 = new PdfPCell();
                        cell18.setBorderWidth(1);
                        cell18.setColspan(4);
                        cell18.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        cell18.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                        cell18.setPhrase(new Paragraph(previewDatalist.get(i).getItems().get(j).getChild().get(k).getResults(), fontChina14));
                        table.addCell(cell18);
                    }
                }
            }
            document.add(table);

            // 巡查人员签名+校验人员+负责人员
            document.add(blank1);
            StringBuilder sbP = new StringBuilder();
            sbP.append("巡查人员签名：");
            sbP.append(inspection);
            sbP.append("\u3000\u3000\u3000");
            sbP.append("校验人员：");
            sbP.append(creator);
            sbP.append("\u3000\u3000\u3000");
            sbP.append("负责人员：");
            sbP.append(inspection);
            Paragraph sP = new Paragraph(sbP.toString(), fontChina14);
            sP.setAlignment(Element.ALIGN_CENTER);
            document.add(sP);
            // 5:关闭文档
            document.close();
            fos.flush();
            fos.close();
            return pdfFileName;
        } catch (FileNotFoundException e) {
            Log.e("FileNotintbdData",e.toString());
            e.printStackTrace();
            return "failure";
        } catch (DocumentException e) {
            Log.e("DocumentintbdData",e.toString());
            e.printStackTrace();
            return "failure";
        } catch (IOException e) {
            Log.e("IOintbdData",e.toString());
            e.printStackTrace();
            return "failure";
        }
         }
        return "failure";
    }
    /**
     * 使用系统当前日期作为结束时间2017.11.1512.10.50
     */
    private String getEndFileName(boolean isdate) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat;
        if (isdate==true){
            dateFormat = new SimpleDateFormat(
                    "yyyyMMddHHmmss");
        }else{
            dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH-mm-ss");
        }
        return dateFormat.format(date);
    }


    public List<TaskPreviewData> getPreviewDatalist() {
        return previewDatalist;
    }

    public void setPreviewDatalist(List<TaskPreviewData> previewDatalist) {
        this.previewDatalist = previewDatalist;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getReservoirID() {
        return reservoirID;
    }

    public void setReservoirID(String reservoirID) {
        this.reservoirID = reservoirID;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getWater_line() {
        return water_line;
    }

    public void setWater_line(String water_line) {
        this.water_line = water_line;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDown_water() {
        return down_water;
    }

    public void setDown_water(String down_water) {
        this.down_water = down_water;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
