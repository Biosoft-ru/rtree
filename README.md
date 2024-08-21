# rtree
Small library to make genome interval overlap queries using memory efficient rtree.

```java
SiteIndex<String> index = new SiteIndex<>();
index.addSite("siteA", "chr1", 100, 200);
index.addSite("siteB", "chr1", 190, 210);
index.build();
List<String> result = index.queryOverlapping("chr1", 50, 110);
```

## Maven

```xml
<dependency>
    <groupId>ru.biosoft.rtree</groupId>
    <artifactId>ru.biosoft.rtree</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Gradle

```groovy
implementation group: 'ru.biosoft.rtree', name: 'ru.biosoft.rtree', version: '0.0.1'
```