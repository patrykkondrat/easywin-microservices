package com.easywin.betservice.service;

import com.easywin.betservice.dto.BetRequest;
import com.easywin.betservice.dto.BetResponse;
import com.easywin.betservice.dto.BetToTicketResponse;
import com.easywin.betservice.event.UpdateBetStatusInTicket;
import com.easywin.betservice.model.Bet;
import com.easywin.betservice.model.BetStatus;
import com.easywin.betservice.repository.BetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

    @Mock
    BetRepository betRepository;

    @Mock
    KafkaTemplate<String, UpdateBetStatusInTicket> kafkaTemplate;

    BetService betService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        betService = new BetService(betRepository, kafkaTemplate);
    }

    @Test
    void createBet() {
        BetRequest betRequest = new BetRequest("description", "1.0",
                "2.0", "hostname", "guestname", "date", "discipline");

        Bet bet = Bet.builder()
                ._id(UUID.randomUUID().toString())
                .description(betRequest.getDescription())
                .hostRate(betRequest.getHostRate())
                .guestRate(betRequest.getGuestRate())
                .guestname(betRequest.getGuestname())
                .hostname(betRequest.getHostname())
                .betStatus(BetStatus.PENDING)
                .date(betRequest.getDate())
                .discipline(betRequest.getDiscipline())
                .build();

        when(betRepository.save(any(Bet.class))).thenReturn(bet);

        betService.createBet(betRequest);

        verify(betRepository, times(1)).save(any(Bet.class));
    }

    @Test
    void deleteBet_CorrectId() {
        String id = "1";

        when(betRepository.existsById(id)).thenReturn(true);

        betService.deleteBet(id);

        verify(betRepository).deleteById(id);
    }

    @Test
    void deleteBet_IncorrectId() {
        String id = "1";
        when(betRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> betService.deleteBet(id));
    }

    @Test
    void getAllBet() {
        Bet bet1 = new Bet("id1", "description1", "2.0", "2.0", "hostname1",
                "guestname1", BetStatus.PENDING, "date", "discipline1");
        Bet bet2 = new Bet("id2", "description2", "2.0", "2.0", "hostname2",
                "guestname2", BetStatus.PENDING, "date", "discipline2");

        List<Bet> bets = Arrays.asList(bet1, bet2);
        when(betRepository.findAll()).thenReturn(bets);

        List<BetResponse> betResponses = betService.getAllBet();

        assertEquals(betResponses.size(), 2);
        assertEquals(betResponses.get(0).getDescription(), "description1");
        assertEquals(betResponses.get(1).getDescription(), "description2");
        assertEquals(betResponses.get(0).getHostname(), "hostname1");
        assertEquals(betResponses.get(1).getHostname(), "hostname2");
        assertEquals(betResponses.get(0).getGuestname(), "guestname1");
        assertEquals(betResponses.get(1).getGuestname(), "guestname2");
        assertEquals(betResponses.get(0).getDiscipline(), "discipline1");
        assertEquals(betResponses.get(1).getDiscipline(), "discipline2");
        assertEquals(betResponses.get(0).getBetStatus(), BetStatus.PENDING);
        assertEquals(betResponses.get(1).getBetStatus(), BetStatus.PENDING);
        assertEquals(betResponses.get(0).getDate(), "date");
        assertEquals(betResponses.get(1).getDate(), "date");
    }

    @Test
    void getBetById() {
        String id = UUID.randomUUID().toString();
        Bet bet3 = new Bet(id, "description3", "2.0", "2.0", "hostname3",
                "guestname3", BetStatus.PENDING, "date3", "discipline3");
        when(betRepository.findById(id)).thenReturn(Optional.of(bet3));

        BetResponse betResponse = betService.getBetById(id);

        assertEquals("description3", betResponse.getDescription());
        assertEquals("2.0", betResponse.getHostRate());
        assertEquals("2.0", betResponse.getGuestRate());
        assertEquals("hostname3", betResponse.getHostname());
        assertEquals("guestname3", betResponse.getGuestname());
        assertEquals(BetStatus.PENDING, betResponse.getBetStatus());
        assertEquals("date3", betResponse.getDate());
        assertEquals("discipline3", betResponse.getDiscipline());
    }

    @Test
    void isBetInBets() {
        List<String> betIds = Arrays.asList("1", "2", "3");
        List<Bet> bets = Arrays.asList(
                new Bet("1", "description1", "2.0", "2.0", "hostname1",
                        "guestname1", BetStatus.PENDING, "date",
                        "discipline1"),
                new Bet("2", "description2", "2.0", "2.0", "hostname2",
                        "guestname2", BetStatus.PENDING, "date",
                        "discipline2"),
                new Bet("3", "description3", "2.0", "2.0", "hostname3",
                        "guestname3", BetStatus.PENDING, "date",
                        "discipline3")
        );

        when(betRepository.findById("1")).thenReturn(Optional.of(bets.get(0)));
        when(betRepository.findById("2")).thenReturn(Optional.of(bets.get(1)));
        when(betRepository.findById("3")).thenReturn(Optional.of(bets.get(2)));
        when(betRepository.findById("4")).thenReturn(Optional.empty());
        when(betRepository.findById("5")).thenReturn(Optional.empty());
        when(betRepository.findById("6")).thenReturn(Optional.empty());

        List<BetToTicketResponse> result =
                betService.isBetInBets(Arrays.asList("1", "2", "3", "4", "5",
                        "6"));

        assertThat(result.get(0)).isEqualToComparingFieldByField(new BetToTicketResponse("1", "hostname1",
                "guestname1", "2.0", "2.0", BetStatus.PENDING));
        assertThat(result.get(1)).isEqualToComparingFieldByField(new BetToTicketResponse("2", "hostname2",
                "guestname2", "2.0", "2.0", BetStatus.PENDING));
    }

    @Test
    void getDisciplines() {
        Bet bet1 = new Bet();
        bet1.setDiscipline("Football");
        Bet bet2 = new Bet();
        bet2.setDiscipline("Football");
        Bet bet3 = new Bet();
        bet3.setDiscipline("Basketball");

        List<Bet> bets = Arrays.asList(bet1, bet2, bet3);

        when(betRepository.findAll()).thenReturn(bets);

        Set<String> disciplines = betService.getDisciplines();

        assertEquals(2, disciplines.size());

        assertEquals(disciplines, Set.of("Football", "Basketball"));
    }

    @Test
    void getBetByDiscipline() {
        Bet bet1 = new Bet("id1", "description1", "2.0", "2.0", "hostname1",
                "guestname1", BetStatus.PENDING, "date", "discipline1");
        Bet bet2 = new Bet("id2", "description2", "2.0", "2.0", "hostname2",
                "guestname2", BetStatus.PENDING, "date", "discipline2");
        Bet bet3 = new Bet("id3", "description3", "2.0", "2.0", "hostname2",
                "guestname3", BetStatus.PENDING, "date", "discipline1");

        List<Bet> bets = Arrays.asList(bet1, bet2, bet3);
        String targetDiscipline = "discipline1";
        List<Bet> betsByDiscipline = Arrays.asList(bet1, bet3);

        when(betRepository.findBetByDiscipline(targetDiscipline)).thenReturn(betsByDiscipline);

        List<BetResponse> betResponses = betService.getBetByDiscipline(targetDiscipline);

        assertEquals(betResponses.size(), 2);
        assertEquals(betsByDiscipline.get(0).get_id(),
                betResponses.get(0).get_id());
        assertEquals(betsByDiscipline.get(1).get_id(),
                betResponses.get(1).get_id());
    }

    @Test
    void changeBetStatus_PendingToHomeWin() {
        String id = UUID.randomUUID().toString();
        BetStatus betStatus = BetStatus.PENDING;

        Bet bet = new Bet();
        bet.set_id(id);
        bet.setBetStatus(betStatus);

        when(betRepository.findById(anyString())).thenReturn(Optional.of(bet));

        betService.changeBetStatus(id, BetStatus.HOME_TEAM_WIN);

        verify(betRepository, times(1)).save(eq(bet));
    }

    @Test
    void changeBetStatus_PendingToGuestWin() {
        String id = UUID.randomUUID().toString();
        BetStatus betStatus = BetStatus.PENDING;

        Bet bet = new Bet();
        bet.set_id(id);
        bet.setBetStatus(betStatus);

        when(betRepository.findById(anyString())).thenReturn(Optional.of(bet));

        betService.changeBetStatus(id, BetStatus.GUEST_TEAM_WIN);

        verify(betRepository, times(1)).save(eq(bet));
    }
    @Test
    void changeBetStatus_HomeWinToPending() {
        String id = UUID.randomUUID().toString();
        BetStatus betStatus = BetStatus.HOME_TEAM_WIN;

        Bet bet = new Bet();
        bet.set_id(id);
        bet.setBetStatus(betStatus);

        when(betRepository.findById(anyString())).thenReturn(Optional.of(bet));

        betService.changeBetStatus(id, BetStatus.PENDING);

        verify(betRepository, times(1)).save(eq(bet));
    }
}