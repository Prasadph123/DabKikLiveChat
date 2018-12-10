package sample.sdk.dabkick.livechat;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.LiveChatCallbackListener;
import com.dabkick.engine.Public.MessageInfo;
import com.dabkick.engine.Public.UserInfo;
import com.dabkick.engine.Public.UserPresenceCallBackListener;

import java.util.ArrayList;
import java.util.Objects;

import sample.sdk.dabkick.livechat.adapter.ChatMsgAdapter;
import sample.sdk.dabkick.livechat.adapter.ChatRoomPagerAdapter;
import sample.sdk.dabkick.livechat.model.ChatRoom;
import sample.sdk.dabkick.livechat.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatSessionFragment extends Fragment implements View.OnClickListener {

    ImageButton sendBtn;
    AppCompatImageView mBackBtn;
    View view;
    AppCompatEditText chatEditText;
    RecyclerView chatListView;
//    ChatMsgAdapter mChatMessageAdapter;
//    List<MessageInfo> mChatMessageList = new ArrayList<MessageInfo>();
    ChatBackPress chatBackPress;
    TextView mUserName, mUserRoomLocation, mUserRoomDate;
    RelativeLayout mParentLayout;
    private String TAG = ChatSessionFragment.class.getSimpleName();
    private ChatMsgAdapter mChatMessageAdapter;
    private LiveChatCallbackListener mLiveChatListener;
    private UserPresenceCallBackListener mUserPresence;

    public ChatSessionFragment() {
    }


    public void findViews() {
        chatEditText = view.findViewById(R.id.chatEditText);
        sendBtn = view.findViewById(R.id.send_chat_msg);
        sendBtn.setOnClickListener(this);
        chatListView = view.findViewById(R.id.listview_chat);
        mUserName = view.findViewById(R.id.user_room_name);
        mUserRoomLocation = view.findViewById(R.id.user_location);
        mUserRoomDate = view.findViewById(R.id.user_date);

        setUpLayoutManager();
//        setUpChatAdapter();
        mBackBtn = view.findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(this);
        mParentLayout = view.findViewById(R.id.parent_layout);
        mParentLayout.setOnClickListener(this);
    }

    /*public void setUpChatAdapter() {

        mChatMessageAdapter = new ChatMsgAdapter(this.getActivity());
        if (chatListView != null)
            chatListView.setAdapter(mChatMessageAdapter);

        chatListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatListView.scrollToPosition(chatListView.getAdapter().getItemCount() - 1);
            }
        }, 100);
    }*/


    public void initViews() {
        ChatRoom room = getRoomInfo();
        if (room != null) {
            mUserName.setText(room.getRoomName());
            mUserRoomLocation.setText(room.getRoomLocation());
            mUserRoomDate.setText(room.getRoomDate());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_session, container, false);

        findViews();

        initViews();

        //added to scroll the list to the last item when edit text looses focus
        chatEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                chatListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (chatListView != null && chatListView.getAdapter() != null && !isFocused)
                            chatListView.scrollToPosition(chatListView.getAdapter().getItemCount() - 1);
                    }
                }, 100);

            }
        });

      /*   mLiveChatListener = new LiveChatCallbackListener() {
            @Override
            public void receivedChatMessage(String s, MessageInfo messageInfo) {

            }
        };

         mUserPresence = new UserPresenceCallBackListener() {
            @Override
            public void userEntered(String s, UserInfo userInfo) {

            }

            @Override
            public void userExited(String s, UserInfo userInfo) {

            }

            @Override
            public void userDataUpdated(String s, UserInfo userInfo) {

            }

            @Override
            public void getNumberOfUsersLiveNow(String s, int i) {

            }
        };*/

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((HomepageActivity) Objects.requireNonNull(getActivity())).dkLiveChat.subscribe("my room", mLiveChatListener, mUserPresence, new CallbackListener() {
                @Override
                public void onSuccess(String s, Object... objects) {
                    Toast.makeText(getActivity(), "Success "+s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String s, Object... objects) {

                }
            });
        }*/

        return view;
    }

    private String getRoomName(int position) {
        String[] nameArray = getResources().getStringArray(R.array.roomNames);
        switch (position) {
            case 0:
                return nameArray[0];
            case 1:
                return nameArray[1];
            case 2:
                return nameArray[2];
        }

        return "NONE";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_chat_msg:
                //text utils checks for null object as well
                if(chatEditText.getText() == null || TextUtils.isEmpty(chatEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter a proper message", Toast.LENGTH_SHORT).show();
                    return;
                }
                MessageInfo info = new MessageInfo();
                info.setUserId("XYZ123");
                info.setAppSpecificUserID("anon123");
                info.setChatMsgType(0);
                info.setSystemMessage(false);
                info.setChatMessage(chatEditText.getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((HomepageActivity) Objects.requireNonNull(getActivity())).dkLiveChat.chatEventListener.sendMessage("", info, new CallbackListener() {
                        @Override
                        public void onSuccess(String s, Object... objects) {
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String s, Object... objects) {
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                if (!TextUtils.isEmpty(chatEditText.getText().toString().trim())) {
                    String chatMsg = chatEditText.getText().toString().replaceAll("^\\s+|\\s+$", "");
                    chatEditText.setText("");
                    Utils.hideKeyboard(getActivity());
                } else {
                    Snackbar.make(view, "Enter Message", Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.back_btn:
                if (getActivity() != null) {
                    Utils.hideKeyboard(getActivity());
                    getActivity().onBackPressed();
                    ChatRoomFragment.isDetailChatOpen = false;
                    chatBackPress.backButtonClick();
                }
                break;
            case R.id.parent_layout:
                Utils.hideKeyboard(getActivity());
                break;
        }
    }

    private void setUpLayoutManager() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        chatListView.setLayoutManager(mLayoutManager);
        chatListView.setItemAnimator(new DefaultItemAnimator());
    }

    void setbackPress(ChatBackPress chatBackPressed) {
        chatBackPress = chatBackPressed;
    }

    public interface ChatBackPress {

        void backButtonClick();
    }

    private ChatRoom getRoomInfo() {
        if (ChatRoomPagerAdapter.getCurrentItem() < ((HomepageActivity) getActivity()).mRoomPagerAdapter.getCount()) {
            ChatRoom room = ((HomepageActivity) getActivity()).mRoomPagerAdapter.listOfRooms.get(ChatRoomPagerAdapter.getCurrentItem());
            return room;
        }
        return null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ChatRoomFragment.isDetailChatOpen = false;
        chatBackPress.backButtonClick();
    }
}
