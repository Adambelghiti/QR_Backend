package com.example.qrcodearticleapp.controller;

import com.example.qrcodearticleapp.Dto.ArticleDTO;
import com.example.qrcodearticleapp.entity.Article;
import com.example.qrcodearticleapp.entity.Entrepot;
import com.example.qrcodearticleapp.entity.Fabricant;
import com.example.qrcodearticleapp.entity.Fournisseur;
import com.example.qrcodearticleapp.service.ArticleService;
import com.example.qrcodearticleapp.service.EntrepotService;
import com.example.qrcodearticleapp.service.FabricantService;
import com.example.qrcodearticleapp.service.FournisseurService;
import com.example.qrcodearticleapp.service.QRCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private EntrepotService entrepotService;

    @Autowired
    private FabricantService fabricantService;

    @Autowired
    private FournisseurService fournisseurService;

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article != null) {
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Article> saveArticle(@RequestBody ArticleDTO articleDTO) {
        // Logging for debugging
        System.out.println("Received DTO: " + articleDTO);

        String entrepotNom = articleDTO.getEntrepotNom();
        String fabricantName = articleDTO.getFabricantName();
        String fournisseurName = articleDTO.getFournisseurName();

        Entrepot entrepot = entrepotService.getEntrepotByName(entrepotNom);
        Fabricant fabricant = fabricantService.getFabricantByName(fabricantName);
        Fournisseur fournisseur = fournisseurService.getFournisseurByName(fournisseurName);

        // More logging
        System.out.println("Entrepot: " + entrepot);
        System.out.println("Entrepot: " + entrepot.getNom());
        System.out.println("Entrepot: " + entrepot.getLocation());

        System.out.println("Fabricant: " + fabricant);
        System.out.println("Fabricant: " + fabricant.getName());

        System.out.println("Fournisseur: " + fournisseur);
        System.out.println("Fournisseur: " + fournisseur.getName());


        if (entrepot == null || fabricant == null || fournisseur == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Creating and saving the article
        Article article = new Article();
        article.setNom(articleDTO.getNom());
        article.setLongueur(articleDTO.getLongueur());
        article.setLargeur(articleDTO.getLargeur());
        article.setHauteur(articleDTO.getHauteur());
        article.setCategorie(articleDTO.getCategorie());
        article.setEntrepot(entrepot);
        article.setFabricant(fabricant);
        article.setFournisseur(fournisseur);

        // Logging the Article object
        System.out.println("Article before saving: " + article);

        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(savedArticle);
    }




    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        String entrepotNom = articleDTO.getEntrepotNom();
        String fabricantName = articleDTO.getFabricantName();
        String fournisseurName = articleDTO.getFournisseurName();

        Entrepot entrepot = entrepotService.getEntrepotByName(entrepotNom);
        Fabricant fabricant = fabricantService.getFabricantByName(fabricantName);
        Fournisseur fournisseur = fournisseurService.getFournisseurByName(fournisseurName);

        if (entrepot == null || fabricant == null || fournisseur == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // or throw a custom exception
        }

        Article existingArticle = articleService.getArticleById(id);
        if (existingArticle == null) {
            return ResponseEntity.notFound().build(); // Handle the case where the article doesn't exist
        }

        // Update the article's details
        existingArticle.setNom(articleDTO.getNom());
        existingArticle.setLongueur(articleDTO.getLongueur());
        existingArticle.setLargeur(articleDTO.getLargeur());
        existingArticle.setHauteur(articleDTO.getHauteur());
        existingArticle.setCategorie(articleDTO.getCategorie());
        existingArticle.setEntrepot(entrepot);
        existingArticle.setFabricant(fabricant);
        existingArticle.setFournisseur(fournisseur);

        Article updatedArticle = articleService.saveArticle(existingArticle);
        return ResponseEntity.ok(updatedArticle);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadQRCode(@PathVariable Long id) throws IOException {
        Article article = articleService.getArticleById(id);
        if (article != null && article.getCodeQr() != null) {
            Path path = Paths.get(System.getProperty("user.home"), "Desktop", "Codes");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path filePath = path.resolve("QR_Code_" + id + ".png");
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(article.getCodeQr());
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(article.getCodeQr());
            InputStreamResource resource = new InputStreamResource(bis);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePath.getFileName().toString())
                    .contentType(MediaType.IMAGE_PNG)
                    .contentLength(article.getCodeQr().length)
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

    private byte[] generateQRCode(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
