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
		chrData.sites.add(site);
		chrData.intervals.add(from, to);
	}
	public void build()
	{
		byChr.values().forEach(chrData-> {
			chrData.index = new RTree();
			chrData.index.build(chrData.intervals);
		});
	}
	
	public List<T> queryOverlapping(String chr, int from, int to)
	{
		ChrData<T> chrData = byChr.get(chr);
		if(chrData == null)
			return Collections.emptyList();
		return chrData.index.queryList(chrData.sites, from, to);
	}
	
	private static class ChrData<T>
	{
		List<T> sites = new ArrayList<>();
		RTree index;
		CompactIntervalList intervals = new CompactIntervalList();
	}
}
