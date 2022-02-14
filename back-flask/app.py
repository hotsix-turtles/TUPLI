from flask import Flask, jsonify
# ORM
from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String
# 환경변수
import os
from dotenv import load_dotenv
# json to dictionary
import json
# sklearn 머신러닝 라이브러리
from sklearn.feature_extraction import DictVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__) 

'''
DB 관련 설정 + ENV 샘플 동봉, 원래는 gitgnore
'''
# Declare connection
# mysql_url = "mysql+pymysql://root:1234@localhost:3306/turtletube_test?charset=utf8"
load_dotenv()
mysql_url = "mysql+pymysql://" + os.environ.get('DB_USER') + ":"+ os.environ.get('DB_PASS') + "@" + os.environ.get('DB_URL') +"?charset=utf8"
engine = create_engine(mysql_url, echo=True, convert_unicode=True)
# Declare & create Session
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
# Create SqlAlchemy Base Instance
Base = declarative_base()
Base.query = db_session.query_property()

def init_database():
    Base.metadata.create_all(bind=engine)

'''
DB 시작/ 종료 설정
'''
@app.before_first_request
def beforeFirstRequest():
    init_database()

@app.teardown_appcontext
def teardownContext(exception):
    db_session.remove()

'''
DTO(Model 아님!) 설정 
'''
class Playlist(Base):
    __tablename__ = 'playlist'

    id = Column(Integer, primary_key=True)
    # user_id = Column(Integer)
    playlist_info = Column(String)
    recommend_playlists = Column(String)

'''
실제 작업
'''
# 해당 유저의 정보 확인, 리스트 반환
@app.route("/db/<tid>")
def db_conn(tid):
    # 플레이 리스트 3만건 기준 6초 정도 소요
    # 필요할 경우 대분류(type)별 전처리
    # 원래는 여기서 대분류나 타입 분류 (Spring 맞춰서 유저 번호 1번 세팅 해둠)
    # playlists = Playlist.query.filter(Playlist.user_id == 1)[:999]  
    playlists = Playlist.query.all()

    playlists_list = list()
    playlists_num = list()
    for idx, playlist in enumerate(playlists):
        tmp_dict = json.loads(playlist.playlist_info)
        playlists_list.append(tmp_dict)
        playlists_num.append(playlist.id)
        # 나의 번호 기록
        if playlist.id == int(tid):
            request_num = idx

    # 데이터 분석
    vectorizer = DictVectorizer(sparse=False)
    playlists_v = vectorizer.fit_transform(playlists_list)
    cos_sim = cosine_similarity(playlists_v, playlists_v)
    # 해당 유저 내의 순위, 본인 제외 정렬,
    sim_scores = list(enumerate(cos_sim[request_num]))  
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)  # 코사인 유사성 순으로 정렬
    sim_scores = sim_scores[:12]  # 넉넉히 한 11개 슬라이스 (본인 제외 될 준비)
    ranking = [i[0] for i in sim_scores]  # 우선 순위
    # 결과 적재 후 반환
    response = list()
    for rank in ranking:
        # 본인 제외
        if playlists_num[rank] != int(tid):
            response.append(playlists_num[rank])
    print('calc for', tid, 'finished:', response)

    return jsonify(response)


# 모든 플레이리스트의 추천 재계산 (개발자용, 엔드포인트 공개 금지)
@app.route("/playlist/renew/all")
def playlist_recommend_renew_all():
    playlists = Playlist.query.all()
    playlists_list = list()
    playlists_num = list()
    request_nums = list()
    for idx, playlist in enumerate(playlists):
        tmp_dict = json.loads(playlist.playlist_info)
        playlists_list.append(tmp_dict)
        playlists_num.append(playlist.id)
        request_nums.append(idx)

    # 데이터 분석
    vectorizer = DictVectorizer(sparse=False)
    playlists_v = vectorizer.fit_transform(playlists_list)
    cos_sim = cosine_similarity(playlists_v, playlists_v)
    # 모든 플레이리스트 정렬
    for request_num in request_nums:
        sim_scores = list(enumerate(cos_sim[request_num]))  
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)  # 코사인 유사성 순으로 정렬
        sim_scores = sim_scores[:12]  # 넉넉히 한 11개 슬라이스 (본인 제외 될 준비)
        ranking = [i[0] for i in sim_scores]  # 우선 순위
        # 결과 적재 후 반환
        response = list()        
        for rank in ranking:
            # 본인 제외
            if playlists_num[rank] != int(playlists_num[request_num]):
                response.append(playlists_num[rank])
        # 결과 일단 출력
        print('answer', playlists_num[request_num] , end=" : ")
        print(response)
        # 결과 각각 갱신
        playlist_update = Playlist.query.filter(Playlist.id == playlists_num[request_num]).first()
        playlist_update.recommend_playlists = json.dumps(response)
    db_session.commit()

    return "we've done a great job!";    

# 작동 확인용
@app.route("/") 
def hello():
    return "Hello World!"

if __name__ == "__main__" :
    # app.run(host='127.0.0.1', port=8080, debug=True)
    app.run(host='0.0.0.0')

