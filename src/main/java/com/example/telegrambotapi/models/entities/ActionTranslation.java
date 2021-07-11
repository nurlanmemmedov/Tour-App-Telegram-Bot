package com.example.telegrambotapi.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionTranslation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name="action_id", nullable=false)
    private Action action;
    private String code;
    private String text;
}

