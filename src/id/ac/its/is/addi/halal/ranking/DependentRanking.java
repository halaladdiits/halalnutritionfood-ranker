package id.ac.its.is.addi.halal.ranking;

import id.ac.its.is.addi.halal.utils.Settings;
import org.apache.lucene.index.IndexableField;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DependentRanking {
    public DependentRanking() {}
    public Double getSpreadValue(String[] uniqueTerms, List<IndexableField> fields) {
        double q = uniqueTerms.length;
        double v = 1;
        String atributeTemp = "";
        for (int j = 0; j < uniqueTerms.length; j++) {
            System.out.println("==== "+uniqueTerms[j]+" ====");
            for (int k = 0; k < fields.size(); k++) {
                if ((fields.get(k).name().equalsIgnoreCase(Settings.FIELD1_LABEL)
                        || fields.get(k).name().equalsIgnoreCase(Settings.FIELD2_CONTAINSINGREDIENT)
                        || fields.get(k).name().equalsIgnoreCase(Settings.FIELD_EX_SPREAD0_SUBJECT)
                        || fields.get(k).name().equalsIgnoreCase(Settings.FIELD_EX_SPREAD1_PATH))) {

                } else {
                    if (fields.get(k).stringValue().toLowerCase().contains(uniqueTerms[j])) {
                        if (!atributeTemp.equalsIgnoreCase(fields.get(k).name())) {
                            System.out.println("name = "+fields.get(k).name()+" | value = "+fields.get(k).stringValue());
                            v++;
                            atributeTemp = fields.get(k).name();
                            System.out.println("Pluss == Value q = "+q+" === Value v = "+v);
                        } else {

                        }
                    } else {

                    }
                }
                //System.out.println("===================");
                //System.out.println("name = "+fields.get(k).name()+" | value = "+fields.get(k).stringValue());

            }

        }

        Double spread = (((Math.abs(q)-Math.abs(v))+1)/Math.abs(q));

        //System.out.println("Final Value q = "+q+" === Value v = "+v +" === spread ="+spread);

        return spread;

    }


}
