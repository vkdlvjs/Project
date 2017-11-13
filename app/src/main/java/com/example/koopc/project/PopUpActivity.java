package com.example.koopc.project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;

public class PopUpActivity extends AppCompatActivity {

    ImageView mBuildingImageView; // 건물 이미지 -- 추후 구현 예정
    TextView mBuildingNameView; // 건물 이름
    TextView mBuildingDescriptionView; // 건물 약식 서술
    ListView mIconList; // 건물 아이콘 리스트 -- 추후 구현 예정

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    DatabaseReference buildingNameRef = mRootRef.child("building"); // 참조 데이터베이스 내 차일드 값 받기.
    DatabaseReference eventNameRef = mRootRef.child("event"); // 참조 데이터베이스 내 차일드 값 받기.
    DatabaseReference bulletNameRef = mRootRef.child("bullet"); // 참조 데이터베이스 내 차일드 값 받기.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up);

        mBuildingNameView = (TextView) findViewById(R.id.popup_bulidingName);
        mBuildingDescriptionView = (TextView) findViewById(R.id.popup_buildingDescription);

        String[] gpsData = getIntent().getStringArrayExtra("gps"); // 좌표값을 받아서 해당 좌표와 동일할 경우 DB 에서 받아온다.
        //Toast.makeText(mContext, "좌표값을 받아옵시다", Toast.LENGTH_SHORT).show();
        mBuildingNameView.setText(gpsData[0]);
    }

    @Override
    protected void onStart() {
        super.onStart();

        buildingNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // 내가 DB에서 값을 바꿀경우 사용
                for(DataSnapshot ds : dataSnapshot.getChildren()){// 스냅 샷으로 빌딩의 정보를 받아 for문을 돌린다.
                    String s = ds.child("buildingName").toString();
                    Toast.makeText(PopUpActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { // 오류 동작

            }
        });
    }

    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업)
        // 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
