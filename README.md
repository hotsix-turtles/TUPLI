# 🌷 TUPLI - 유튜브 콘텐츠 기반 SNS

## 💜 프로젝트 진행 기간
2022.01.10(월) ~ 2022.02.18(금) (39일간 진행)  
SSAFY 6기 2학기 공통프로젝트 - TUPLI

</br>

## 🎵 TUPLI - 설계 배경
콘텐츠의 범람이 일어나는 요즘 시대에서 여러분들은 유튜브를 보며 '이 영상, 나만보기 아깝다!' 또는, '나와 비슷한 취향을 가진 사람은 어떤 채널을 구독중일까?' 라는 생각을 해보신 적 있나요?

TUPLI는 그러한 니즈를 충족시켜주기 위해 탄생한 유튜브 콘텐츠 기반 SNS입니다. TUPLI와 함께라면, 유튜브 컨텐츠 공유를 손쉽게! 할 수 있습니다.

</br>

## 💜 TUPLI - 개요
*- 나의 유튜브 영상 취향을 공유하고 타인의 취향을 함께 즐기자 -*  

TUPLI는 You<span style="color:violet">TU</span>be <span style="color:violet">PL</span>ayl<span style="color:violet">I</span>st의 약자입니다.  
TUPLI는 '플레이리스트'를 만들고 영상 리스트를 업로드하여 취향을 공유하거나, '플레이룸'을 개설하고 다른 영상을 동시에 실시간으로 시청할 수 있는 기능을 제공하는 웹서비스입니다.  

타인과 취향을 공유한다는 점에서 사람들과 북적일 수 밖에 없는 환경임을 고려하여 <span style="color:violet">TUPLI</span>는 SNS 컨셉으로 개발되었습니다. 따라서, 타인과 교류하고 연을 맺기 쉽습니다. 또한, 열심히 컨텐츠를 공유하는 유저는 그만큼 다양한 유저들이 찾아와 줄 것이고, 많은 사람들의 인기를 받을 수 있을 것입니다.

</br>

## ✔ 시스템 구성도
---
# ![TUPLI 시스템 구성도](https://user-images.githubusercontent.com/55949647/153050025-a07ac67a-f684-44b3-a0ca-47c424cf6a7a.png)

</br>



</br>

## 💜 주요 기능
---
- 플레이리스트
    - 혼자보기 아쉬운 영상들을 친구들에게 공유하고 싶을 때.
    - 제목과 설명, 그리고 태그에 업로드한 동영상에 관심을 가질만한 키워드를 적어주세요.
    - 그러면 해당 분야에 관심이 있는 유저는 업로드된 플레이리스트를 통해 편하게 취향저격된 영상만을 감상할 수 있습니다.
    - 찾아온 유저는 미처 알지 못했던 유튜브 채널을 소개받을 수 있는 이점도 있습니다.
    - 나와 취향이 맞는 유저는 팔로우하고 소식을 공유해요.
- 플레이룸
    - 몸은 떨어져있어도 같이 영화를 보는 것처럼 플레이룸 내의 유저들은 동영상의 싱크를 공유합니다.
    - 같은 싱크의 동영상을 보면서 서로 느낀점과 생각을 실시간으로 나눌 수 있습니다.
    - 방장은 동영상 싱크를 조절할 수 있습니다. 
    - 친구들과 같이 웹드라마 정주행을 하거나, 실시간 축구경기를 같이 관람해보아요!   
- 게시글
    - 지금 하고있는 생각이나, 오늘의 계획 등을 자유롭게 작성해요.
    - 유저들은 게시글에 좋아요를 누르고, 덧글을 작성할 수 있어요.

</br>





## ✔ 개발환경
---

**Backend**
- IntelliJ IDE
- Springboot 2.6.3
- Spring Data JPA
- Spring Security
- Spring Validation
- Spring Web
- Spring Websocket
- QueryDSL
- Redis
- GCP 3.0.0
- Swagger 3.0.0
- Firebase 8.1.0
- MariaDB
- Flask

