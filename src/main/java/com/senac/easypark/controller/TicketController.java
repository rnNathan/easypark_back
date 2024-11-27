package com.senac.easypark.controller;


import java.util.List;

import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.entities.Ticket;
import com.senac.easypark.util.validacao.ValidarTipoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senac.easypark.model.dto.TicketDTO;
import com.senac.easypark.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    private ValidarTipoAcesso validarTipoAcesso;


    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketDTO> criarTicket(@RequestBody TicketDTO ticketDTO) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        TicketDTO novoTicket = ticketService.criarTicket(ticketDTO);
        return new ResponseEntity<>(novoTicket, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> buscarTicketPorId(@PathVariable Integer id) throws EstacionamentoException {
        // Busca um ticket pelo ID
        validarTipoAcesso.validarSeExisteUsuario();
        TicketDTO ticket = ticketService.buscarTicketPorId(id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> listarTickets() throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        List<TicketDTO> tickets = ticketService.listarTickets();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<TicketDTO> finalizarTicket(@PathVariable Integer id) throws EstacionamentoException {
        validarTipoAcesso.validarSeExisteUsuario();
        // Finaliza um ticket (registra a saída e calcula o valor)
        TicketDTO ticketFinalizado = ticketService.finalizarTicket(id);
        return ResponseEntity.ok(ticketFinalizado);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
