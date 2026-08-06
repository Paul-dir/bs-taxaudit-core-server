package com.mor.itas.api.controller;

import com.mor.itas.domain.model.ExitConference;
import com.mor.itas.domain.model.ExitConferenceAgenda;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.adapter.ExitConferenceAdapter;
import com.mor.itas.persistence.adapter.ExitConferenceAgendaAdapter;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/qa/exit-conference")
public class ExitConferenceController {

    private final ExitConferenceAgendaAdapter agendaAdapter;
    private final ExitConferenceAdapter conferenceAdapter;
    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public ExitConferenceController(ExitConferenceAgendaAdapter agendaAdapter,
                                    ExitConferenceAdapter conferenceAdapter,
                                    QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.agendaAdapter = agendaAdapter;
        this.conferenceAdapter = conferenceAdapter;
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @PostMapping("/agendas")
    public ResponseEntity<ExitConferenceAgenda> submitAgenda(
            @RequestParam Long qaReviewCaseId,
            @RequestParam String preparedByRole,
            @RequestParam String items) {
        ExitConferenceAgenda agenda = new ExitConferenceAgenda();
        agenda.setQaReviewCaseId(qaReviewCaseId);
        agenda.setPreparedByRole(preparedByRole);
        agenda.setItems(items);
        agenda.setStatus("SUBMITTED");

        // Update QA case status
        qaReviewCaseAdapter.findById(qaReviewCaseId).ifPresent(qaCase -> {
            qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.AGENDAS_SUBMITTED);
            qaReviewCaseAdapter.save(qaCase);
        });

        ExitConferenceAgenda saved = agendaAdapter.save(agenda);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/agendas/approve/{agendaId}")
    public ResponseEntity<String> approveAgenda(@PathVariable Long agendaId) {
        return agendaAdapter.findByQaReviewCaseId(findAgendaCaseId(agendaId)).stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .map(agenda -> {
                    agenda.setStatus("APPROVED");
                    agendaAdapter.save(agenda);

                    qaReviewCaseAdapter.findById(agenda.getQaReviewCaseId()).ifPresent(qaCase -> {
                        qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.AGENDAS_APPROVED);
                        qaReviewCaseAdapter.save(qaCase);
                    });

                    return ResponseEntity.ok("Agenda " + agendaId + " approved");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleConference(@RequestParam Long qaReviewCaseId,
                                                     @RequestParam LocalDateTime scheduledAt) {
        ExitConference conference = new ExitConference();
        conference.setQaReviewCaseId(qaReviewCaseId);
        conference.setScheduledAt(scheduledAt);

        conferenceAdapter.save(conference);

        qaReviewCaseAdapter.findById(qaReviewCaseId).ifPresent(qaCase -> {
            qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.EXIT_CONFERENCE_SCHEDULED);
            qaReviewCaseAdapter.save(qaCase);
        });

        return ResponseEntity.ok("Exit conference scheduled for case " + qaReviewCaseId);
    }

    @PostMapping("/conduct/{qaReviewCaseId}")
    public ResponseEntity<String> conductConference(@PathVariable Long qaReviewCaseId,
                                                    @RequestParam String attendees,
                                                    @RequestParam String minutes) {
        return conferenceAdapter.findByQaReviewCaseId(qaReviewCaseId)
                .map(conference -> {
                    conference.setAttendees(attendees);
                    conference.setMinutesOutcomeNotes(minutes);
                    conference.setConductedAt(LocalDateTime.now());
                    conferenceAdapter.save(conference);

                    qaReviewCaseAdapter.findById(qaReviewCaseId).ifPresent(qaCase -> {
                        qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.EXIT_CONFERENCE_CONDUCTED);
                        qaReviewCaseAdapter.save(qaCase);
                    });

                    return ResponseEntity.ok("Exit conference conducted for case " + qaReviewCaseId);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private Long findAgendaCaseId(Long agendaId) {
        // Since we need to find the case by agenda, we'll iterate all
        // This is a simplified approach; in production use a proper query
        return agendaAdapter.findByQaReviewCaseId(agendaId).isEmpty() ? null : agendaId;
    }
}