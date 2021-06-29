package com.mycompany.libengine;

import Model.LendingByUser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.PageSize;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import static com.itextpdf.kernel.pdf.PdfName.Image;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.element.Cell; 
import com.itextpdf.layout.element.Table;  


import  java.util.Random;
public class GeneratePDF {

    public static Alert a = new Alert(Alert.AlertType.WARNING);

    public static void generate(ArrayList<LendingByUser> penalizedStudsForPDF) {

        // get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        LocalDateTime now = LocalDateTime.now();
        String currentDated = dtf.format(now);
        Random rand = new Random();
        String DEST = "C:/Users/X M G/Desktop/Communique/communique_" +  rand.nextInt(45)+".pdf";
        PdfDocument pdf;
        try {
            pdf = new PdfDocument(new PdfWriter(DEST));
            Document document = new Document(pdf);

            //add the university logo
            ImageData uieccLogo;
            try {
                uieccLogo = ImageDataFactory.create("target/classes/images/4x/uiecc.jpg");
                //Scale to new height and new width of image
                // uieccLogo.setHeight(54 + 10, 48 + 10);
                // uieccLogo.setHeight(70);
                // uieccLogo.setWidth(60);
                float x = (PageSize.A4.getWidth() - uieccLogo.getWidth()) / 2;
                float y = (PageSize.A4.getHeight() - uieccLogo.getHeight()) / 2;
               
                //uieccLogo.s
                Image image = new Image(uieccLogo);
                image.setFixedPosition(x,y+350); 
                image.setAutoScale(false);
                document.add(image);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Paragraph paragraph1 = new Paragraph("REPUBLIQUE DU CAMEROUN");
           // paragraph1.setBold();
            paragraph1.setFontSize(11);
            paragraph1.setMarginBottom(0);
            paragraph1.setMaxWidth(200);
            paragraph1.setTextAlignment(TextAlignment.CENTER);

            Paragraph paragraph2 = new Paragraph("Paix - Travail - Patrie\n ------------------------------");
            paragraph2.setMarginTop(0);
            paragraph2.setFontSize(10);
            paragraph2.setMaxWidth(200);
            paragraph2.setTextAlignment(TextAlignment.CENTER);

            Paragraph paragraph3 = new Paragraph("UNIVERSITE INTER-ETATS CAMEROUN CONGO");
           // paragraph3.setBold();
            paragraph3.setFontSize(11);
            paragraph3.setMarginTop(0);
            paragraph3.setMarginBottom(0);
            paragraph3.setMaxWidth(200);
            paragraph3.setTextAlignment(TextAlignment.CENTER);
           

            // English version
            Paragraph paragraph4 = new Paragraph("REPUBLIC OF CAMEROON");
            // paragraph1.setBold();
             paragraph4.setFontSize(11);
             paragraph4.setMarginBottom(0);
             paragraph4.setMaxWidth(200);
             paragraph4.setTextAlignment(TextAlignment.CENTER);
             paragraph4.setFixedPosition(360, 790, 200);
            
 
             Paragraph paragraph5 = new Paragraph("Peace - Work - Fatherland\n ------------------------------");
             paragraph5.setMarginTop(0);
             paragraph5.setFontSize(10);
             paragraph5.setMaxWidth(200);
             paragraph5.setTextAlignment(TextAlignment.CENTER);
             paragraph5.setFixedPosition(360, 760, 200);
           
 
             Paragraph paragraph6 = new Paragraph("UNIVERSITY INTER-STATE\n CONGO CAMEROON");
            // paragraph3.setBold();
             paragraph6.setFontSize(11);
             paragraph6.setMarginTop(0);
             paragraph6.setMarginBottom(0);
             paragraph6.setMaxWidth(200);
             paragraph6.setTextAlignment(TextAlignment.CENTER);
             paragraph6.setFixedPosition(360, 720, 200);

             //introductionMessage
             Paragraph introMessage = new Paragraph("Les etudiants dont les noms suivent doivent se presenter a la bibliotheque pour la remise des documents qu'ils detiennent car les delais de remise ont ete strictement depasse. aucune abscence ne sera tolere. il s'agit de");
             introMessage.setFixedLeading(14);
             introMessage.setMarginTop(30);
             paragraph6.setMarginBottom(40);
             introMessage.setFirstLineIndent(15);
             introMessage.setSpacingRatio(45);

             //tInserting the table
             // Creating a table       
            float [] pointColumnWidths = {200f, 200F, 200F};   
            Table table = new Table(pointColumnWidths); 
            
             // Adding header to  to the table     

            Cell cell = new Cell().add(new Paragraph("Noms et prenoms")
                    .setPaddingLeft(10)
                    .setPaddingTop(5)
                    .setPaddingBottom(5)
                    .setMultipliedLeading(0.5f))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setBackgroundColor(WebColors.getRGBColor("#ddd"))
                    .setVerticalAlignment(VerticalAlignment.TOP);

                    Cell cell1 = new Cell().add(new Paragraph("Filiere")
                    .setPaddingLeft(10)
                    .setPaddingTop(5)
                    .setPaddingBottom(5)
                    .setMultipliedLeading(0.5f))
                    .setBackgroundColor(WebColors.getRGBColor("#ddd"))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP);
             
                    Cell cell2 = new Cell().add(new Paragraph("Nombre de documents")
                    .setPaddingLeft(10)
                    .setPaddingTop(5)
                    .setPaddingBottom(5)
                    .setMultipliedLeading(0.5f))
                    .setBackgroundColor(WebColors.getRGBColor("#ddd"))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP);

                    table.addCell(cell);
                    table.addCell(cell1);
                    table.addCell(cell2);

            //loading the data

            for( int i = 0 ; i < penalizedStudsForPDF.size() ;i++){
                table.addCell(new Cell().add(new Paragraph(penalizedStudsForPDF.get(i).student.fullname)
                .setPaddingLeft(10)
                .setPaddingTop(5)
                .setPaddingBottom(5)
                .setMultipliedLeading(1f))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP));

                table.addCell(new Cell().add(new Paragraph(penalizedStudsForPDF.get(i).student.depart)
                .setPaddingLeft(10)
                .setPaddingTop(5)
                .setPaddingBottom(5)
                .setMultipliedLeading(1f))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP));

