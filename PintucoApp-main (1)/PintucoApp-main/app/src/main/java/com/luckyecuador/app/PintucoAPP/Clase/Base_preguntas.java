package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_preguntas {

    public String id;//YA
    public String question;//YA
    public String answer;//YA
    public String opta;//YA
    public String optb;//YA
    public String optc;//YA
    public String canal;//YA
    public String tiempo; //YA
    public String test_id; //YA

    public Base_preguntas() {}

    public Base_preguntas(String id, String question, String answer, String opta, String optb, String optc, String canal,String tiempo,String test_id) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.opta = opta;
        this.optb = optb;
        this.optc = optc;
        this.canal = canal;
        this.tiempo = tiempo;
        this.test_id = test_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOpta() {
        return opta;
    }

    public void setOpta(String opta) {
        this.opta = opta;
    }

    public String getOptb() {
        return optb;
    }

    public void setOptb(String optb) {
        this.optb = optb;
    }

    public String getOptc() {
        return optc;
    }

    public void setOptc(String optc) {
        this.optc = optc;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }
}
