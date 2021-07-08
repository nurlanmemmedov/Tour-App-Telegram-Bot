package com.example.telegrambotapi.models;

import com.example.telegrambotapi.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="question_id", nullable=false)
    private Question question;
    private ActionType type;
    private String answer;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "next_id", referencedColumnName = "id")
    private Question nextQuestion;
    @OneToMany(mappedBy = "action",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<ActionTranslation> actionTranslations;

    public String getAnswer(String code) {
        ActionTranslation translation =  actionTranslations.stream()
                .filter(t -> t.getCode().equals(code))
                .findFirst().orElse(null);
        if (translation == null) return this.answer;
        return translation.getText();
    }
}
