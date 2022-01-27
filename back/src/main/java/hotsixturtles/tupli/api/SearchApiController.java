package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.params.PlayroomSearchCondition;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SearchApiController {

    private final SearchService searchService;

    // 일단 keyword, email, order 및 size 조건 가져오기
    // 좋아요 순 정렬 시 userdto 사용 or simpleuserdto에 좋아요 갯수 추가

    /**
     * 유저 검색
     * @param keyword
     * @param email
     * @param pageable : 예시 => {sort=email,desc ...} size, page 값 따로 넘길 수 있음
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/account/search")
    public ResponseEntity<?> searchUser(@RequestParam String keyword, @RequestParam @Nullable String email ,
                                        @PageableDefault(size = 20, sort ="nickname",  direction = Sort.Direction.ASC) Pageable pageable ){
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        if(email != null ) userSearchCondition.setEmail(email);
        userSearchCondition.setKeyword(keyword);
        List<User> userList = searchService.searchUser(userSearchCondition, pageable);
        List<SimpleUserDto> result = userList.stream().map(b -> new SimpleUserDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 플레이룸 검색
     * @param keyword 제목 // 검색에 필요한 기준 추가가능 (parameter 추가)
     * @param pageable : 예시 => {sort=email,desc ...} size, page 값 따로 넘길 수 있음
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playroom/search")
    public ResponseEntity<?> searchPlayroom(@RequestParam String keyword,
                                        @PageableDefault(size = 20, sort ="roomTitle",  direction = Sort.Direction.ASC) Pageable pageable ){
        PlayroomSearchCondition playroomSearchCondition = new PlayroomSearchCondition();
        playroomSearchCondition.setKeyword(keyword);
        List<Playroom> playroomList = searchService.searchPlayroom(playroomSearchCondition, pageable);
        List<PlayroomDto> result = playroomList.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

}
