package br.ufpr.dac.voos.rest;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import br.ufpr.dac.voos.models.Voo;
import br.ufpr.dac.voos.models.Aeroporto;

@CrossOrigin
@RestController
public class VooRest {
    public static final List<Voo> voos = new ArrayList<>();
    
    @GetMapping("/voos")
    public List<Voo> getVoos() {
        return voos;
    }

    static {
        voos.add(new Voo("1", "Voo 1", LocalDateTime.now(), new Aeroporto("1", "Aeroporto 1", "Cidade 1", "Estado 1", "Pais 1"), new Aeroporto("2", "Aeroporto 2", "Cidade 2", "Estado 2", "Pais 2"), 100.0, 100, 0, "Ativo"));
        voos.add(new Voo("2", "Voo 2", LocalDateTime.now(), new Aeroporto("3", "Aeroporto 3", "Cidade 3", "Estado 3", "Pais 3"), new Aeroporto("4", "Aeroporto 4", "Cidade 4", "Estado 4", "Pais 4"), 100.0, 100, 0, "Ativo"));
        voos.add(new Voo("3", "Voo 3", LocalDateTime.now(), new Aeroporto("5", "Aeroporto 5", "Cidade 5", "Estado 5", "Pais 5"), new Aeroporto("6", "Aeroporto 6", "Cidade 6", "Estado 6", "Pais 6"), 100.0, 100, 0, "Ativo"));
    }


}
