package com.rumors.words;

import java.util.Set;

public interface Exclude {
    Set<String> words = Set.of(
            "about", "above", "across", "after", "against", "among", "around", "at", "before", "behind", "below", "beside",
            "between", "by", "down", "during", "for", "from", "in", "inside", "into", "near", "of", "off", "on", "out",
            "over", "through", "to", "toward", "under", "up", "with", "aboard", "along", "amid", "as", "beneath",
            "beyond", "but", "concerning", "considering", "despite", "except", "following", "like", "minus", "next",
            "onto", "opposite", "outside", "past", "per", "plus", "regarding", "round", "save", "since", "than",
            "underneath", "unlike", "until", "upon", "versus", "via", "within", "without",

            "do", "did", "will", "would", "have", "had", "has", "be", "is", "am", "was", "were", "been", "make", "want",
            "should", "does", "being", " doing", "having", "making", "need",
            "dont", "cant", "didnt", "wasnt", "wont", "havent", "hasnt",

            "which", "what", "that", "there", "this", "these", "when", "then", "those", "such", "where", "here",

            "you", "he", "she", "it", "they", "we", "us", "himself", "herself", "itself", "them", "him",
            "your", "her", "his", "my", "mine", "their",

            "more", "less", "most", "some", "many", "only", "just",
            "also", "because", "other",

            "twitter", "tweet", "instagram", "youtube", "retweeted", "retweet", "share", "tweets"
    );
}
