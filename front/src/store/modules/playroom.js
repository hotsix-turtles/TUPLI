const playroom = {
  namespaced: true,
  state: {
    roomTitle: '3일만에 다이어트 포기 선언하게 만든 영상들',
    roomPublic: false,
    roomAuthorProfilePic: 'https://picsum.photos/100/100',
    roomAuthorName: '춘식이',
    roomAuthorFollowerCount: 456,
    roomStartTime: new Date(2022, 1, 23, 18, 30),
    roomEndTime: new Date(2022, 1, 23, 20, 30),
    roomContent: '같이 치맥하면서 먹방 보실분들?\r\n같이 치맥하면서 먹방 보시분들?\r\n같이 치맥하면서 먹방 보시분들?\r\n'.replace(/\r?\n/, '\n'),
    roomTags: ['먹방', '쯔양', '고기먹방', '고기먹방1', '고기먹방2', '고기먹방3', '고기먹방4', ],
    roomPlaylists: [ 1, 2, 3, 4, 5 ],
    roomCurrentPlaylist: '0005',
    roomVideos: [ 1, 2, 3, 4, 5, 6, 7, 8, 9 ],
    roomCurrentVideo: '0013',
    roomCurrentPlayTime: '01:30'
  },
  mutations: {},
  actions: {
  },
  getters: {
    roomPlayTime: ( {roomStartTime, roomEndTime} ) => `${roomStartTime.getHours()}:${roomStartTime.getMinutes()} - ${roomEndTime.getHours()}:${roomEndTime.getMinutes()}`,
    roomPublicLabel: ( {roomPublic} ) => roomPublic ? '공개' : '비공개',
    roomReducedContent: ( {roomContent} ) => roomContent.split(/\r?\n/).slice(0, 2).join('\n'),
    roomPlaylistItems: ( {roomPlaylists} ) => roomPlaylists.map((v) => {
      const playlistItems = {
        1: { id: 1, thumbnail_url: 'https://picsum.photos/100/101' },
        2: { id: 2, thumbnail_url: 'https://picsum.photos/100/102' },
        3: { id: 3, thumbnail_url: 'https://picsum.photos/100/103' },
        4: { id: 4, thumbnail_url: 'https://picsum.photos/100/104' },
        5: { id: 5, thumbnail_url: 'https://picsum.photos/100/105' },
        6: { id: 6, thumbnail_url: 'https://picsum.photos/100/106' },
      }
      // TODO: axios json으로 대체

      return playlistItems[v]
    }),
    roomPlaylistVideoItems: ( {roomVideos} ) => roomVideos.map((v) => {
      const playlistVideoItems = {
        1: { id: 1, thumbnail_url: 'https://picsum.photos/161/90', title: "먹물파스타 먹방", playtime: '01:30' },
        2: { id: 2, thumbnail_url: 'https://picsum.photos/162/90', title: "먹물파스타 먹방", playtime: '01:30' },
        3: { id: 3, thumbnail_url: 'https://picsum.photos/163/90', title: "먹물파스타 먹방", playtime: '01:30' },
        4: { id: 4, thumbnail_url: 'https://picsum.photos/164/90', title: "먹물파스타 먹방", playtime: '01:30' },
        5: { id: 5, thumbnail_url: 'https://picsum.photos/165/90', title: "먹물파스타 먹방", playtime: '01:30' },
        6: { id: 6, thumbnail_url: 'https://picsum.photos/166/90', title: "먹물파스타 먹방", playtime: '01:30' },
        7: { id: 7, thumbnail_url: 'https://picsum.photos/167/90', title: "먹물파스타 먹방", playtime: '01:30' },
        8: { id: 8, thumbnail_url: 'https://picsum.photos/168/90', title: "먹물파스타 먹방", playtime: '01:30' },
        9: { id: 9, thumbnail_url: 'https://picsum.photos/169/90', title: "먹물파스타 먹방", playtime: '01:30' },
      }
      // TODO: axios json으로 대체

      return playlistVideoItems[v]
    }),
  }
};

export default playroom;
