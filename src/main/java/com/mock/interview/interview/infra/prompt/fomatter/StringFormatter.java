package com.mock.interview.interview.infra.prompt.fomatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {
    private static final String PATTERN_STRING = "[$][_](\\w+)_";
    private static final Pattern VALID_PATTERN = Pattern.compile(PATTERN_STRING);

    /**
     * template에 $_key_ 형식을 파라미터에 key에 맞는 값으로 치환. 만약 key가 없을 경우 null로 치환
     * @param template $_abc_ $_kkk_ $_ccc_
     * @param parameters {"abc":"Hello", "kkk":"World!"}
     * @return "Hello World! null"
     */
    public static String format(String template, Map<String, String> parameters) {
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
