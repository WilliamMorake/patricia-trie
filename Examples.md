# Simple Example #

This is a simple example how to use the PATRICIA Trie. The basic concept is very similar to a [SortedMap](http://java.sun.com/javase/6/docs/api/java/util/SortedMap.html) which uses a [Comparator](http://java.sun.com/javase/6/docs/api/java/util/Comparator.html) to compare keys. The PATRICIA Trie is a bit trickier though. It needs bitwise access to the keys. That means one must think in terms of bits and not bytes, characters or an object being greater than an another. The PATRICIA Trie's version of a Comparator is called a KeyAnalyzer and it comes with a couple implementations like the StringKeyAnalyzer.

```
// Trie of First Name -> Person
Trie<String, Person> trie = new PatriciaTrie<String, Person>(StringKeyAnalyzer.INSTANCE);
trie.put("Anna", person1);
trie.put("Alex", person2);
trie.put("Emma", person3);
trie.put("Patrick", person4);
trie.put("William", person5);

// Returns Alex
Map.Entry<String, Person> entry = trie.select("Al");
```

An interesting property of Tries is that they will always return something if they're not empty. An empty Trie is the only case where the select operation will return null! It's however important to understand that this Trie does not check if one key is a prefix on an another. Technically speaking it's a proximity operator (bitwise XOR distance) and it will return whatever is closest to a provided key. Here is a little example that demonstrates the behavior.

```
// Trie of First Name -> Person
Trie<String, Person> trie = new PatriciaTrie<String, Person>(StringKeyAnalyzer.INSTANCE);
trie.put("Xavier", person1);

// Returns Xavier
Map.Entry<String, Person> entry = trie.select("Al");
```

Depending on what you are looking for you may have to do some post processing with the returned values.