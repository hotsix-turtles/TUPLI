package hotsixturtles.tupli.service.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CategorySortList {

    public static final Map<Integer, Integer> CATEGORY_SORT_LIST = createCategorySortList();

    // userinfo 엔티티 뱃지용 장르 순서대로 번호 설정
    private static Map<Integer, Integer> createCategorySortList() {
        Map<Integer, Integer> result = new HashMap<>();
        result.put(1, 7);
        result.put(2, 11);
        result.put(10, 8);
        result.put(15, 5);
        result.put(17, 10);
        result.put(18, 7);
        result.put(19, 1);
        result.put(20, 2);
        result.put(21, 3);
        result.put(22, 3);
        result.put(23, 6);
        result.put(24, 6);
        result.put(25, 9);
        result.put(26, 4);
        result.put(27, 9);
        result.put(28, 9);
        result.put(29, 11);
        result.put(30, 7);
        result.put(31, 7);
        result.put(32, 11);
        result.put(33, 8);
        result.put(34, 6);
        result.put(35, 6);
        result.put(36, 7);
        result.put(37, 11);
        result.put(38, 11);
        result.put(39, 11);
        result.put(40, 11);
        result.put(41, 11);
        result.put(42, 11);
        result.put(43, 11);
        result.put(44, 11);
        return Collections.unmodifiableMap(result);
    }
}
