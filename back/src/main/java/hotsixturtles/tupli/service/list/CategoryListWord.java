package hotsixturtles.tupli.service.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CategoryListWord {

    public static final Map<String, String> CATEGORY_LIST_WORD = createCategoryListWord();

    private static Map<String, String> createCategoryListWord() {
        Map<String, String> result = new HashMap<>();
        result.put("trip", "여행");
        result.put("game", "게임");
        result.put("life", "일상");
        result.put("style", "노하우/스타일");
        result.put("animal", "동물");
        result.put("entertainment", "엔터테인먼트");
        result.put("movie", "영화/드라마");
        result.put("music", "음악");
        result.put("education", "교육/시사");
        result.put("sports", "스포츠");
        result.put("etc", "기타");
        return Collections.unmodifiableMap(result);
    }

}