**Frontend**
- Visual Studio Code IDE
- Vue 2.6.11
- Vuetify 2.4.0
- Vuex 3.4.0
- Webstomp-Client 1.2.6
- Sock.js-Client 1.5.2
- Firebase 9.6.6
- sweetalrt2 11.3.10

**CI/CD**
- AWS EC2
- Jenkins
- NGINX
- SSL


## ✔ 협업 툴
---
- Git
- Notion
- Gether Town
- JIRA
- Slack
- MatterMost
- Webex

## ✔ 협업 환경
---
- Gitlab
  - 코드의 버전을 관리
  - 이슈 발행, 해결을 위한 토론
  - MR시, 팀원이 코드리뷰를 진행하고 피드백 게시
- JIRA
  - 매주 목표량을 설정하여 Sprint 진행
  - 업무의 할당량을 정하여 Story Point를 설정하고, In-Progress -> Done 순으로 작업  
- 회의
  - Gether Town 아침회의 진행, 전날 목표 달성량과 당일 할 업무 브리핑
  - 각자 위치에서 건네야 할 말이 생기면 팀원의 위치로 이동하여 전달
  - 빠른 소통과 신속한 대응이 가능하다.
- Notion
  - 회의가 있을때마다 회의록을 기록하여 보관
  - 회의가 길어지지 않도록 다음날 제시할 안건을 미리 기록
  - 기술확보 시, 다른 팀원들도 추후 따라할 수 있도록 보기 쉽게 작업 순서대로 정리
  - 컨벤션 정리
  - 간트차트 관리
  - 스토리보드, 스퀀스다이어그램, 기능명세서 등 모두가 공유해야 하는 문서 관리

## ✔ 컨벤션
---
- Gitlab
  - 기본
    - init 제외하고 **git add . 금지! (수정한 소스 파일만 add 해주세요) 
    - feature branch(ex. front-회원가입페이지)는 dev에 PR 처리 후 삭제
    - master,dev branch 부터는 CI/CD(Jenkins) 연결 후 자동 빌드
  - 브랜치 양식
    - master = latest release
    - dev
      - dev-front
      - dev-back
    - feature
      - feature-front/[feature name]
      - feature-back/[feature name]
        - ex. feature-back/chat
    - fix
      - fix/[issue num]
    - extra
      - extra
      - 기타 문서 수정 등 위에 해당하지 않는 경우

  - 커밋 양식

    - Core commit
      - feature
        - 지라이슈번호 #comment feature:backend:[메세지]
        - 지라이슈번호 #comment feature:frontend:[메세지]
    - fix
        - 지라이슈번호 #comment fix:backend:[메세지]
    - refactor
        - 같은 기능을 하는 코드를 리팩토링(재작성) 한 경우 (logic 변경 x)
        - 지라이슈번호 #comment refactor:수정한 파일
  - Additional Commit
      - docs
          - Markdown, Image 등 문서를 생성 혹은 수정한 경우
          - docs:간단한 설명
      - style
          - 중괄호, 세미콜론 위치 등의 간단한 변경 (logic 변경 x)
          - style:수정한 파일
      - test
          - 테스트를 추가, 변경 하는 경우 (production code 변경 x)
          - test:수정한 파일
      - chore
          - 기타 모든 잡무
          - 예를 들어, 설정파일(package.json, application.json 등)을 변경한 경우
          - chore:수정한 파일
- JIRA
  - 태스크 컨벤션
    - 머릿말에 [BE], [FE], [Design], [Extra]를 달아서 분류한다.

## ✔ ERD
---
### 초기 ERD를 ERD 클라우드에서 작성하였고, 현재는 DB를 해석하고 자동으로 ERD로 바꿔주는 MySQL 워크벤치를 이용하여 저희 서비스에서 사용되는 테이블을 ERD로 표현하였습니다.  

