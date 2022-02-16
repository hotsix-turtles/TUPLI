# 🌷 TUPLI - 유튜브 콘텐츠 기반 SNS

![타이틀이미지](https://user-images.githubusercontent.com/55949647/154088644-8ae7ec32-04bb-4fc3-810a-c2111ec2afb8.png)


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

TUPLI는 You<span style="color:violet">TU</span>be <span style="color:violet">PL</span>ayl<span style="color:violet">I</span>st의 약자입니다.  
TUPLI는 '플레이리스트'를 만들고 영상 리스트를 업로드하여 취향을 공유하거나, '플레이룸'을 개설하고 다른 영상을 동시에 실시간으로 시청할 수 있는 기능을 제공하는 웹서비스입니다.  

타인과 취향을 공유한다는 점에서 사람들과 북적일 수 밖에 없는 환경임을 고려하여 <span style="color:violet">TUPLI</span>는 SNS 컨셉으로 개발되었습니다. 따라서, 타인과 교류하고 연을 맺기 쉽습니다. 또한, 열심히 컨텐츠를 공유하는 유저는 그만큼 다양한 유저들이 찾아와 줄 것이고, 많은 사람들의 인기를 받을 수 있을 것입니다.

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
- sweetalrt2 11.3.10

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


## ✔ 팀원 역할 분배 $$$$$
---
![팀 역할 배분](https://user-images.githubusercontent.com/55949647/153080335-02cacda4-c000-4f0b-9002-a5f392924aaf.png)

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


## 🎵 TUPLI 서비스 화면 ($$$$$ GIF 앞으로 촬영해서 넣자.)
---
</br>
