package com.example.smartfinder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.naver.maps.map.overlay.Overlay.*;


public class MainActivity extends AppCompatActivity implements NaverMap.OnMapClickListener, OnClickListener, OnMapReadyCallback{
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;//네이버맵 사용하기 위해1
    private FusedLocationSource locationSource;//네이버맵 사용하기위해2
    private NaverMap naverMap;//맨 처음 네이버맵을 선언해야 한 지도에 여러개의 마커를 찍음
    private InfoWindow infoWindow;//인포윈도우
    private List<Marker> markerList = new ArrayList<Marker>();
    public String URL;//여기서부터
    public String Phone;
    public String Name;
    public String Category;//여기까지가 현재 보고있는 인포윈도우의 URL,전화번호,음식점 이름, 카테고리
    String City;//현재 도시
    Double lat;//카메라좌표 래티튜드
    Double lng;//카메라좌표 롱기튜드
    ProgressDialog pd;//로딩바


    /*
      검색 기능의 구현방법에 대하여

      안심식당의 api는 사용이 매우 불편해 먼저 처리과정을 거쳐야했다.
      먼저 첫번째로, 안심식당의 검색은 시도명(서울특별시,경기도,대구광역시등)를 이용해 검색하거나, 시군구명(중구,남원구)등을 이용해서 검색만 가능했다.
      따라서 사용자에게 지역을 입력받아야 했는데, 이를 위해서 두가지 선택지가 있었다.
      첫번째, 현재 위치의 정확한 지명, 그것도 시도명 혹은 시군구명만을 입력하는것. 이는 사용이 매우 어렵고 귀찮으며 유용하지않다
      두번째, 안드로이드 스피너 기능을 이용해 개발자가 미리 설정해둔 목록중 맞는 지역명을 찾아 선택하는것. 이또한 사용에 어려우며, 더 큰문제로는 시도명을 사용해 목록을 만들경우,
      (예를들어)경기도를 검색한다면 경기도의 모든 안심식당을 검색해 현재 내 주변에 있는곳을 찾아 마커를 찍어주는 방식이되야할텐데, 마커도 거의 몇천개가 찍히고 시간도 오래걸려
      비효율적이라는점이다. 대신 시군구명을 사용해 목록을 만들경우엔, 구의 이름이 겹쳐 다른지역 구(예를들어, 중구는 여러지역에 있다)를
      검색하게 될수도 있다는것이다.
      그렇다고 시도명과 시군구명을 함께 사용한다면, 구역을 바꿔 검색할때마다 보고자 하는곳의 정확한 지역명을 사용자가 알아내 선택해야하는데, 이는 비효율적이기도하고, 사용에도 어렵다.
      부산 바다 주변 어딘가의 안심식당을 찾으려는데 그곳의 정확한 지명까지 먼저 찾고 검색 스피너에서 해당 지역명을 찾아야만 검색이된다면 효율적이라 하진 않을것이다.

      이문제를 해결하기위해, 카카오 api중 리버스 지오코딩을 이용해 현재 내가 보고있는곳의 위치좌표를 시군구명으로 바꾸어 검색에 이용했다.
      이를통해, 기존 네이버,카카오,구글지도와 같이 원하는 위치를 보다 버튼한번만 누르면 주변 안심식당을 찾을수있게되었다. 하지만 이때 문제가 있었는데, "안양시 동안구"와같이 결과가 받아지기도했다.
      안심식당의 api는 오직 "안양시"만을 입력해야만 검색이되는 불편함이 있었기에, 이런경우 뒤의 동안구 부분을 지워주는 과정을 추가해주었다.

      두번째로는,이렇게 얻은 지역정보를 이용해 안심식당을 검색하고 결과값을 받는것이다.
      이때 문제였던것이, 안심식당의 마커를 찍기위해선 지도상에서의 x,y좌표가 필요하다는 점이였는데, 안심식당 api는 해당 내용을 제공해주지 않았다.
      대신 한글로된 주소를 제공해주었는데, 문제는 이 주소가 정확하지 않아 해당 주소로 지오코딩을 해 x,y좌표를 얻을수 없다는 점이였다.
      예를들어, 광진구 건국대학교 신공학관 3층 103호 와 같은 정보를 제공한다던가 도로명으로 잘 제공되었더라도 지오코딩으로 x,y좌표를 얻을수 없는 경우가 많았다.

      이런 문제를 해결하기위해, 음식점의 이름만 받고, 카카오의 키워드로검색 api를 사용해 x,y좌표를 얻어내었다.
      이 api는 어떤 음식점의 이름, 혹은 장소를 검색하면, 그곳의 좌표와 다음플레이스 정보의 url을 주는 api이다. 물론 이때도 문제가 생겼다.
      예를 들어 안심식당 이름으로 롯데리아가 왔다고 가정해보자
      이경우 롯데리아는 아주 많은 지점이 있는데, 그냥 키워드 검색을 해버리면 현재 내 위치 주변의 지점이 아닌 다른곳의 정보를 알려준다
      또 꼭 체인점이 아니더라도, 음식점 이름이 같으면 같은 문제가 발생한다
      이 문제를 해결하기 위해 내가 보고있는 곳의 좌표를 카카오 키워드 검색에 함께 보내주었더니, 주변의 내가 원하는 음식점이 결과로 제대로 반환된다
      하지만 이때도 음식점 이름이 "강릉닭갈비" 와 같다면, 설령 내 주소를 함께 보냈다 하더라도 "강릉"에 있는 "닭갈비"음식점들을 검색했다.
      이 문제를 고치기 위해 추가로 현재 내가 보고있는 도시의 이름을 함께 검색 (예를들어 "광진구 강릉닭갈비")하였더니 문제가 해결되었다
      마지막 문제로는, 다음플레이스 자체에 해당 음식점의 정보가 등록 되지 않았거나, 안심식당api 에서 제공해주는 이름이 올바르지 않은 경우가 있었다.
      예를들어, 건대입구역의 (주)카페낢진.맘스터치 와 같은 경우인데, 이런경우는 전체에서 차지하는 개수가 적기도 하고, 해결방안이 없기에 이후 에러를 일으키지 않도록 필터링 작업정도만 해주었다.

      세번째로는, 마커를 찍는과정인데, 이제 얻어온 음식점의 이름,전화번호,좌표,URL,업종명 을 가지고 찍어주기만 하면 되었다.
      이부분에서는 큰 문제가 없었지만 마커가 너무 많이 찍힐경우 부하가 커지므로, 다른지역을 검색할경우
      이전에 찍었던 마커를 지워주고, 현재 내 좌표를 주변으로 1500미터 안의 음식점만 표현해주도록 하였다.

      이를통해서 검색기능의 문제들을 해결했고, 해당 정보들을 이용해 마커찍기, 마커 클릭시 인포윈도우를 보여줘 해당 음식점에대해 추가정보를 얻도록 구현하기, 그 음식점의 다음 플레이스로 연결되어
      더 많은 정보를 얻을수 있게 하기, 맘에드는 음식점을 저장해 찜리스트 만들기, 간단한 듀토리얼 제공하기, 개발자 깃허브와 연결해 추가적으로 도움을 줄수 있도록 하기등을 더 구현해주었다.



     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//액티비티 메인을 화면에 set


        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);//지도를 화면에 표시
        mapFragment.getMapAsync(this);//MapFragment는 지도에 대한 뷰 역할만을 담당하므로 API를 호출하려면 인터페이스 역할을 하는 NaverMap 객체가 필요한데, getMapAsync() 메서드로 OnMapReadyCallback을 등록하면 비동기로 NaverMap 객체를 얻을 수있음


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);//메뉴바 표시 구현
        setSupportActionBar(myToolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); 추가구현 고민중인 부분,현재는 의미 없음


        Button btn = (Button)findViewById(R.id.button);//첫 화면의 "바로가기" 버튼 정의, 이후 URL값이 생기면 바로가기 버튼을 눌러 내가 보고있는 인포윈도우의 다음플레이스로 화면 전환할수있음
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(URL!=null) {
                    // 웹페이지 열기
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                    startActivity(mIntent);
                }
            }
        });


        Button btn2 = (Button)findViewById(R.id.button2);//첫 화면의 "찜하기" 버튼 정의, 이후 URL,전화번호,카테고리,이름값이 생기면 버튼을 누르면 파이어베이스에 찜리스트로 저장이됨
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (URL != null) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();//여기서부터 파이어베이스에 저장하는 부분

                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("URL", URL);//이런 정보들을 저장함
                    user.put("Phone", Phone);
                    user.put("Category", Category);
                    user.put("Name", Name);

                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("1", "DocumentSnapshot added with ID: " + documentReference.getId());//저장 잘되었는지 로그로 띄워 확인하는 부분
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("2", "Error adding document", e);//마찬가지로 로그로 확인
                                }
                            });

                }
            }
        });// 여기까지가 파이어베이스에 찜리스트 저장 기능


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //오른쪽 상단의 메뉴모양 정의된 R.메뉴.메뉴 사용하기 위해 작성된 부분
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            //실제 메뉴의 기능 구현
        switch (item.getItemId()) {
            case R.id.action_settings1://메뉴의 첫번째 버튼 눌리면 내 깃허브와 연결됨

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/wjdtkdgns777/smartfinder"));
                startActivity(intent);//인텐트와 uri이용하여 깃허브 연결

                return super.onOptionsItemSelected(item);

            case R.id.action_settings2://간단한 어플의 도움말 메뉴

                Intent intent2 = new Intent(getApplicationContext(), guideInfo.class);//가이드인포로 넘어감
                startActivity(intent2);


                return super.onOptionsItemSelected(item);


            case R.id.action_settings3://내 찜리스트 보여주는 메뉴

                Intent intent3 = new Intent(getApplicationContext(), MyList.class);//마이리스트로 넘어감
                startActivity(intent3);


                return super.onOptionsItemSelected(item);

            default://디폴트는 설정 안함


                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        //맵이 준비되었을때

        this.naverMap=naverMap;//제일 처음에 선언한 네이버맵과 현재 맵을 동일시해 나중에 마커를 찍어줄때 하나의 맵에 다 찍을수 있도록 함, 이렇지 않으면 맵하나를 만들고 마커하나를 찍고, 다음마커를 찍을때는 새로운 맵이 만들어져 결국 마커 하나만 화면에 남음
        naverMap.setOnMapClickListener(this);//맵이 클릭되었을경우 리스너, 이후 사용
        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);//gps사용허가 물어보도록, 아래 추가 내용있음
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();//지도상 내 현재위치로 가는 버튼 생성하기위해 작성
        uiSettings.setLocationButtonEnabled(true);//왼쪽 아래에, 현재 내 gps상 위치로 카메라 이동하는 버튼 보이게함




        infoWindow = new InfoWindow();//인포윈도우 정의
        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();//인포윈도우가 현재 클릭된 마커의 정보를 이용하기 위해 작성
                KakaoResult2 result = (KakaoResult2) marker.getTag();//현재 클릭된 마커에 붙여진 태그정보는 카카오리절트 형식의 정보인데, 이를 받음(아래에 더 설명됨)
                View view = View.inflate(MainActivity.this, R.layout.view_info_window, null);//인포윈도우 띄움
                ((TextView) view.findViewById(R.id.name)).setText(result.getDocuments2().get(0).getname());//인포윈도우 보이는부분 음식점 이름 설정
                Name = result.getDocuments2().get(0).getname();//위에 선언한 이름에 저장해서 나중에도 사용하도록함(예를들어 찜리스트)

                ((TextView) view.findViewById(R.id.stock)).setText(result.getDocuments2().get(0).category_name);//인포윈도우 보이는부분 카테고리 설정
                Category = result.getDocuments2().get(0).category_name;//나중에 카테고리 사용할수있게

                URL = result.getDocuments2().get(0).place_url;//유알엘도 나중에 사용할수있게 저장
                ((TextView) view.findViewById(R.id.time)).setText(result.getDocuments2().get(0).phone);//인포윈도우 전화번호 설정
                Phone = result.getDocuments2().get(0).phone;//나중에 사용할수있게
                return view;
            }



        });



        Button Searchbutton = (Button) findViewById(R.id.Sbutton);//여기를 기반으로 검색버튼 정의

        Searchbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetMarkerList();//눌리면 일단 전에 있던 마커 리스트를 지워줘서 시스템 부하를 줄여줌
                pd = new ProgressDialog(MainActivity.this);//pd.부분은 전부 로딩바 보이게 하는 내용
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("주변 안심식당을 검색하는 중입니다");
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();
                Thread mThread = new Thread() {
                    @Override
                    public void run() {//검색중에 다른일을 할수 있게 하기 위해서 쓰레드 사용

                        LatLng mapCenter = naverMap.getCameraPosition().target;//현재 카메라가 보고있는 좌표 맵 센터에 저장
                        lat=mapCenter.latitude;//그중 래티튜드 lat에 저장해 나중에 사용가능하게
                        lng=mapCenter.longitude;//똑같이 하되 롱기튜드
                        ReverseGeo();//역지오코딩으로 현재 좌표 얻도록

                    }
                };
                mThread.start();


            }
        });



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//위에 있던 gps연결 확인 코드
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }//gps사용허가 받았는지 아닌지 확인하는 코드
    }


    private void ReverseGeo() {//역 지오코딩을 통해 현재 카메라가 보고있는 좌표의 도시명을 알수 있음, 이를 이용해 이후 안심식당 api 검색


        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dapi.kakao.com").addConverterFactory(GsonConverterFactory.create()).build();
        //레트로핏을 이용해 카카오 api사용했다.레트로핏은 REST 기반의 웹 서비스를 통해 JSON 구조의 데이터를 쉽게 가져오고 업로드할 수 있게 하는 라이브러리이다.카카오 api클래스에 쿼리와 키등을 추가설명
        KakaoApi kakaoApi = retrofit.create(KakaoApi.class);//카카오 api클래스 사용


        kakaoApi.getcitybygeo(lat, lng).enqueue(new Callback<KakaoResult>() {//현재 카메라 x,y값 보내주면 결과받음
            @Override
            public void onResponse(Call<KakaoResult> call, Response<KakaoResult> response) {
                if (response.code() == 200) {//만약 제대로 연결이 되고
                    KakaoResult result = response.body();

                    if (result.getDocuments().size() != 0) {//제대로 결과를 받았을 경우


                        City = result.getDocuments().get(0).getAddress().getName();//시티에 현재 내가 보고있는곳의 도시이름을 저장함
                        int idx = City.indexOf(" ");//이때 카카오 api에 문제가 있었는데, "안양시"만 받아야 검색에 사용할수 있는데 "안양시 동안구"라고 결과가 오는경우가 일부 있었음 이를해결하기 위해서,

                        if(idx!=-1) {
                            City = City.substring(0, idx);//안양시 뒤의 부분을 떼어내고 안양시만 남김
                        }


                        Ansim(City);//이 도시 이름으로 안심식당 api를 사용

                    }

                }
            }

            @Override
            public void onFailure(Call<KakaoResult> call, Throwable t) {

            }
        });
    }


    private void Ansim(final String RELAX_SIDO_NM) {//안심식당 api사용
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://211.237.50.150:7080/openapi/5010c6384bc1bf89ca9024762721a81118a3d59e00cce037a1b09b5732169e05/json/Grid_20200713000000000605_1/1/1000/").addConverterFactory(GsonConverterFactory.create()).build();
        AnsimApi ansimApi = retrofit.create(AnsimApi.class);//위의 카카오 api와 마찬가지로 레트로핏 사용

        ansimApi.getStoresByGeo(RELAX_SIDO_NM).enqueue(new Callback<StoreSaleResult>() {
            @Override
            public void onResponse(Call<StoreSaleResult> call, Response<StoreSaleResult> response) {

                if (response.code() == 200) {
                    StoreSaleResult result2 = response.body();
                    if (result2.Grid_20200713000000000605_1.getRow().size() != 0) {//현재 내가 보는 도시로 검색했고, 안심식당 결과가 잘 온경우
                        //Log.d(String.valueOf((result2.Grid_20200713000000000605_1.Anisim_total())), "total"); (확인용으로 로그체크로 개수 새봤었음)

                        Toast.makeText(MainActivity.this, City+"에서"+result2.Grid_20200713000000000605_1.Anisim_total()+"개의 결과가 검색되었습니다", Toast.LENGTH_LONG).show();//얼마나 안심식당이 있나 토스트메시지
                        if (result2.Grid_20200713000000000605_1.getRow() != null && result2.Grid_20200713000000000605_1.totalCnt > 0) {//만약 한개 이상 있다면
                            for (StoreSaleResult.Grid_20200713000000000605_1.row name : result2.Grid_20200713000000000605_1.row) {

                               KakaoRestaurant(name.rname);//그 개수만큼 카카오 키워드검색 api를 사용해서 정보를 얻음


                            }
                        }
                    }

                    if (result2.Grid_20200713000000000605_1.getRow().size() == 0) {//없으면 없다고 알림

                        Toast.makeText(MainActivity.this, "주변에 안심식당이 없습니다", Toast.LENGTH_LONG).show();
                        pd.dismiss();//주변에 없으므로 로딩바 해제
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreSaleResult> call, Throwable t) {

            }
        });
    }




    private void KakaoRestaurant(final String name7) {//카카오 키워드 검색 api(https://developers.kakao.com/tool/rest-api/open/get/v2-local-search-keyword.%7Bformat%7D)


        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dapi.kakao.com").addConverterFactory(GsonConverterFactory.create()).build();
        KakaoApi2 kakaoApi2 = retrofit.create(KakaoApi2.class);//레트로핏 사용


        kakaoApi2.getname(City +" "+ name7, lat, lng, 20000).enqueue(new Callback<KakaoResult2>() {
            /*위에 설명된 바와 같이
            안심식당 이름으로 롯데리아가 왔다고 가정해보자
            이경우 롯데리아는 아주 많은 지점이 있는데, 그냥 키워드 검색을 해버리면 현재 내 위치 주변의 지점이 아닌 다른곳의 정보를 알려준다
            꼭 체인점이 아니더라도, 음식점 이름이 같으면 같은 문제가 발생한다
            그렇기 때문에 내가 보고있는 곳의 좌표를 카카오 키워드 검색에 함께 보내주면 주변의 음식점이 결과로 반환된다
            하지만 음식점 이름이 "강릉닭갈비" 와 같다면, 내 주소를 함께 보냈다 하더라도 "강릉"에 있는 "닭갈비"음식점들을 검색한다
            이 문제를 고치기 위해 현재 내가 보고있는 도시의 이름과 함께 검색, 예를들어 "광진구 강릉닭갈비"할 경우 문제가 해결되었다
            radius는 내 반경 20000미터내의 음식점을 검색한다는것이다.이후 마커찍을때 너무 먼곳은 필터링 할것이므로 정확도를 위해 넓게 잡았다
            */
            @Override
            public void onResponse(Call<KakaoResult2> call, Response<KakaoResult2> response) {
                if (response.code() == 200) {
                    KakaoResult2 result4 = response.body();


                    if (result4.getDocuments2().size() != 0) {

                        //검색결과가 제대로 나온경우 마커찍기에 들어간다
                            SetMapMarker(result4);


                    }
                    if (result4.getDocuments2().size() == 0) {
                        pd.dismiss();
                        Log.d(String.valueOf(name7), "size 0");//에러 체크용 로그
                    }


                }
            }

