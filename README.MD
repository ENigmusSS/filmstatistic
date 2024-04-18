Script takes as CLI args the path to folder with .json files of given format and arg to count.   
JSON format:
```json
[
    {
        "arg1":["a","b","c"],
        "arg2":["a","b","c"]
    },
    {
        "arg1":["a","b","c"],
        "arg2":["a","b","c"]
    }
]
```
As output script generates "statistics_by_$arg.xml" file of format:   
```xml
<?xml version="1.0" encoding="UTF-8"?>

<statistics>
    <item>
        <value>a</value>
        <count>3</count>
    </item>
    <item>
        <value>b</value>
        <count>2</count>
    </item>
    <item>
        <value>c</value>
        <count>1</count>
    </item>
</statistics>
```
Script do not map files` contents in any kind of entities and do not keep any data except cli argument defined field, and is domain independent.   
Resources folder contains files examples.    
Multithreading tests on 10 .json files with 10-12 entities each, of 8 attributes:    
1 thread:  197743500 ns   
2 threads: 208955200 ns   
4 threads: 217095300 ns   
8 threads: 213042700 ns   
10 threads:221561600 ns