package com.a405.gamept.game.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_bonus")
public class JobBonus {

    @Id
    private String code;
    @ManyToOne
    @JoinColumn(name = "job_code")
    private Job job;

}
