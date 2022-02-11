
function timeConverter(UNIX_timestamp){
  var a = new Date(UNIX_timestamp * 1000);
  var year = a.getFullYear();
  var month = a.getMonth();
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

export { timeConverter, playtimeConverter, DurationChange }
