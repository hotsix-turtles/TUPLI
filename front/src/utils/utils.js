import SERVER from '@/api/server'

function timeConverter(UNIX_timestamp){
  const today = new Date();
  const timeValue = new Date(UNIX_timestamp * 1000);

  const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
  if (betweenTime < 1) return '방금전';
  if (betweenTime < 60) {
    return `${betweenTime}분전`;
  }

  const betweenTimeHour = Math.floor(betweenTime / 60);
  if (betweenTimeHour < 24) {
    return `${betweenTimeHour}시간전`;
  }

  const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
  if (betweenTimeDay < 30) {
    return `${betweenTimeDay}일전`;
  }

  var a = new Date(UNIX_timestamp * 1000);
  var year = a.getFullYear();
  var month = a.getMonth() + 1;
  var date = a.getDate();
  var hour = a.getHours();
  var min = a.getMinutes();
  var time = year + '년 ' + month + '월 ' + date + '일 ' + hour + ':' + min ;
  return time;
}

function playtimeConverter(UNIX_timestamp){
  var a = new Date(UNIX_timestamp * 1000);
  var hour = a.getHours();
  var min = a.getMinutes();
  var time = hour + ':' + min ;
  return time;
}


function HMS(input, type){
  let index = input.indexOf(type);

  if(index < 0 & type != 'H'){
    return "00"; // 들어오는 값이 없는 경우 index가 -1 , H를 00: 으로 표시하고 싶으면 뒤에 유닛값조건만 지우면 된다.
  }
  if(isNaN(input.charAt(index-2))){ //해당 유닛의 인덱스 2번째앞이 숫자인지 확인
    if (type == 'H') { // H이고 한자리 숫자인경우 한자리 숫자만 반환
      return input.charAt(index-1);
    } else{
      return '0' + input.charAt(index-1);} // 숫자 아닌 경우에는 0을 붙인걸 반환
  }else{
    return input.charAt(index-2) + input.charAt(index-1); // 숫자인 경우에는 합쳐서 반환
  }
}

function DurationChange(input){
  let H = HMS(input, 'H');
  let M = HMS(input, 'M');
  let S = HMS(input, 'S');

  if (H) { //H가 들어있는 경우에는 : 더하기
    H += ':'
  } else
  {
    if (M=='00') { // H없고 M이 00 인 경우 0으로 변환
      M='0'
    }
  }
  return H  + M + ':' + S ;
}

function getImage(image) {
  // 내용물 비어있으면 startsWith 작동 안함
  if (image == null || image == "") {
    return require(`@/assets/profile_basic.jpg`)
  // OAUTH 유저등의 풀 링크 사진
  } else if (image.startsWith('http')) {
    return image
  // 일반 유저 등의 GCP 업로드 사진
  } else if (image.startsWith('/')) {
    return SERVER.ROUTES.image + image
  // 일반 가입시 기본 부여 랜덤 사진
  } else if (image.startsWith('#')) {
    if (image == '#1') {
      return require(`@/assets/profile/profile_01.png`)
    } else if (image == '#2') {
      return require(`@/assets/profile/profile_02.png`)
    } else if (image == '#3') {
      return require(`@/assets/profile/profile_03.png`)
    } else if (image == '#4') {
      return require(`@/assets/profile/profile_04.png`)
    } else if (image == '#5') {
      return require(`@/assets/profile/profile_05.png`)
    } else if (image == '#6') {
      return require(`@/assets/profile/profile_06.png`)
    } else if (image == '#7') {
      return require(`@/assets/profile/profile_07.png`)
    } else if (image == '#8') {
      return require(`@/assets/profile/profile_08.png`)
    } else if (image == '#9') {
      return require(`@/assets/profile/profile_09.png`)
    } else if (image == '#10') {
      return require(`@/assets/profile/profile_10.png`)
    } else if (image == '#11') {
      return require(`@/assets/profile/profile_11.png`)
    } else if (image == '#12') {
      return require(`@/assets/profile/profile_12.png`)
    } else if (image == '#13') {
      return require(`@/assets/profile/profile_13.png`)
    } else if (image == '#14') {
      return require(`@/assets/profile/profile_14.png`)
    } else if (image == '#15') {
      return require(`@/assets/profile/profile_15.png`)
    } else if (image == '#16') {
      return require(`@/assets/profile/profile_16.png`)
    } else if (image == '#17') {
      return require(`@/assets/profile/profile_17.png`)
    } else if (image == '#18') {
      return require(`@/assets/profile/profile_18.png`)
    } else if (image == '#19') {
      return require(`@/assets/profile/profile_19.png`)
    } else if (image == '#20') {
      return require(`@/assets/profile/profile_20.png`)
    } else if (image == '#21') {
      return require(`@/assets/profile/profile_21.png`)
    } else {
      return require(`@/assets/profile_basic.jpg`)
    }
  // 이 와중에 없으면 일단 기본 사진
  } else {
    return require(`@/assets/profile_basic.jpg`)
  }
}

export { timeConverter, playtimeConverter, DurationChange, getImage }
