package br.ufpr.dac.MSClientes.rest;

import br.ufpr.dac.MSClientes.models.Milhas;
import br.ufpr.dac.MSClientes.models.Usuario;
import br.ufpr.dac.MSClientes.models.dto.MilhasDTO;
import br.ufpr.dac.MSClientes.repository.ClienteRepo;
import br.ufpr.dac.MSClientes.repository.MilhasRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/milhas")
public class MilhasRest {
	
	@Autowired
    private MilhasRepository milhasRepository;
	@Autowired
    private ClienteRepo usuarioRepository;

    @PostMapping
    public ResponseEntity<?> processarMilhas(@RequestBody MilhasDTO request) {
        Usuario cliente = usuarioRepository.findById(request.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (request.getEntradaSaida().equals("SAIDA") && cliente.getSaldoMilhas() < request.getQuantidade()) {
            return ResponseEntity.badRequest().body("Saldo insuficiente");
        }

        Milhas milhas = new Milhas();
        milhas.setCliente(cliente);
        milhas.setQuantidade(request.getQuantidade());
        milhas.setEntradaSaida(request.getEntradaSaida());
        milhas.setValorEmReais(request.getValorEmReais());
        milhas.setDescricao(request.getDescricao());
        milhas.setDataHoraTransacao(LocalDateTime.now());
        milhas.setReservaId(request.getReservaId());

        if (request.getEntradaSaida().equals("ENTRADA")) {
            cliente.setSaldoMilhas(cliente.getSaldoMilhas() + request.getQuantidade());
        } else if (request.getEntradaSaida().equals("SAIDA")) {
            cliente.setSaldoMilhas(cliente.getSaldoMilhas() - request.getQuantidade());
        }

        usuarioRepository.save(cliente);
        return ResponseEntity.ok(milhasRepository.save(milhas));
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<Milhas>> getMilhasByCliente(@PathVariable("clienteId") Long clienteId) {
        return ResponseEntity.ok(milhasRepository.findByClienteId(clienteId));
    }

}