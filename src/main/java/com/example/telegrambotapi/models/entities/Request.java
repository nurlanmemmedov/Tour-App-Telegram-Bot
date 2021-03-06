package com.example.telegrambotapi.models.entities;

import com.example.telegrambotapi.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime expireDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @JsonManagedReference
    @OneToMany(mappedBy = "request",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Offer> offers;

    public List<Offer> getNextNotSentRequests(){
        return this.offers.stream().filter(o -> o.getIsSent() == null ||
                !o.getIsSent()).collect(Collectors.toList());
    }

}
