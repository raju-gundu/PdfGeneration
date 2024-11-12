package com.pdf.pdfgeneration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.pdf.pdfgeneration.controller.PdfController;
import com.pdf.pdfgeneration.models.Invoice;
import com.pdf.pdfgeneration.models.Item;
import com.pdf.pdfgeneration.service.PdfService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PdfControllerTest {
    @Mock
    private PdfService pdfService;

    @InjectMocks
    PdfController pdfController;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice();
        invoice = new Invoice();
        invoice.setSeller("XYZ Pvt. Ltd.");
        invoice.setSellerGstin("29AABBCCDD121ZD");
        invoice.setSellerAddress("New Delhi, India");
        invoice.setBuyer("Vedant Computers");
        invoice.setBuyerGstin("29AABBCCDD131ZD");
        invoice.setBuyerAddress("New Delhi, India");

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setName("Product 1");
        item.setQuantity("12 Nos");
        item.setRate(123.00);
        item.setAmount(1476.00);
        items.add(item);
        invoice.setItems(items);
    }

    @Test
    void testGeneratePdf() throws Exception {
        Mockito.when(pdfService.generatePdf(invoice)).thenReturn(new ByteArrayOutputStream().assignBytes(new ObjectMapper().writeValueAsBytes(invoice)));
        ResponseEntity<byte[]> response = pdfController.generatePdf(invoice);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_PDF);
        Assertions.assertEquals(response.getHeaders().getContentDisposition().getType(), "attachment");
        Assertions.assertEquals(response.getHeaders().getContentDisposition().getFilename(), "invoice.pdf");
        byte[] responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertTrue(responseBody.length > 0);
    }
}
