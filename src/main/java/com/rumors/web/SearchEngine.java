package com.rumors.web;

import java.util.Set;

public interface SearchEngine {

    Set<String> findLinksFor(String query);
}
