package br.ufpr.dac.voos.rest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.ufpr.dac.voos.models.Voo;
import br.ufpr.dac.voos.models.Aeroporto;
import org.springframework.web.bind.annotation.PutMapping;



@CrossOrigin
@RestController
public class VooRest {
    public static final List<Voo> listaVoos = new ArrayList<>();
    
    @GetMapping("/voos")
    public List<Voo> listarTodosVoos() {
        return listaVoos;
    }

    @GetMapping("/voos/{id}")
    public Voo listarUmVoo(@PathVariable("id") String id) {
        return listaVoos.stream().filter(v -> v.getId().equals(id)).findAny().orElse(null);
    }

    @PostMapping("/voos")
    public Voo inserirVoo(@RequestBody Voo vooParam) {
        
        Voo novoVoo = listaVoos.stream().max(Comparator.comparing(Voo::getId)).orElse(null);

        if (novoVoo == null) {
            vooParam.setId("1");
        } else {
            vooParam.setId(novoVoo.getId() + 1);
            listaVoos.add(vooParam);
        }
        
        return vooParam;
    }

    @PutMapping("voos/{id}")
    public Voo editarVoo(@PathVariable("id") String id, @RequestBody Voo vooParam) {
        Voo vooEditado = listaVoos.stream().filter(v -> v.getId().equals(id)).findAny().orElse(null);

        if (vooEditado != null) {
            vooEditado.setCodigoVoo(vooParam.getCodigoVoo());
            vooEditado.setDataHoraPartida(vooParam.getDataHoraPartida());
            vooEditado.setOrigem(vooParam.getOrigem());
            vooEditado.setDestino(vooParam.getDestino());
            vooEditado.setValorPassagem(vooParam.getValorPassagem());
            vooEditado.setQuantidadeAssentos(vooParam.getQuantidadeAssentos());
            vooEditado.setQuantidadePassageiros(vooParam.getQuantidadePassageiros());
            vooEditado.setStatus(vooParam.getStatus());
        }
        
        return vooEditado;
    }

    @DeleteMapping("/voos/{id}")
    public Voo removerVoo(@PathVariable("id") String id) {
        Voo vooRemovido = listaVoos.stream().filter(v -> v.getId().equals(id)).findAny().orElse(null);

        if (vooRemovido != null) {
            listaVoos.removeIf(v -> v.getId().equals(id));
        }
        
        return vooRemovido;
    }
    

    static {
        listaVoos.add(new Voo("1", "Voo 1", LocalDateTime.now(), new Aeroporto("1", "Aeroporto 1", "Cidade 1", "Estado 1", "Pais 1"), new Aeroporto("2", "Aeroporto 2", "Cidade 2", "Estado 2", "Pais 2"), 100.0, 100, 0, "Ativo"));
        listaVoos.add(new Voo("2", "Voo 2", LocalDateTime.now(), new Aeroporto("3", "Aeroporto 3", "Cidade 3", "Estado 3", "Pais 3"), new Aeroporto("4", "Aeroporto 4", "Cidade 4", "Estado 4", "Pais 4"), 100.0, 100, 0, "Ativo"));
        listaVoos.add(new Voo("3", "Voo 3", LocalDateTime.now(), new Aeroporto("5", "Aeroporto 5", "Cidade 5", "Estado 5", "Pais 5"), new Aeroporto("6", "Aeroporto 6", "Cidade 6", "Estado 6", "Pais 6"), 100.0, 100, 0, "Ativo"));
        //listaVoos.add(new Voo("4", "Voo 4", LocalDateTime.now(), new Aeroporto("7", "Aeroporto 7", "Cidade 7", "Estado 7", "Pais 7"), new Aeroporto("8", "Aeroporto 8", "Cidade 8", "Estado 8", "Pais 8"), 100.0, 100, 0, "Ativo"));
    }


}
