package com.example.qrcodearticleapp.controller;

import com.example.qrcodearticleapp.entity.Fabricant;
import com.example.qrcodearticleapp.service.FabricantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fabricants")
public class FabricantController {

    @Autowired
    private FabricantService fabricantService;

    @GetMapping
    public List<Fabricant> getAllFabricants() {
        return fabricantService.getAllFabricants();
    }

    @GetMapping("/{id}")
    public Fabricant getFabricantById(@PathVariable Long id) {
        return fabricantService.getFabricantById(id);
    }

    @PostMapping
    public Fabricant saveFabricant(@RequestBody Fabricant fabricant) {
        System.out.println("Fabricant=="+fabricant);
        return fabricantService.saveFabricant(fabricant);
    }

    @PutMapping("/{id}")
    public Fabricant updateFabricant(@PathVariable Long id, @RequestBody Fabricant fabricant) {
        fabricant.setId(id);
        return fabricantService.saveFabricant(fabricant);
    }

    @DeleteMapping("/{id}")
    public void deleteFabricant(@PathVariable Long id) {
        fabricantService.deleteFabricant(id);
    }
}
