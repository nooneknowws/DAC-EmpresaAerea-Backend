package br.ufpr.dac.MSReserva.events;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.ufpr.dac.MSReserva.config.rabbitmq.RabbitMQConfig;
import br.ufpr.dac.MSReserva.cqrs.query.ReservaQueryRepository;
import br.ufpr.dac.MSReserva.model.Reserva;
import br.ufpr.dac.MSReserva.model.StatusReserva;

@Component
public class ReservaEventListener {
    
    @Autowired
    private ReservaQueryRepository reservaQueryRepository;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleReservaEvent(ReservaEvent event) {


        
        switch (event.getTipo()) {
            case CREATED:
            	
            case UPDATED:
                Reserva reserva = new Reserva();
                reserva.setId(event.getId());
                reserva.setDataHora(event.getDataHora());
                reserva.setAeroportoOrigemCod(event.getAeroportoOrigemCod());
                reserva.setAeroportoDestinoCod(event.getAeroportoDestinoCod());
                reserva.setValor(event.getValor());
                reserva.setMilhas(event.getMilhas());
                reserva.setStatus(StatusReserva.valueOf(event.getStatus()));
                reserva.setVooId(event.getVooId());
                reserva.setClienteId(event.getClienteId());
                reserva.setCodigoReserva(event.getCodigoReserva());
                reserva.setQuantidade(event.getQuantidade());
                reserva.setCodigoVoo(event.getCodigoVoo());

                
                reservaQueryRepository.save(reserva);
                break;
                
            case DELETED:
                reservaQueryRepository.deleteById(event.getId());
                break;
        }
    }
    public enum EventType {
        CREATED, UPDATED, DELETED
    }
}