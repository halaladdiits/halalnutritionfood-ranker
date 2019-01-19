package id.ac.its.is.addi.halal.model;

import java.util.List;
import java.util.Map;

public class Result {
    Double score;
    String label;
    List<Map<String, String>> atributes;

    public Result() {

    }

    public Result(Double score, String type, String label, String manufacture, String foodCode, String comment, List<Map<String, String>> atributes) {
        this.score = score;
        this.label = label;
        this.atributes = atributes;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }



    public List<Map<String, String>> getAtributes() {
        return atributes;
    }

    public void setAtributes(List<Map<String, String>> atributes) {
        this.atributes = atributes;
    }
}
