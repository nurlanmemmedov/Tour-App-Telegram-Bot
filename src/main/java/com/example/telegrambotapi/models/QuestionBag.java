package com.example.telegrambotapi.models;

import com.example.telegrambotapi.enums.ActionType;
import com.example.telegrambotapi.models.entities.Action;
import com.example.telegrambotapi.models.entities.Question;
import com.example.telegrambotapi.repositories.QuestionRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Data
public class QuestionBag {

    @Autowired
    private QuestionRepository repository;

    private List<Question> questions;
    private Question firstQuestion;
    private Question phoneQuestion;

    @PostConstruct
    public void init(){
        this.questions = repository.findAll();
        this.firstQuestion = repository.getQuestionByQuestionKey("language");
        this.phoneQuestion = repository.getQuestionByQuestionKey("askPhone");
    }

    public Question getNext(Question question, Message message){
        if (hasButton(question)){
            Action action = question.getActions()
                            .stream().filter(a ->
                            (a.getAnswer().equals(message.getText()) || a.getTranslations()
                                    .stream().anyMatch(t -> t.getText().equals(message.getText()))
                            )).findFirst().orElse(null);
            if (action == null) return null;
            return action.getNextQuestion();
        }
        Action action = question.getActions().stream().findFirst().orElse(null);//TODO
        if (action == null) return null;
        return action.getNextQuestion();
    }

    public Boolean isFirst(Question question){
        return question.getQuestionKey().equals("language");
    }

    public Boolean isLast(Question question){
        return question.getQuestionKey().equals("last");
    }

    public Boolean isEnding(Question question){
        return question.getQuestionKey().equals("end");
    }

    public Boolean hasButton(Question question){
        return question.getActions().stream()
                .anyMatch(a -> a.getType() == ActionType.BUTTON);
    }
}
