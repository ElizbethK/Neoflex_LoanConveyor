package ru.neostudy.loanConveyorProject.deal.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "passport")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "passport_id", nullable = false)
    private long passportId;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private String number;

    @Column(name = "issue_branch")
    private String issueBranch;

    @Column(name = "issue_date")
    private LocalDate issueDate;

}
