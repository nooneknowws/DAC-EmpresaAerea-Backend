package br.ufpr.dac.reserva;

import br.ufpr.dac.reserva.dto.ReservaDTO;
import br.ufpr.dac.reserva.model.Reserva;
import br.ufpr.dac.reserva.model.StatusReserva;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody ReservaDTO reservaDTO) {
        Reserva reserva = reservaService.criarReserva(reservaDTO);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> consultarReserva(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.consultarReserva(id);
        return reserva.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<Reserva> realizarCheckIn(@PathVariable Long id) {
        Reserva reserva = reservaService.realizarCheckIn(id);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reserva>> listarReservasPorCliente(@PathVariable Long clienteId) {
        List<Reserva> reservas = reservaService.listarReservasPorCliente(clienteId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/voo/{vooId}")
    public ResponseEntity<List<Reserva>> listarReservasPorVoo(@PathVariable Long vooId) {
        List<Reserva> reservas = reservaService.listarReservasPorVoo(vooId);
        return ResponseEntity.ok(reservas);
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmarReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.atualizarStatusReserva(id, StatusReserva.CONFIRMADO);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Reserva> finalizarReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.atualizarStatusReserva(id, StatusReserva.FINALIZADO);
        return ResponseEntity.ok(reserva);
    }
}
