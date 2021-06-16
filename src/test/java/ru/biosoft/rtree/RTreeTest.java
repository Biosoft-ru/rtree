package ru.biosoft.rtree;

import gnu.trove.list.TIntList;
import junit.framework.TestCase;

public class RTreeTest extends TestCase
{
    public void test1()
    {
        CompactIntervalList data = new CompactIntervalList();
        data.add(1, 10);
        data.add(5, 11);
        
        RTree rTree = new RTree();
        rTree.build( data );
        
        TIntList indices = rTree.queryIndices( 0, 20 );
        assertEquals( 2, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        assertEquals( 1, indices.get( 1 ));
        
        indices = rTree.queryIndices( 0, 1 );
        assertEquals( 1, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        
        indices = rTree.queryIndices( 2, 4 );
        assertEquals( 1, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        
        indices = rTree.queryIndices( 2, 5 );
        assertEquals( 2, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        assertEquals( 1, indices.get( 1 ));
        
        indices = rTree.queryIndices( 11, 15 );
        assertEquals( 1, indices.size() );
        assertEquals( 1, indices.get( 0 ));
    }
    
    public void testNeg()
    {
        CompactIntervalList data = new CompactIntervalList();
        data.add(1, 10);
        data.add(5, 11);
        data.add(12, 13);
        data.add(13, 14);
        data.add(14, 15);
        
        RTree rTree = new RTree();
        rTree.build( data );
        
        TIntList indices = rTree.queryIndices( 0, 1 );
        assertEquals( 1, indices.size() );
        assertEquals( 0, indices.get( 0 ));
    }
    
    
    public void testEmpty()
    {
    	CompactIntervalList data = new CompactIntervalList();
        
        RTree rTree = new RTree();
        rTree.build( data );
        
        TIntList indices = rTree.queryIndices( -100, +100 );
        assertEquals( 0, indices.size() );
    }
    
    public void testSingle()
    {
    	CompactIntervalList data = new CompactIntervalList();
        data.add(1, 10);
        
        RTree rTree = new RTree();
        rTree.build( data );
        
        TIntList indices = rTree.queryIndices( 0, 20 );
        assertEquals( 1, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        
        indices = rTree.queryIndices( 0, 1 );
        assertEquals( 1, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        
        indices = rTree.queryIndices( 10, 12 );
        assertEquals( 1, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        
        indices = rTree.queryIndices( -1, 0 );
        assertEquals( 0, indices.size() );
        
        indices = rTree.queryIndices( 11, 15 );
        assertEquals( 0, indices.size() );
    }
    
    public void testNested()
    {
    	CompactIntervalList data = new CompactIntervalList();
        data.add(1, 10);
        data.add(3, 7);
        
        RTree rTree = new RTree();
        rTree.build( data );
        
        TIntList indices = rTree.queryIndices( 0, 20 );
        assertEquals( 2, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        assertEquals( 1, indices.get( 1 ));
        
        indices = rTree.queryIndices( 0, 1 );
        assertEquals( 1, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        
        indices = rTree.queryIndices( 2, 4 );
        assertEquals( 2, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        assertEquals( 1, indices.get( 1 ));
        
        indices = rTree.queryIndices( 2, 5 );
        assertEquals( 2, indices.size() );
        assertEquals( 0, indices.get( 0 ));
        assertEquals( 1, indices.get( 1 ));
        
        indices = rTree.queryIndices( 11, 15 );
        assertEquals( 0, indices.size() );
    }
    
    public void testFindClosest()
    {
        CompactIntervalList data = new CompactIntervalList();
        data.add(1, 10);
        data.add(5, 11);
        
        RTree rTree = new RTree();
        rTree.build( data );

        assertEquals(1, rTree.findClosest(20, 22));
        assertEquals(1, rTree.findClosest(11, 11));
        assertEquals(0, rTree.findClosest(2, 3));
        assertEquals(0, rTree.findClosest(0, 0));
    }
}
