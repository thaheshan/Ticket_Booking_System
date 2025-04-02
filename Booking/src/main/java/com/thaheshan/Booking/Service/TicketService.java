//import com.thaheshan.Booking.Models.Ticketpool;
//import com.thaheshan.Booking.Repository.TicketRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TicketService {
//    @Autowired
//    private TicketRepository ticketRepository;
//
//    public List<Ticketpool> getAllTickets() {
//        return ticketRepository.findAll();
//    }
//
//    public Ticket createTicket(Ticket ticket) {
//        return ticketRepository.save(ticket);
//    }
//
//    public void updateTicketStatus(Long id, String status) {
//        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
//        ticket.setStatus(status);
//        ticketRepository.save(ticket);
//    }
//}
