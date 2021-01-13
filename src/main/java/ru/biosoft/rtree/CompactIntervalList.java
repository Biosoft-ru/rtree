package ru.biosoft.rtree;

import gnu.trove.list.array.TIntArrayList;

public class CompactIntervalList implements IntervalArray
{
    private TIntArrayList data;
    
    public CompactIntervalList() {
        data = new TIntArrayList();
    }
    
    public CompactIntervalList(int capacity) {
        data = new TIntArrayList( capacity );
    }

    @Override
    public int getFrom(int i)
    {
        return data.get( 2*i );
    }
    
    public void setFrom(int i, int val)
    {
        data.set( 2*i, val );
    }

    @Override
    public int getTo(int i)
    {
        return data.get( 2*i+1 );
    }
    
    public void setTo(int i, int val)
    {
        data.set( 2*i+1, val );
    }
    
    public void add(int from, int to)
    {
        data.add( from );
        data.add( to );
    }

    @Override
    public int size()
    {
        return data.size()/2;
    }
    
    public void trimToSize()
    {
        data.trimToSize();
    }
}
