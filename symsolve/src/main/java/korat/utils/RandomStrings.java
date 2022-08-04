package korat.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomStrings {

    public static Set<String> generateRandomStringSet(int setSize, int minLength, int maxLength) {
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < setSize; i++)
            addRandomStringToSet(set, minLength, maxLength);
        assert (setSize == set.size());
        return set;
    }

    private static void addRandomStringToSet(Set<String> set, int minLength, int maxLength) {
        Random r = new Random();
        int length = r.nextInt((maxLength - minLength) + 1) + minLength;
        String elem = generateRandomString(length);
        while (set.contains(elem))
            elem = generateRandomString(length);
        set.add(elem);
    }

    public static String generateRandomString(int stringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1).limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}
