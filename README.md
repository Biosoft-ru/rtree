# rtree
Small library to make genome interval overlap queries using memory efficient rtree.

```java
SiteIndex<String> index = new SiteIndex<>();
index.addSite("siteA", "chr1", 100, 200);
index.addSite("siteB", "chr1", 190, 210);
index.build();
List<String> result = index.queryOverlapping("chr1", 50, 110);
```
