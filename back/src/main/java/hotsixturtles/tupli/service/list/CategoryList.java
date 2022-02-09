package hotsixturtles.tupli.service.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CategoryList {

    public static final Map<Integer, String> CATEGORY_LIST = crateCategoryList();

    private static Map<Integer, String> crateCategoryList() {
        Map<Integer, String> result = new HashMap<>();
        result.put(1, "영화/드라마");
        result.put(2, "기타");
        result.put(10, "음악");
        result.put(15, "동물");
        result.put(17, "스포츠");
        result.put(18, "영화/드라마");
        result.put(19, "여행");
        result.put(20, "게임");
        result.put(21, "일상");
        result.put(22, "일상");
        result.put(23, "엔터테인먼트");
        result.put(24, "엔터테인먼트");
        result.put(25, "교육/시사");
        result.put(26, "노하우/스타일");
        result.put(27, "교육/시사");
        result.put(28, "교육/시사");
        result.put(29, "기타");
        result.put(30, "영화/드라마");
        result.put(31, "영화/드라마");
        result.put(32, "기타");
        result.put(33, "음악");
        result.put(34, "엔터테인먼트");
        result.put(35, "엔터테인먼트");
        result.put(36, "영화/드라마");
        result.put(37, "기타");
        result.put(38, "기타");
        result.put(39, "기타");
        result.put(40, "기타");
        result.put(41, "기타");
        result.put(42, "기타");
        result.put(43, "기타");
        result.put(44, "기타");
        return Collections.unmodifiableMap(result);
    }
}
