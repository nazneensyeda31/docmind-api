package com.docmind.docmind_api.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class PdfExtractorService {

    public String extractText(MultipartFile file){
        if(file.getContentType() == null || !file.getContentType().equals(("application/pdf"))){
            throw new IllegalArgumentException("Only PDF files are allowed");
        }
        try (PDDocument document = Loader.loadPDF(file.getBytes())){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF: "+ e.getMessage());
        }


    }
}
