package net.semanticmetadata.lire;

import junit.framework.TestCase;
import net.semanticmetadata.lire.imageanalysis.features.global.*;
import net.semanticmetadata.lire.indexers.parallel.ParallelIndexer;

public class MyTest extends TestCase {
    public void testParallelIndexer(){
        ParallelIndexer indexer = new ParallelIndexer(3, "index", "testdata/ferrari/black/");
        //indexer.addExtractor(ColorLayout.class);
        //indexer.addExtractor(ScalableColor.class);
        //indexer.addExtractor(EdgeHistogram.class);
        indexer.addExtractor(DominantColor.class);
        indexer.run();
    }
}
