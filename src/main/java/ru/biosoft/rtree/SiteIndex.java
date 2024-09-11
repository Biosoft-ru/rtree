package ru.biosoft.rtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SiteIndex<T> {
	private Map<String, ChrData<T>> byChr = new HashMap<>();
	
	public void addSite(T site, String chr, int from, int to)
	{
		ChrData<T> chrData = byChr.computeIfAbsent(chr, x->new ChrData<>());
		chrData.sites.add(new Entry<>(site,from,to));
	}
	public void build()
	{
		byChr.values().forEach(chrData-> {
			Collections.sort(chrData.sites);
			chrData.index = new RTree();
			chrData.index.build(chrData);
		});
	}
	
	public List<T> queryOverlapping(String chr, int from, int to)
	{
		ChrData<T> chrData = byChr.get(chr);
		if(chrData == null)
			return Collections.emptyList();
		ArrayList<T> result = new ArrayList<>();
		chrData.index.findOverlapping( from, to, idx->result.add( chrData.sites.get( idx ).site ) );
		return result;
	}
	
	public T findClosest(String chr, int from, int to)
	{
		ChrData<T> chrData = byChr.get(chr);
		if(chrData == null)
			return null;
		int idx = chrData.index.findClosest(from, to);
		if(idx == -1)
			return null;
		return chrData.sites.get(idx).site;
	}
	
	private static class ChrData<T> implements IntervalArray
	{
		List<Entry<T>> sites = new ArrayList<>();
		RTree index;
		@Override
		public int getFrom(int i) {
			return sites.get(i).from;
		}
		@Override
		public int getTo(int i) {
			return sites.get(i).to;
		}
		@Override
		public int size() {
			return sites.size();
		}
	}
	
	private static class Entry<T> implements Comparable<Entry<T>>
	{
		public Entry(T site, int from, int to) {
			super();
			this.site = site;
			this.from = from;
			this.to = to;
		}
		T site;
		int from,to;
	    @Override
	    public int compareTo(Entry i)
	    {
	        int res = Integer.compare( from, i.from );
	        return res == 0 ? Integer.compare( to, i.to ) : res;
	    }

	}
}
