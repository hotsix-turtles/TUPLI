const playroom = {
    namespaced: true,
    state: {
      roomTitle: '3일만에 다이어트 포기 선언하게 만든 영상들',
      roomPublic: false,
      roomAuthorProfilePic: 'https://www.naver.com',
      roomAuthorName: '춘식이',
      roomAuthorFollowerCount: 456,
      roomStartTime: new Date(2022, 1, 23, 18, 30),
      roomEndTime: new Date(2022, 1, 23, 20, 30),
      roomContent: '같이 치맥하면서 먹방 보실분들?\r\n같이 치맥하면서 먹방 보시분들?\r\n같이 치맥하면서 먹방 보시분들?\r\n'.replace(/\r?\n/, '\n'),
      roomTags: ['먹방', '쯔양', '고기먹방', '고기먹방', '고기먹방', '고기먹방', '고기먹방'],
      roomPlaylists: [ '0005', '0010' ],
      roomCurrentPlaylist: '0005',
      roomVideos: [ '0010', '0011', '0013' ],
      roomCurrentVideo: '0013',
    },
    mutations: {},
    actions: {},
    getters: {
      roomPlayTime: ( {roomStartTime, roomEndTime} ) => `${roomStartTime.getHours()}:${roomStartTime.getMinutes()} - ${roomEndTime.getHours()}:${roomEndTime.getMinutes()}`,
      roomPublicLabel: ( {roomPublic} ) => roomPublic ? '공개' : '비공개',
      roomReducedContent: ( {roomContent} ) => roomContent.split(/\r?\n/).slice(0, 2).join('\n')
    }
  };
   
export default playroom;