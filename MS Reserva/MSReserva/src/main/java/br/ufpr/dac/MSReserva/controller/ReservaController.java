package br.ufpr.dac.MSReserva.controller;

import br.ufpr.dac.MSReserva.cqrs.command.ReservaCommandService;
import br.ufpr.dac.MSReserva.cqrs.query.ReservaQueryService;
import br.ufpr.dac.MSReserva.dto.CriarReservaDTO;
import br.ufpr.dac.MSReserva.dto.ReservaDTO;
import br.ufpr.dac.MSReserva.model.Reserva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaCommandService commandService;

    @Autowired
    private ReservaQueryService queryService;

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody CriarReservaDTO reservaDTO) {
        Reserva reserva = commandService.criarReserva(reservaDTO);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorCliente(
        @PathVariable("clienteId") Long clienteId) {
        System.out.println("Controller: Received request for clientId: " + clienteId);
        List<ReservaDTO> reservas = queryService.listarReservasPorCliente(clienteId);
        System.out.println("Controller: Returning " + reservas.size() + " reservas");
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/voo/{vooId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorVoo(
        @PathVariable("vooId") Long vooId) {
        List<ReservaDTO> reservas = queryService.listarReservasPorVoo(vooId);
        return ResponseEntity.ok(reservas);
    }
}