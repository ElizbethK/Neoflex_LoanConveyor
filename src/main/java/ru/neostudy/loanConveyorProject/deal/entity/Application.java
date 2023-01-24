package ru.neostudy.loanConveyorProject.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.stereotype.Component;
import ru.neostudy.loanConveyorProject.conveyor.dto.LoanOfferDTO;
import ru.neostudy.loanConveyorProject.deal.enums.ApplicationStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Component
@Getter
@Setter
@Data
@Entity
@Table(name = "application")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "application_id", nullable = false)
    private long applicationId;

    //ForeignKey1
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

    //ForeignKey1
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    private Credit credit;

    //Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private ApplicationStatus applicationStatus;


    @Column(name = "creation_date")
    private LocalDate creationDate;


    //Jsonb
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "applied_offer")
     private LoanOfferDTO appliedOffer;


    @Column(name = "sign_date")
    private LocalDate signDate;


    @Column(name = "ses_code")
    private long sesCode;


    //Jsonb
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "status_history")
    private List<StatusHistoryJsonb> statusHistoryList;


    public void setStatusHistoryList(StatusHistoryJsonb statusHistoryJsonb) {
        statusHistoryList.add(statusHistoryJsonb);
    }
}
