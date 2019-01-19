/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.its.is.addi.halal;

import java.io.IOException;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.Similarity.SimWeight;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author USER
 */
public class TFIEFSimilarity extends Similarity {

    @Override
    public long computeNorm(FieldInvertState state) {
        return 1l;
    }

    @Override
    public SimWeight computeWeight(float boost, CollectionStatistics collectionStats, TermStatistics... termStats) {
        float df = termStats.length == 1 ? this.df(collectionStats, termStats[0]) : this.df(collectionStats, termStats);
        long docCount = collectionStats.docCount() == -1L ? collectionStats.maxDoc() : collectionStats.docCount();
        
        return new TFIEFStats(df, docCount);
    }

    @Override
    public SimScorer simScorer(SimWeight sw, LeafReaderContext lrc) throws IOException {
        TFIEFStats weight = (TFIEFStats) sw;
        return new TFIEFSimScorer(weight);
    }

    public float tf(float f) {
        return (float) f;
    }
    
    private float df(CollectionStatistics collectionStats, TermStatistics termStats) {
        return termStats.docFreq();
    }
    
    private float df(CollectionStatistics collectionStats, TermStatistics[] termStats) {              
        float df = 0.0f;
        for (int i = 0; i < termStats.length; ++i) {
            TermStatistics stat = termStats[i];            
            df += this.df(collectionStats, stat);
        }        
        return df;
    }
    
    class TFIEFStats extends SimWeight {                
        private final float df;
        private final long docCount;

        public TFIEFStats(float df, long docCount) {
            this.df = df;
            this.docCount = docCount;
        }
    }
    
    class TFIEFSimScorer extends SimScorer {
        
        private final TFIEFStats weight;


        public TFIEFSimScorer(TFIEFStats weight) {
            this.weight = weight;
        }        

        @Override
        public float score(int i, float f) throws IOException {
            return (float) ((TFIEFSimilarity.this.tf(f) / weight.df) * Math.log10(weight.docCount / (1 + TFIEFSimilarity.this.tf(f))));
        }

        @Override
        public float computeSlopFactor(int i) {
            return 1f;
        }

        @Override
        public float computePayloadFactor(int i, int i1, int i2, BytesRef br) {
            return 1f;
        }
        
    }

}