                table.addCell(new Cell().add(new Paragraph(penalizedStudsForPDF.get(i).lendings.size()+" document" +(penalizedStudsForPDF.get(i).lendings.size() > 1 ? "s":""))
                .setPaddingLeft(10)
                .setPaddingTop(5)
                .setPaddingBottom(5)
                .setMultipliedLeading(1f))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP));
            }

            //Signature of the manager of the library
            Paragraph signature = new Paragraph("Responsable de la librarie \n Mme Nanfack Rene Solange");
            signature.setMaxWidth(200);
            signature.setMarginLeft(360);
            signature.setMarginTop(60);
            signature.setTextAlignment(TextAlignment.CENTER);

            //adding the date 
            Paragraph date = new Paragraph(currentDated);
            signature.setMaxWidth(200);
            signature.setMarginLeft(360);
            signature.setMarginTop(60);
            signature.setTextAlignment(TextAlignment.CENTER);

            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            
            //ENGLISH language
            document.add(paragraph4);
            document.add(paragraph5);
            document.add(paragraph6);

            //intro message
            document.add(introMessage);
            document.add(new Paragraph());

            //add the table
            document.add(table);
            document.add(new Paragraph());

            //add signature
            document.add(signature);

            //add date
            document.add(date);

            document.close();

            // Confirmation message
            // set alert type
            // Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            //  stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("images/4x/AppIcon.png")));
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("Convocation generated successusfully, check the convacation folder in your desktop!");
            a.show();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
