# smartfinder


정부가 식사문화 개선을 위해 선정한 ‘안심식당’ 정보를 일반 국민들이 알기 쉽게 제공한다.

농림축산식품부에서 지정한 안심식당은 ▲덜어먹기 가능한 도구 비치·제공 ▲위생적인 수저관리 ▲종사자 마스크 착용 등 3대 식사문화 개선 수칙을 지키는 곳이다. 

해당 정보는 일반 사용자가 직관적으로 보기 힘들다는 문제를 해결하기 위해, 지도에 주변 안심식당을 표시해주는 방식으로

안심식당의 활용성을 높이고자 개발하게 되었다.

다음 키워드로 검색 api,
다음 리버스 지오코딩 api,
안심식당 api,
네이버맵 api,
파이어베이스 api를 구현에 주로 사용하였다.

https://youtu.be/pRZjgaug47A

를 통해 자세한 시연영상을 확인할수 있다.

내가 보고있는 지역에 안심식당이 검색되었다고 메시지가 뜨지만 마커가 없는경우 오류가 아니다.
마커가 과다하게 찍히는것을 방지하게 위해, 내가 보고있는곳 주변 2000m의 음식점만 나오도록 해주었기에 주변에 안심식당이 보이지 않을수도 있다.
혹시 제 프로젝트를 사용하실경우 api key들을 바꾸는것을 잊지 말아주세요!

This application shows nearby "Ansim Restaurant", which is selected by South korea government for offering extra dishes, keep spoons clean, and restaurnat workers always wear masks.

I made this to search nearby Ansim Restaurant more directly, unlike Public Dataset offered by government.

I used Daum Reversegeo api, Daum keyword search api, Anisim api, navermap api, firebase api.

This application shows restaurnt only whithin 2000m based on your camera position, so sometimes it may not show you
nearby Ansim Restaurant, even though it pops up message that it found restaurant in your city.

This application only works in south korea, as this is based on Korea's public data. Keep aware of that.

But I believe this might help someone who is looking for way to learn about api, or map based application, so I'm leaving comment.

Good luck with your development and remember to change api keys in api classes!
