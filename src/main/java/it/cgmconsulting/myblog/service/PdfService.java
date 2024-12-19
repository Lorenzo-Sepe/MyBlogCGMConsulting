package it.cgmconsulting.myblog.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PostRepository postRepository;
    public InputStream createPdf(int postId, String imagePath) throws MalformedURLException {

        Post post = postRepository.getPost(postId, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        String title = post.getTitle();
        String postImage = post.getImage();
        String content = post.getContent();
        String author = post.getUser().getUsername();
        String publicationDate = post.getPublishedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // codice per creazione pdf

        // creazione pdf vuoto con metadati e impostazioni di pagina
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        addMetaData(pdf, title, author);
        Document document = new Document(pdf, PageSize.A4); // Il formato A4 in termini tipografici corrisponde a circa 595.0 punti in larghezza e 842.0 punti in altezza

        // TITLE
        Paragraph pTitle = new Paragraph(title)
                .simulateBold()
                .setFontSize(20)
                .setFontColor((new DeviceRgb(100,149,237)), 100);
        document.add(pTitle);
        addEmptyLines(document, 1);

        // IMAGE
        if(postImage != null) {
            ImageData imageData = ImageDataFactory.create(imagePath + postImage);
            document.add(new Image(imageData));
            addEmptyLines(document, 1);
        }

        // CONTENT
        Paragraph pContent = new Paragraph(content).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(pContent);
        addEmptyLines(document, 1);

        // UPDATED AT
        Paragraph pUpdtatedAt = new Paragraph("Pubblicato il: "+publicationDate).simulateItalic().setTextAlignment(TextAlignment.RIGHT);
        document.add(pUpdtatedAt);

        // AUTHOR
        Paragraph pAuthor = new Paragraph("Autore: "+author).simulateItalic().setTextAlignment(TextAlignment.RIGHT);
        document.add(pAuthor);

        // NUMERI DI PAGINA (bottom/right)
        int numberOfPages = pdf.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            document.showTextAligned(new Paragraph(String.format("page %s of %s", i, numberOfPages)).setFontSize(8),
                    560, 20, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
        }

        // CHIUSURA DOCUMENTO
        document.close();

        InputStream in = new ByteArrayInputStream(out.toByteArray());
        return in;

    }

    private static void addEmptyLines(Document document, int number) {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph("\n"));
        }
    }

    private void addMetaData(PdfDocument pdf, String title, String author) {
        PdfDocumentInfo info = pdf.getDocumentInfo();
        info.setTitle(title);
        info.setAuthor(author);
    }
}
