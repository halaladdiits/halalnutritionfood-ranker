package id.ac.its.is.addi.halal.ranking;

public class FinalScore {
    public FinalScore() {}

    public Double getFinalScore(Double staticScore, Double queryScore) {
        Double w = 1.8;
        Double k = 1.0;
        Double a = 0.6;
        Double finalScore = Math.log(queryScore) + (w * (Math.pow(staticScore, a)/(Math.pow(k,a)+Math.pow(staticScore,a))));
        return finalScore;
    }
}
