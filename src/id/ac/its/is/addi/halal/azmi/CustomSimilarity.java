/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.its.is.addi.halal.azmi;

import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.util.BytesRef;

/**
 * @author USER
 */
public class CustomSimilarity extends TFIDFSimilarity {

    @Override
    public float tf(float f) {
        return (float) Math.sqrt(f);
    }

    @Override
    public float idf(long l, long l1) {
        return (float) (Math.log(l1 / (double) (l + 1)) + 1.0);
    }

    @Override
    public float lengthNorm(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float sloppyFreq(int i) {
        return 1.0f / (i + 1);
    }

    @Override
    public float scorePayload(int i, int i1, int i2, BytesRef br) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
