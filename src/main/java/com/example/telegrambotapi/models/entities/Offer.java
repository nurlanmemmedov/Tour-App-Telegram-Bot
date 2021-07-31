package com.example.telegrambotapi.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String uuid;
    private byte[] image;
    private Integer messageId;
    private Integer offerId;

    @JsonBackReference
    @ManyToOne(targetEntity = Request.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private Request request;

    @Column(name = "is_sent", columnDefinition = "boolean default false")
    private Boolean isSent;
}