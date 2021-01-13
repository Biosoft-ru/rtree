package ru.biosoft.rtree;

public class CompactIntervalArray implements IntervalArray
{
    private int[] data;
    
    public CompactIntervalArray(int size)
    {
        data = new int[size*2];
    }

    @Override
    public int getFrom(int i)
    {
        return data[2*i];
    }
    
    public void setFrom(int i, int val)
    {
        data[2*i] = val;
    }

    @Override
    public int getTo(int i)
    {
        return data[2*i+1];
    }
    
    public void setTo(int i, int val)
    {
        data[2*i+1]=val;
    }

    @Override
    public int size()
    {
        return data.length/2;
    }
}
