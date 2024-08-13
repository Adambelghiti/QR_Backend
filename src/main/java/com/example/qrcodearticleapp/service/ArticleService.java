package com.example.qrcodearticleapp.service;

import com.example.qrcodearticleapp.entity.Article;
import com.example.qrcodearticleapp.repository.ArticleRepository;
import com.example.qrcodearticleapp.repository.EntrepotRepository;
import com.example.qrcodearticleapp.repository.FabricantRepository;
import com.example.qrcodearticleapp.repository.FournisseurRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntrepotRepository entrepotRepository;

    @Autowired
    private FabricantRepository fabricantRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Transactional
    public Article saveArticle(Article article) {
        // Ensure related entities are saved or fetched from the database
        if (article.getEntrepot() != null) {
            article.setEntrepot(entrepotRepository.findById(article.getEntrepot().getId()).orElse(article.getEntrepot()));
        }
        if (article.getFabricant() != null) {
            article.setFabricant(fabricantRepository.findById(article.getFabricant().getId()).orElse(article.getFabricant()));
        }
        if (article.getFournisseur() != null) {
            article.setFournisseur(fournisseurRepository.findById(article.getFournisseur().getId()).orElse(article.getFournisseur()));
        }

        // Generate QR code with full information (remove serial number if not required)
        String qrContent = String.format("Name: %s, Length: %s, Width: %s, Height: %s, Category: %s, Warehouse: %s, Manufacturer: %s, Supplier: %s",
                article.getNom(),
                article.getLongueur(),
                article.getLargeur(),
                article.getHauteur(),
                article.getCategorie(),
                article.getEntrepot(),
                article.getFabricant(),
                article.getFournisseur()
        );
        article.setCodeQr(generateQRCode(qrContent));
        System.out.println("Article Details:");
        System.out.println("  Nom: " + article.getNom());
        System.out.println("  Longueur: " + article.getLongueur());
        System.out.println("  Largeur: " + article.getLargeur());
        System.out.println("  Hauteur: " + article.getHauteur());
        System.out.println("  Categorie: " + article.getCategorie());

        System.out.println("  Entrepot:");
        System.out.println("    Nom: " + article.getEntrepot().getNom());
        System.out.println("    Location: " + article.getEntrepot().getLocation());

        System.out.println("  Fabricant:");
        System.out.println("    Name: " + article.getFabricant().getName());

        System.out.println("  Fournisseur:");
        System.out.println("    Name: " + article.getFournisseur().getName());
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    private byte[] generateQRCode(String content) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return byteArrayOutputStream.toByteArray();
    }
}
