package ru.neostudy.loanConveyorProject.deal.entity;

import lombok.Data;
import ru.neostudy.loanConveyorProject.deal.enums.ApplicationStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "application_id", nullable = false)
    private String applicationId; // int or String??

    //ForeignKey1
    @Column(name = "client_id", nullable = false)
    private long clientId;

    //ForeignKey1
    @Column(name = "credit_id", nullable = false)
    private long creditId;

    @Column(name = "application_status", nullable = false)
    private ApplicationStatus applicationStatus;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

   // @Column(name = "applied_offer", nullable = false)
    // private LoanOfferDTO appliedOffer; // Type - ??

    @Column(name = "sign_date", nullable = false)
    private LocalDate signDate;

    @Column(name = "ses_code", nullable = false)
    private long sesCode;

  //  @Column(name = "status_history", nullable = false)
//    private List<StatusHistory> statusHistoryList;
}
