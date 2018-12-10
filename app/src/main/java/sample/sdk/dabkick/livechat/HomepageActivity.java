package sample.sdk.dabkick.livechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dabkick.engine.Public.Authentication;
import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.DKLiveChat;

import java.util.ArrayList;

import sample.sdk.dabkick.livechat.adapter.ChatRoomPagerAdapter;
import sample.sdk.dabkick.livechat.model.ChatRoom;

public class HomepageActivity extends AppCompatActivity {

    CustomViewPager mChatRoomPager;
    public ChatRoomPagerAdapter mRoomPagerAdapter;
    ArrayList<ChatRoom> mChatRoomList;
    FrameLayout chatSessionFragContainer;
    public DKLiveChat dkLiveChat;
    private String TAG = HomepageActivity.class.getSimpleName();


    public void findViews() {
        mChatRoomPager = findViewById(R.id.chat_room_view_pager);
        chatSessionFragContainer = findViewById(R.id.chat_session_fragment_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        findViews();

        initChats();
        mChatRoomList = new ArrayList<ChatRoom>();
        mRoomPagerAdapter = new ChatRoomPagerAdapter(getSupportFragmentManager(), HomepageActivity.this);
        mChatRoomPager.setOffscreenPageLimit(0);
        mChatRoomPager.setAdapter(mRoomPagerAdapter);

        mChatRoomPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                ChatRoomPagerAdapter.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private void initChats() {
        /**
         * Sample app developer id and key
         */
        Authentication auth = new Authentication("DKe1ac069ddf1011e7a1d8062", "f84bd8d546b10cff2b601093e47f61");

        dkLiveChat = new DKLiveChat(getApplicationContext(), auth, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(String s, Object... objects) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mDKLiveChat.endLiveChat();
    }
}

