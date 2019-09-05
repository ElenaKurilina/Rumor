package com.rumors.web;

import java.util.Set;

public interface SearchEngine {

    Set<String> getLinksFor(String query);
}
