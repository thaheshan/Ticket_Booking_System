//package com.thaheshan.Booking.Controller;
//
//import com.thaheshan.Booking.Service.TicketService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/tickets")
//public class TicketController {
//    @Autowired
//    private TicketService ticketService;
//
//    @GetMapping
//    public List<Ticket> getAllTickets() {
//        return ticketService.getAllTickets();
//    }
//
//    @PostMapping
//    public Ticket createTicket(@RequestBody Ticket ticket) {
//        return ticketService.createTicket(ticket);
//    }
//
//    @PutMapping("/{id}")
//    public void updateTicketStatus(@PathVariable Long id, @RequestParam String status) {
//        ticketService.updateTicketStatus(id, status);
//    }
//}
//
