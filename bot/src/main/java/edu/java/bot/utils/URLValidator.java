package edu.java.bot.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class URLValidator {

    private URLValidator() {
    }

    private static final Pattern URL_PATTERN = Pattern.compile("^(https?)://[^\\s/$.?#].[^\\s]*$");

    public static boolean isValidUrl(String url) {
        return URL_PATTERN.matcher(url).matches();
    }

    public static URI extractUri(String url) throws URISyntaxException {
        return new URI(url);
    }
}
