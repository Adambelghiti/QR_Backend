package com.example.qrcodearticleapp.controller;

import com.example.qrcodearticleapp.entity.Entrepot;
import com.example.qrcodearticleapp.repository.EntrepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrepots")
public class EntrepotController {

    @Autowired
    private EntrepotRepository entrepotRepository;

    @GetMapping
    public List<Entrepot> getAllEntrepots() {
        return entrepotRepository.findAll();
    }

    @GetMapping("/{id}")
    public Entrepot getEntrepotById(@PathVariable Long id) {
        return entrepotRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Entrepot saveEntrepot(@RequestBody Entrepot entrepot) {
        return entrepotRepository.save(entrepot);
    }

    @PutMapping("/{id}")
    public Entrepot updateEntrepot(@PathVariable Long id, @RequestBody Entrepot entrepot) {
        if (!entrepotRepository.existsById(id)) {
            return null;
        }
        entrepot.setId(id);
        return entrepotRepository.save(entrepot);
    }

    @DeleteMapping("/{id}")
    public void deleteEntrepot(@PathVariable Long id) {
        if (entrepotRepository.existsById(id)) {
            entrepotRepository.deleteById(id);
        }
    }
}
