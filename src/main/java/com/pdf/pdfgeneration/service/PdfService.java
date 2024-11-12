package com.pdf.pdfgeneration.service;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.pdf.pdfgeneration.models.Invoice;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class PdfService {

    public ByteArrayOutputStream generatePdf(Invoice invoice) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        document.setFont(PdfFontFactory.createFont(StandardFonts.COURIER_BOLD));
        float[] pointColumnWidths = { 300,300 };
        Table table = new Table(pointColumnWidths);
        String sellerInfo = "Seller:\n" + invoice.getSeller()+"\n"+invoice.getSellerAddress()+"\nGSTIN: "+invoice.getSellerGstin();
        String buyerInfo = "Buyer:\n" + invoice.getBuyer()+"\n"+invoice.getBuyerAddress()+"\nGSTIN: "+invoice.getBuyerGstin();
        table.addCell(new Cell().add(new Paragraph(sellerInfo)).setPadding(30));
        table.addCell(new Cell().add(new Paragraph(buyerInfo)).setPadding(30));
        float[] productWidth = { 150, 150, 150, 150 };
        Table productTable = new Table(productWidth);
        productTable.setTextAlignment(TextAlignment.CENTER);
        productTable.addCell(new Cell().add(new Paragraph("Item")));
        productTable.addCell(new Cell().add(new Paragraph("Quantity")));
        productTable.addCell(new Cell().add(new Paragraph("Rate")));
        productTable.addCell(new Cell().add(new Paragraph("Amount")));
        invoice.getItems().forEach(item -> {
            productTable.addCell(new Cell().add(new Paragraph(item.getName())));
            productTable.addCell(new Cell().add(new Paragraph(item.getQuantity())));
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getRate()))));
            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getAmount()))));
        });

        document.add(table);
        document.add(productTable);
        pdfWriter.close();
        pdfDocument.close();
        document.close();


        return outputStream;
    }

}
