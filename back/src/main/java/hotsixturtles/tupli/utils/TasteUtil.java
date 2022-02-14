package hotsixturtles.tupli.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 취향분석 Method 모음
 */
public class TasteUtil {

    // 상위 다섯개의 취향을 List로 담아 User에 저장 >> 둘러보기에 활용
    public static List<String> getTaste(ConcurrentHashMap<String, Integer> tasteInfo) {
        List<String> userTaste = new ArrayList<>();
        // 유저 정보 분석, MAP을 Value DESC로 나열. 최대 다섯
        Map<String, Integer> topFive =
                tasteInfo.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(5)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (String category : topFive.keySet()) {
            userTaste.add(category);
        }
        return userTaste;
    }
}
