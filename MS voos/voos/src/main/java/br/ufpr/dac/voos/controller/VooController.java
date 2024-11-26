package br.ufpr.dac.voos.controller;

import br.ufpr.dac.voos.dto.VooDTO;
import br.ufpr.dac.voos.models.Voo;
import br.ufpr.dac.voos.repository.VooRepository;
import br.ufpr.dac.voos.services.VooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voos")
public class VooController {
    
        @Autowired
        private VooService vooService;

        @Autowired
        private VooRepository VooRepository;
    
        @GetMapping
        public List<VooDTO> listarTodosVoos() {
            List<Voo> voos = VooRepository.findAllWithOrigemAndDestino();

            return voos.stream().map(VooDTO::new).toList();
        }
    
        @GetMapping("/{id}")
        public ResponseEntity<VooDTO> listarUmVoo(@PathVariable("id") Long id) {
            return VooRepository.findByIdWithOrigemAndDestino(id)
                    .map(voo -> ResponseEntity.ok(new VooDTO(voo)))
                    .orElse(ResponseEntity.notFound().build());
        }
    
        @PostMapping
        public Voo inserirVoo(@RequestBody Voo voo) {
            return vooService.inserirVoo(voo);
        }
    
        @PutMapping("/{id}")
        public Voo editarVoo(@PathVariable("id") Long id, @RequestBody Voo voo) {
            return vooService.editarVoo(id, voo);
        }
    
        @DeleteMapping("/{id}")
        public void deletarVoo(@PathVariable("id") Long id) {
            vooService.deletarVoo(id);
        }
    
}
