package br.ufpr.dac.voos.services;

import br.ufpr.dac.voos.models.Voo;
import br.ufpr.dac.voos.models.Aeroporto;
import br.ufpr.dac.voos.repository.VooRepository;
import br.ufpr.dac.voos.repository.AeroportoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VooService {
    
        @Autowired
        private VooRepository vooRepository;

        @Autowired
        private AeroportoRepository aeroportoRepository;


        // Get all
        public List<Voo> listarTodosVoos() {
            return vooRepository.findAllWithOrigemAndDestino();
        }
        
        // Get one
        public Voo listarUmVoo(Long id) {
            return vooRepository.findByIdWithOrigemAndDestino(id).orElse(null);         
        }
    
        // Post
        public Voo inserirVoo(Voo voo, String origemCodigo, String destinoCodigo) {
            Aeroporto origem = aeroportoRepository.findByCodigo(origemCodigo)
                .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado: " + origemCodigo));
        
        Aeroporto destino = aeroportoRepository.findByCodigo(destinoCodigo)
                .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado: " + destinoCodigo));
        
        voo.setOrigem(origem);
        voo.setDestino(destino);

        return vooRepository.save(voo);
        }
    
        // Put
        public Voo editarVoo(Long id, Voo voo) {
            Voo vooEditado = vooRepository.findById(id).orElse(null);
    
            if (vooEditado != null) {
                vooEditado.setDataHoraPartida(voo.getDataHoraPartida());
                vooEditado.setOrigem(voo.getOrigem());
                vooEditado.setDestino(voo.getDestino());
                vooEditado.setValorPassagem(voo.getValorPassagem());
                vooEditado.setQuantidadeAssentos(voo.getQuantidadeAssentos());
                vooEditado.setQuantidadePassageiros(voo.getQuantidadePassageiros());
                vooRepository.save(vooEditado);
            }
    
            return vooEditado;
        }
    
        // Delete
        public void deletarVoo(Long id) {
            vooRepository.deleteById(id);
        }
    
}
