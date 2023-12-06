package com.mock.interview.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {
//    private static final String PATTERN_STRING = "[$][_](\\w+)_";
    private static final String PATTERN_STRING = "[$][_](\\w+)_";
    private static final Pattern VALID_PATTERN = Pattern.compile(PATTERN_STRING);

    public static String format(String template, Map<String, Object> parameters) {
        StringBuilder newTemplate = new StringBuilder(template);
        List<Object> valueList = new ArrayList<>();

        Matcher matcher = VALID_PATTERN.matcher(template);

        while (matcher.find()) {
            String key = matcher.group(1);

            String paramName = "$_" + key + "_";
            int index = newTemplate.indexOf(paramName);
            if (index != -1) {
                newTemplate.replace(index, index + paramName.length(), "%s");
                valueList.add(parameters.get(key));
            }
        }

        return String.format(newTemplate.toString(), valueList.toArray());
    }

}
