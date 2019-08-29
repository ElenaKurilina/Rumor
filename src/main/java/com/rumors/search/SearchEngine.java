package com.rumors.search;

import java.util.Set;

public interface SearchEngine {

    Set<String> getLinksFor(String query);
}