![TUPLI ERD](https://user-images.githubusercontent.com/55949647/153108823-f4c93aaa-ba38-4a0b-9cde-061fa51e72ab.png)

## ✔ 요구사항 정의서 (일부)
---
![요구사항 정의서 일부](https://user-images.githubusercontent.com/55949647/153079097-81b54cea-7715-4362-969d-214eb1312971.png)


## ✔ 화면 정의서
---
![TUPLI 화면 정의서](https://user-images.githubusercontent.com/55949647/153079263-597bf907-5828-42fd-9d9e-aeeec06db5f4.png)


## ✔ 시퀀스 다이어그램 (일부)
---
### 처음부터 프로젝트 진행중 고도화 하여 변경된것까지 총 32개의 시퀀스 다이어그램을 작성하였고, 그 중 끝까지 고도화한 18개의 시퀀스 다이어그램을 업로드하였습니다.  

이메일 로그인
![1  이메일 로그인](https://user-images.githubusercontent.com/55949647/153079417-40119a73-9276-4a6a-a259-3e72f59c0383.png)

취향조사
![5  취향조사](https://user-images.githubusercontent.com/55949647/153079498-3f990815-7143-454f-b913-6b5a0d7eeebd.png)


## ✔ 팀원 역할 분배
---
![팀 역할 배분](https://user-images.githubusercontent.com/55949647/153080335-02cacda4-c000-4f0b-9002-a5f392924aaf.png)

## 🎵 TUPLI 서비스 화면 (구동 화면 + 목업)
---
</br>

## 메인페이지
<!-- 랜드 페이지 (긴버전) -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056233-c45f3d6a-9d76-415d-96f0-a63935ab26be.png"/>  

</br>

## 메인페이지 (구동 화면)
<!-- 랜드 페이지 (실제구동사진) -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153110285-1f375620-6208-4845-9de4-d3f01ec6f0dc.png"/>  

</br>

## 글 생성
<!-- 만들기 버튼 눌렀을 때 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056232-974b197b-a107-4a6e-ba29-15d3f0082e82.png"/>  

</br>

## 플레이리스트 만들기
<!-- 내 플레이리스트 만들기 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153053806-9eda2198-5f5f-4c9c-8dce-b64a2feefe9c.png"/>  

</br>

## 플레이룸 만들기
<!-- 플레이룸 만들기 화면 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153053845-1553033b-e11a-45a0-9972-73c396f832c6.png"/>  

</br>

## 플레이리스트 접속
<!-- 플레이리스트 접속시 보이는 화면 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153053811-cd05445d-dff5-48e5-b212-83e253704bef.png"/>  

</br>

## 플레이룸 접속

<!-- 플레이룸 화면 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056248-355f9252-bea8-46b2-99ad-3d5f112727d9.png"/>  

</br>

## 유저 프로필 탐색
<!-- 타 프로필 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056243-01e527ba-1819-47f9-8dfc-4ff80f821280.png"/>  

</br>

## 알림
<!-- 알림 리스트 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056240-5395dcf9-83ae-49b0-a67c-03c903c138ac.png"/>  

</br>

## 실시간 검색어 트렌드
<!-- 실시간 검색어 트렌드 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056221-5f2c854b-35de-46a5-a23b-900e8f803efd.png"/>  

<br>

## 검색하기
<!-- 검색 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056227-0effffdd-bad7-4036-a292-f5e54a68d3d0.png"/>  

</br>

## 검색하기 (구동 화면)
<!-- 검색 (구동화면)-->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153110546-f23031e1-dd10-444c-8b80-344e2ec89785.png"/> 

</br>

## 검색하기 무한스크롤 (구동 화면)
<!-- 검색 무한스크롤 (구동화면)-->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153110752-0668e001-b06e-4d39-9ebf-f5681edd7472.png"/> 

</br>

## 플레이룸 내부 채팅방
<!-- 플레이룸 채팅창 -->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153056252-42f17a7f-d0ac-4259-b3c9-920969c6cadb.png"/>  

</br>

## 둘러보기 페이지 - 핫 키워드 노출 (구동 화면)
<!-- 둘러보기 페이지 (구동 화면)-->
<img width="45%" src="https://user-images.githubusercontent.com/55949647/153110906-186cb211-42b1-4140-9099-1da0f80b52f4.png"/>  

</br>

## 플레이룸 내부 채팅방 (가로화면)
<!-- 플레이룸 채팅창(가로버전) -->
<img width="100%" src="https://user-images.githubusercontent.com/55949647/153056258-c035ea31-ae68-47bd-9520-83f1fc3c848c.png"/>  

</br>




