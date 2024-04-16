package com.hotel.common.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * pdf工具类
 *
 * @author: 郭晨旭
 * @create: 2024-04-16 15:56
 * @version: 1.0
 */
@Slf4j
public class PDFUtil {
    private static BaseFont baseFont;

    static {
        try {
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.error("创建字体失败: ", e);
        }
    }

    private static final Font FONT = new Font(baseFont, 12);

    public static void addRow(PdfPTable table, String key, String value) {
        table.addCell(new Phrase(key, FONT));
        table.addCell(new Phrase(value, FONT));
    }
}