            @Override
            public void onFailure(Call<KakaoResult2> call, Throwable t) {

            }
        });

    }

    private void SetMapMarker(KakaoResult2 result) {

        pd.dismiss();//로딩바 해제
        int MyDistance = Integer.parseInt(result.getDocuments2().get(0).distance);//음식점과 나로부터의 거리

        if (MyDistance < 1500) {//현재 위치에서 1500미터 안의 음식점들만 마커를 찍어줘 어플의 부하를 줄임
            //Log.d(String.valueOf(result.meta.same_name.keyword), "keyword"); 로그체크
            //Log.d(String.valueOf(result.getDocuments2().get(0).place_name), "name"); 로그체크


            Marker marker = new Marker();//마커 만들어서
            marker.setTag(result);//검색된 리절트값을 태그로 붙여줌. 이래야 위에 봤던것처럼 현재 마커를 클릭했을시 음식점 정보를 보여줄수있음
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_baseline_restaurant_24));//아이콘 설정, 식당에 맞게하고 푸른색이 가장 어울리는것 같아 진한 푸른색으로함

            marker.setPosition(new LatLng(Double.parseDouble(result.getDocuments2().get(0).y), Double.parseDouble(result.getDocuments2().get(0).x)));//마커위치 설정


            marker.setAnchor(new PointF(0.5f, 1.0f));//위치로 부터 마커의 상대적인 표시위치
            marker.setMap(naverMap);//맵에 마커 set

            marker.setOnClickListener(this);//마커 리스너
            markerList.add(marker);//마커 리스트에 저장
        }


    }
    @Override
        public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        if (infoWindow.getMarker() != null) {
        infoWindow.close();//아까 만들었던 맵 클릭시 리스너, 마커 떠있는데 맵 클릭하면 인포윈도우 닫히게 함
        }
        }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;

            if (marker.getInfoWindow() != null) {
                infoWindow.close();//인포윈도우가 열려있을때 다시 클릭하면 인포윈도우 닫히게
            } else {
                if(infoWindow!=null) {
                    infoWindow.open(marker);//그 반대로, 열리게

                }
            }
            return true;
        }
        return false;
    }



    private void resetMarkerList() {//마커리스트 초기화
        if (markerList != null && markerList.size() > 0) {
            for (Marker marker : markerList) {
                marker.setMap(null);
            }
            markerList.clear();
        }

    }


}







