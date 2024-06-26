package removetags;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static List<String> separateSections = null;
    private static List<String> lineTags = null;

    public static void main(String[] args){


        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());

        while(testCases>0){
            String line = in.nextLine();

            separateSections = separateTagSections(line);

            line = clearAndValidateTag();

            System.out.print(line);

            testCases--;
        }
    }

    private static String clearAndValidateTag() {
        StringBuilder content = new StringBuilder();

        for (int indexI = 0; indexI < lineTags.size(); indexI++) {
            String[] textArray = lineTags.get(indexI).split(",");
            String split = separateSections.get(indexI);
            int initialIndex = textArray.length / 2;
            int arraySize = textArray.length;
            String contentFor = "";

            for (int indexJ = 0; indexJ < initialIndex; indexJ++) {

                String start = textArray[indexJ];
                String end = textArray[arraySize - 1];

                String[] startTag = start.split("-");
                String[] endTag = end.split("-");

                String startText = split.substring(Integer.parseInt(startTag[0]), Integer.parseInt(startTag[1]));
                String endText = split.substring(Integer.parseInt(endTag[0]), Integer.parseInt(endTag[1])).replace("/", "");

                if (startText.equals(endText)) {
                    contentFor = split.substring(Integer.parseInt(startTag[1]), Integer.parseInt(endTag[0]));
                } else {
                    split = "None";
                    contentFor = split;
                    break;
                }

                arraySize--;
            }
            content.append(contentFor).append("\n");
        }

        return content.toString();
    }

    private static List<String> separateTagSections(String line) {
        separateSections = new ArrayList<>();
        lineTags = new ArrayList<>();

        StringBuilder tagPosition = null;
        String flagSlit = "";

        final String separateTagRegex = "</([0-9a-zA-Z ])+><([0-9a-zA-Z ])+>";
        final String tagRegex = "<([/0-9a-zA-Z ])+>";

        Pattern pattern = Pattern.compile(separateTagRegex);
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            flagSlit = matcher.group().replace("><", ">_--_<");
            line = line.replaceAll(matcher.group(), flagSlit);
        }

        if (line.contains(flagSlit)) {
            separateSections = Arrays.stream(line.split("_--_")).toList();
        }

        for (String split : separateSections) {
            pattern = Pattern.compile(tagRegex);
            matcher = pattern.matcher(split);

            tagPosition = new StringBuilder();
            while (matcher.find()) {
                tagPosition
                        .append(matcher.start())
                        .append("-")
                        .append(matcher.end())
                        .append(",");
            }
            lineTags.add(tagPosition.toString());
        }
        return separateSections;
    }
}