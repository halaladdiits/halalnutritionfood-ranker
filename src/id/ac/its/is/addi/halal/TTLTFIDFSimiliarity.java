package id.ac.its.is.addi.halal;

import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

public class TTLTFIDFSimiliarity extends TFIDFSimilarity {

    public TTLTFIDFSimiliarity(float freq, long docFreq, long docCount, int length, int distance, int doc, int start, int end, BytesRef payload) {
        tf(freq);

    }

    @Override
    public float tf(float v) {
        return 0;
    }

    @Override
    public float idf(long l, long l1) {
        return 0;
    }

    @Override
    public float lengthNorm(int i) {
        return 0;
    }

    @Override
    public float sloppyFreq(int i) {
        return 0;
    }

    @Override
    public float scorePayload(int i, int i1, int i2, BytesRef bytesRef) {
        return 0;
    }
}
