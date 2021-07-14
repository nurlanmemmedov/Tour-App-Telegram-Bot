package com.example.telegrambotapi.models.entities;

import com.example.telegrambotapi.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uuid;
    private Integer clientId;
    private Long chatId;
    private RequestStatus status;

    @JsonManagedReference
    @OneToMany(mappedBy = "request",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Offer> offers;
    @Column(name = "has_next", columnDefinition = "boolean default true")
    private Boolean hasNext;

    public List<Offer> getNextNotSentRequests(){
        return this.offers.stream().filter(o -> o.getIsSent() == null ||
                !o.getIsSent()).limit(5).collect(Collectors.toList());
    }

}
