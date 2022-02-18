# 🌷 TUPLI - 유튜브 콘텐츠 기반 SNS

![타이틀이미지](https://user-images.githubusercontent.com/55949647/154088644-8ae7ec32-04bb-4fc3-810a-c2111ec2afb8.png)


## TUPLI 링크 : [https://tupli.kr](https://tupli.kr/api/v1/swagger-ui/index.html#/)
## 소개 영상 보기 : [UCC 링크](https://youtu.be/WU3tIIOS0Ec)

## 💜 프로젝트 진행 기간
2022.01.10(월) ~ 2022.02.18(금) (39일간 진행)  
SSAFY 6기 2학기 공통프로젝트 - TUPLI

</br>

## 🎵 TUPLI - 배경
콘텐츠의 범람이 일어나는 요즘 시대에서 여러분들은 유튜브를 보며 '이 영상, 나만보기 아깝다!' 또는, '나와 비슷한 취향을 가진 사람은 어떤 채널을 구독중일까?' 라는 생각을 해보신 적 있나요?

TUPLI는 그러한 니즈를 충족시켜주기 위해 탄생한 유튜브 콘텐츠 기반 SNS입니다. TUPLI와 함께라면, 유튜브 컨텐츠 공유를 손쉽게! 할 수 있습니다.

</br>

## 💜 TUPLI - 개요
*- 나의 유튜브 영상 취향을 공유하고 타인의 취향을 함께 즐기자 -*  

TUPLI는 you<span style="color:violet">TU</span>be <span style="color:violet">PL</span>ayl<span style="color:violet">I</span>st의 약자입니다.  
TUPLI는 '플레이리스트'를 만들고 영상 리스트를 업로드하여 취향을 공유하거나, '플레이룸'을 개설하고 다른 영상을 동시에 실시간으로 시청할 수 있는 기능을 제공하는 웹서비스입니다.  

타인과 취향을 공유한다는 점에서 사람들과 북적일 수 밖에 없는 환경임을 고려하여 <span style="color:violet">TUPLI</span>는 SNS 컨셉으로 개발되었습니다. 따라서, 타인과 교류하고 연을 맺기 쉽습니다. 또한, 열심히 컨텐츠를 공유하는 유저는 그만큼 다양한 유저들이 찾아와 줄 것이고, 많은 사람들의 인기를 받을 수 있을 것입니다.

</br>

## 💜 주요 기능
---
- ### 플레이리스트
    - 혼자보기 아쉬운 영상들을 친구들에게 공유하고 싶을 때.
    - 제목과 설명, 그리고 태그에 업로드한 동영상에 관심을 가질만한 키워드를 적어주세요.
    - 그러면 해당 분야에 관심이 있는 유저는 업로드된 플레이리스트를 통해 편하게 취향저격된 영상만을 감상할 수 있습니다.
    - 찾아온 유저는 미처 알지 못했던 유튜브 채널을 소개받을 수 있는 이점도 있습니다.
    - 나와 취향이 맞는 유저는 팔로우하고 소식을 공유해요.
    <br/>
- ### 플레이룸
    - 몸은 떨어져있어도 같이 영화를 보는 것처럼 플레이룸 내의 유저들은 동영상의 싱크를 공유합니다.
    - 같은 싱크의 동영상을 보면서 서로 느낀점과 생각을 실시간으로 나눌 수 있습니다.
    - 방장은 동영상 싱크를 조절할 수 있습니다. 
    - 친구들과 같이 웹드라마 정주행을 하거나, 실시간 축구경기를 같이 관람해보아요!   
    <br/>
- ### 게시글
    - 지금 하고있는 생각이나, 오늘의 계획 등을 자유롭게 작성해요.
    - 참여했던 플레이룸이나, 관람한 플레이리스트에 대한 생각을 게시글에 남겨보아요.
    - 유저들은 게시글에 좋아요를 누르고, 덧글을 작성할 수 있어요.
    <br/>
- ### 뱃지획득
    - 팔로워가 많을수록, 플레이룸 참여 횟수가 많을수록, 작성한 덧글 수가 많을수록,
    - 획득할 수 있는 뱃지의 색이 변합니다!
    - 이 외에도 다양한 뱃지가 존재하니, 탐구하고 참여하여 TUPLI에 업적을 남겨보세요!
    <br/>
- ### 팔로우
    - 취향이 같은 유저를 팔로우해서 이후에도 계속해서 영상을 공유받을 수 있어요.
    - 팔로우 맺은 친구를 플레이룸에 초대해서 영상을 같이 시청해볼까요?
</br>

## ✔ 주요 기술
---

**Backend - Spring**
- IntelliJ IDE
- Springboot 2.6.3
- Spring Data JPA
- Spring Security
- Spring Validation
- Spring Web
- QueryDSL
- WebSocket
- Redis
- GCP 3.0.0
- Swagger 3.0.0
- Firebase 8.1.0
- MariaDB

**Backend - Flask**
- Scikit-learn
- SQLAlchemy

**Frontend**
- Visual Studio Code IDE
- Vue 2.6.11
- Vuetify 2.4.0
- Vuex 3.4.0
- Webstomp-Client 1.2.6
- Sock.js-Client 1.5.2
- Firebase 9.6.6
- sweetalert2 11.3.10

**CI/CD**
- AWS EC2
- Jenkins
- NGINX
- SSL

## ✔ 프로젝트 파일 구조
---
### Back
```
tupli
  ├── api
  │   └── dev
  ├── config
  │   ├── auth
  │   ├── filter
  │   ├── interceptor
  │   └── listener
  ├── controller
  ├── dto
  │   └── simple
  ├── entity
  ├── info(OAuth)
  ├── repository
  ├── scheduler
  ├── service
  └── utils
```
### Front
```
tupli-front
  ├── node_modules
  ├── public
  └── src
      ├── api
      ├── assets
      ├── components
      │   ├── account
      │   ├── alarm
      │   ├── board
      │   ├── common
      │   ├── home
      │   ├── notice
      │   ├── playlist
      │   ├── playroom
      │   ├── profile
      │   └── video
      ├── configs
      ├── plugins
      ├── router
      ├── scss
      ├── store
      │   └── modules
      ├── utils
      └── views
          ├── account
          ├── board
          ├── common
          ├── handler
          ├── home
          ├── playlist
          ├── playroom
          ├── profile
          └── video
```


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


## ✔ 팀원 역할 분배
---
![역할 배분](https://user-images.githubusercontent.com/55949647/154541550-b52932b4-9e3d-4788-9994-98dd7fe8c875.png)

## ✔ 프로젝트 산출물
---
- [기능명세서](./docs/기능명세서.md)
- [디자인&컨셉기획](./docs/디자인&컨셉기획.md)
- [스토리보드](./docs/스토리보드.md)
- [시퀀스다이어그램](./docs/시퀀스다이어그램.md)
- [아키텍처](./docs/아키텍처.md)
- [와이어프레임](./docs/와이어프레임.md)
- [컨벤션](./docs/컨벤션.md)
- [API](./docs/API.md)
- [ERD](./docs/ERD.md)
- [회의록](./docs/회의록.md)
- [테스트케이스](./docs/TUPLI_테스트케이스.xlsx)
- [명세기술서](./docs/TUPLI_명세_기술서.docx)



## 🎵 TUPLI 서비스 화면
---

### 회원가입 절차
- 이용약관에 동의하지 않으면 진행할 수 없습니다.
- 아이디(이메일)은 타 사용자의 아이디와 중복될 수 없습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587535-8e1656c9-53fa-433e-ac22-efc1963b21a7.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587545-22a4a70f-8cac-4772-a6e5-29c55ff0409b.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587551-b31f46b9-8f39-42ed-8c38-282734224762.gif"/>

<br>

### 메인화면
- 최신순으로 업로드된 플레이리스트 or 플레이룸 or 게시글 정보를 로드합니다.
- 무한스크롤이 적용됩니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587375-cfef4162-d404-41fd-9d28-39712f2cf5b1.gif"/>

<br>

### 플레이리스트 생성
- 제목, 소개글, 태그를 작성합니다.
- 플레이리스트에 담을 영상을 검색해서 추가할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587487-fc0e71b1-507c-4c66-b906-d583f530f139.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587499-da47e90b-c9ae-478f-8942-c4afde9aa4b2.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587504-8e596650-49dd-4ffc-81ae-a2e384527fdc.gif"/>

<br>

### 플레이리스트 이용하기
- 업로드된 플레이리스트로 이동하여 영상을 시청합니다.
- 플레이리스트에 좋아요와 댓글을 남길 수 있습니다.
- 해당 플레이리스트의 정보로 플레이룸을 만들 수 있습니다.
- 업로드한 당사자는 플레이리스트를 삭제하거나 수정할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587515-0ebaf6f4-a87f-474c-946c-dee20bed0476.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587529-b2be3b77-4b18-48d8-910e-89235409fc82.gif"/>

<br>

### 프로필 탐색
- 자신의 프로필 정보 페이지에서 팔로워와 팔로잉한 사용자 목록을 볼 수 있습니다.
- 타인의 프로필으로 이동하여 좋아요 할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587432-39f947e4-988d-47e7-adb4-dedcfbb95c1a.gif"/>

<br>

### 실시간 알림
- 타인이 자신에게 팔로우를 걸었을 때 튜플리를 이용중이라면 실시간 알림을 받을 수 있습니다.
- 플레이룸에 초대되었을 때에도 실시간 알림을 받습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587392-41092786-b6c9-4fcd-a3e8-bf476519d5f5.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587394-02371e17-420f-42cf-9d99-e57e3280465e.gif"/>

<br>

### 플레이룸 생성
- 플레이룸을 생성하기 위해 기존에 업로드된 플레이리스트 정보를 이용할 수 있습니다.
- 팔로우 한 친구를 초대하고, 플레이룸을 운영할 시간, 공개 여부, 영상 목록 셔플 여부, 최대 참여자 수를 설정합니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587441-5bf6a5bf-1bf0-4910-95c8-089e75ed4817.gif"/>

<br>

### 플레이룸 이용하기
- 현재 운영중인 플레이룸은 좌측상단에 ONPLAY라고 써있습니다.
- 참여한 이용자들은 방장이 보는 영상 싱크에 맞춰집니다.
- 플레이룸에 접속하여 좋아요 표시를 할 수 있고, 이용자들과 채팅을 할 수 있습니다.
- 플레이룸을 카카오톡 톡방에 공유할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587451-cd504c78-5147-45df-881d-b8d45e766b26.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587467-401548d1-4ef2-4d16-adc6-abb7e97a47de.gif"/>

<br>

### 알림 목록 이용하기
- 팔로워에 대한 소식, 팔로우 소식 등이 알림 목록에 실시간으로 저장됩니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587397-120e0308-8a4b-4c65-977e-e0991ed5d820.gif"/>

<br>

### 게시글 작성
- 게시글을 작성할 때 플레이리스트와 플레이룸을 엮어서 업로드할 수 있습니다.
- 아무것도 엮지 않으면 입력한 내용만 업로드됩니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587371-0aff4235-5232-48d5-9b67-2354dcabd4d0.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587373-100a85f0-bbcd-47e6-b2ce-4f23d6817c42.gif"/>

<br>

### 메인화면 이용하기
- 메인화면에서 자유롭게 타인이 업로드한 컨텐츠를 탐색할 수 있습니다.
- 그러다가, 맘에 드는 컨텐츠를 업로드하는 사용자를 발견하면 접속하여 활동 내역을 살펴볼 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587377-2da34425-f76d-4c34-90de-f11606d0b60b.gif"/>

<br>

### 탐색하기
- 플레이리스트, 플레이룸, 영상 각각에 대하여 전체 목록을 살펴볼 수 있습니다.
- 현재 유저의 취향에 따라 키워드 버튼이 변화합니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587399-6ca3be70-4f6f-4107-b879-de3bddeb2f56.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587407-4e64cd20-2ed4-4f8c-9ca9-d51d7d7162b2.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587417-01f10449-b4e7-42aa-bd8b-acd5444d4092.gif"/>

<br>

### 검색
- 현재 실시간 검색어 트렌드가 Top 10위까지 노출됩니다. 클릭하면 검색결과로 이동합니다.
- 플레이리스트, 플레이룸, 유저닉네임, 영상제목을 검색할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587561-55048bc1-4649-4bb4-a6b3-468d01f3e9e7.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587364-f62396c2-a221-4424-a6ce-f2ef6b9362b9.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587565-4bfb22ef-621b-4df8-b7b7-bd73d324f50d.gif"/>

<br>

### 자신의 프로필 정보
- 자신의 프로필 화면에서 활동 내역과, 취향 분석 결과 그리고 획득한 뱃지를 확인할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587436-8d94a81a-b287-4d59-99a6-dca905eaf5b5.gif"/>

<br>

### 프로필 정보 변경
- 설정 탭에서 비밀번호를 변경할 수 있습니다.
- 프로필 정보 페이지에서 프로필이미지, 닉네임, 자기소개 글을 변경할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587378-146a0843-c95e-4323-a53e-ea3157a846ee.gif"/>
<img width="30%" src="https://user-images.githubusercontent.com/55949647/154591542-7efa6cf5-ff54-40d1-8ed8-1d2682c30ee4.gif"/>

<br>

### 좋아요 표시한 글 보기
- 설정 페이지에서 좋아요한 플레이리스트, 플레이룸, 게시글 목록을 확인할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587382-e9dcdaf1-e5db-4414-95e1-1230f8f08486.gif"/>

<br>

### 알림관련 설정, 이용약관
- 실시간 알림이 오는 것이 싫다면 수신 거부를 할 수 있습니다.
- TUPLI 서비스 개인정보 이용약관, 그리고 서비스 이용약관을 확인할 수 있습니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587385-b06c3afe-c06e-4d9c-8412-60c59e94540d.gif"/>

<br>

### 로그아웃
- 로그아웃하면 비회원(게스트)로 접속하게됩니다.
- 비회원 사용자는 좋아요, 댓글, 채팅 등 여러 기능이 제한됩니다.

<img width="30%" src="https://user-images.githubusercontent.com/55949647/154587391-bb7fe4c1-bb9b-4c9f-a6bd-6dba13daadde.gif"/>
