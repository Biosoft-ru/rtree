package ru.biosoft.rtree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

public class RTree
{
    private int[] data;
    private IntervalArray leafs;
    
    public void findOverlapping(int from, int to, IntConsumer resultConsumer)
    {
        findOverlapping( from, to, resultConsumer, 0 );
    }
    
    private void findOverlapping(int from, int to, IntConsumer resultConsumer, int node)
    {
        if(node >= nodeCount())
        {
            int idx = node - nodeCount();
            if(idx < leafs.size() && from <= leafs.getTo( idx ) && to >= leafs.getFrom( idx ))
                resultConsumer.accept( idx );
            return;
        }
        if(isStub(node))
        	return;
        if(from <= getTo(node) && to >= getFrom(node))
        {
            findOverlapping( from, to, resultConsumer, leftChildNode(node) );
            findOverlapping( from, to, resultConsumer, rightChildNode(node) );
        }
    }
    
    
    //return index of closest interval or -1
    public int findClosest(int from, int to)
    {
    	return findClosest(from, to, 0);
    }
    
    
    private int findClosest(int from, int to, int node)
    {
        if(node >= nodeCount())
        {
        	int idx = node - nodeCount();
        	if(idx < leafs.size())
        		return idx;
        	else
        		return -1;
        }
        if(isStub(node))
        	return -1;
        
        if(from <= getFrom(node))
        	return leftMostLeaf(node);
        if(to >= getTo(node))
        	return rightMostLeaf(node);
        
        //query is contained in node interval
        int lRes = findClosest(from, to, leftChildNode(node));
        int rRes = findClosest(from, to, rightChildNode(node));
        int lDistance = lRes == -1 ? Integer.MAX_VALUE : distance(from, to, leafs.getFrom(lRes), leafs.getTo( lRes ));
        int rDistance = rRes == -1 ? Integer.MAX_VALUE : distance(from, to, leafs.getFrom(rRes), leafs.getTo( rRes ));
        if(lDistance <= rDistance)
        	return lRes;
        return rRes;
    }
    

    private int distance(int from1, int to1, int from2, int to2)
    {
        if(from1 > to2)
            return from1 - to2;
        if(from2 > to1)
            return from2 - to1;
        return 0;
    }
    
    //return index in leafs
    private int leftMostLeaf(int node)
    {
    	while(node < nodeCount())
    		node = leftChildNode(node);
    	return node - nodeCount();
    }

    //return index in leafs
    private int rightMostLeaf(int node)
    {
    	while(node < nodeCount())
    	{
    		node = rightChildNode(node);
    		if(node < nodeCount() && isStub(node))
    			return leafs.size() - 1;
    	}
    	int idx = node - nodeCount();
    	if(idx >= leafs.size())
    		idx = leafs.size() - 1;
    	return idx;
    }
  
  
    public TIntList queryIndices(int from, int to)
    {
        TIntList result = new TIntArrayList();
        findOverlapping( from, to, idx->result.add( idx ) );
        return result;
    }
    
    public <T> List<T> queryList(List<T> data, int from, int to)
    {
        List<T> result = new ArrayList<>();
        findOverlapping( from, to, idx->result.add( data.get( idx ) ) );
        return result;
    }
    
    
    public void build(IntervalArray ins)
    {
        leafs = ins;
        int size = ins.size();
        
        int nlevels = 0;
        int n = 0;//node count

        //the last level is virtual (ins array itself)
        while(n+1 < size)//n+1 is the number of nodes on the next level
        {
            n += 1 + n;
            nlevels++;
        }
        
        data = new int[n*2];//one node is two integers
        for(int node = 0; node < nodeCount(); node++)
        {
            //will overlap nothing
            setFrom(node, 1);
            setTo(node, 0);
        }
        
        if(size <= 1)
            return;
        
        int startNode = firstNodeOnLevel(nlevels-1);
        
        for(int i = 0; i < size; i+=2)
        {
            int min = i+1 < size ? Math.min( ins.getFrom( i ), ins.getFrom( i+1 ) ) : ins.getFrom( i );
            int max = i+1 < size ? Math.max( ins.getTo( i ), ins.getTo( i+1 ) ) : ins.getTo( i );
            data[startNode*2+i] = min;
            data[startNode*2+i+1] = max;
        }
        
        for(int level = nlevels-2;level >= 0; level--)
        {
            startNode = firstNodeOnLevel(level);
            for(int node = startNode; node <= 2*startNode; node++)
            {
                int lNode = leftChildNode(node);
                int rNode = rightChildNode(node);
                setFrom(node, Math.min( getFrom(lNode), getFrom(rNode) ));
                setTo(node, Math.max( getTo(lNode), getTo(rNode) ));
            }
        }
        
        
    }
    
    private int leftChildNode(int node) { return 2*node+1; }
    private int rightChildNode(int node) { return 2*node+2; }
    private int nodeCount() { return data.length/2; }
    private int getFrom(int node) { return data[2*node]; }
    private int setFrom(int node, int value) { return data[2*node] = value; }
    private int getTo(int node) { return data[2*node+1]; }
    private int setTo(int node, int value) { return data[2*node+1] = value; }
    private int firstNodeOnLevel(int level) { return (1<<level) - 1; }
    private boolean isStub(int node) { return getFrom(node) > getTo(node); }
}
