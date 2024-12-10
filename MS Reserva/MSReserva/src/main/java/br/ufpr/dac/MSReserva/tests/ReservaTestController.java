package br.ufpr.dac.MSReserva.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufpr.dac.MSReserva.model.Reserva;
import br.ufpr.dac.MSReserva.model.StatusReserva;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class ReservaTestController {
    
    @Autowired
    private ReservaTestService testService;
    
    @GetMapping("/repository-test")
    public ResponseEntity<String> testRepositories() {
        System.out.println("Starting repository test");
        
        try {
            // Create a test reservation
            Reserva reserva = new Reserva();
            reserva.setDataHora(LocalDateTime.now());
            reserva.setAeroportoOrigemCod("GRU");
            reserva.setAeroportoDestinoCod("CWB");
            reserva.setValor(369.0);
            reserva.setMilhas(0);
            reserva.setStatus(StatusReserva.PENDENTE);
            reserva.setVooId(8L);
            reserva.setClienteId(5L);
            
            testService.testRepositories();
            
            return ResponseEntity.ok("Test completed - check logs for results");
        } catch (Exception e) {
            System.out.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Test failed: " + e.getMessage());
        }
    }
}