package org.appspot.apprtc;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.appspot.apprtc.service.WebsocketService;
import org.appspot.apprtc.util.ThumbnailsCacheManager;

import static org.appspot.apprtc.RoomActivity.EXTRA_SERVER_NAME;


public class UserActivity extends AppCompatActivity {

    private String mServer;
    private TextView text;
    private TextView message;
    private ImageView image;
    RelativeLayout callButton;
    RelativeLayout chatButton;
    RelativeLayout shareFileButton;
    private User user;
    private RelativeLayout videocallButton;
    private String mOwnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ThumbnailsCacheManager.ThumbnailsCacheManagerInit(getApplicationContext());

        image = (ImageView) findViewById(R.id.image_id);
        text = (TextView) findViewById(R.id.text_id);
        message = (TextView) findViewById(R.id.message);
        Intent intent = getIntent();

        if (intent.hasExtra(WebsocketService.EXTRA_OWN_ID)) {
            mOwnId = intent.getStringExtra(WebsocketService.EXTRA_OWN_ID);
        }

        if (intent.hasExtra(EXTRA_SERVER_NAME)) {
            mServer = intent.getStringExtra(EXTRA_SERVER_NAME);
        }
        if (intent.hasExtra(WebsocketService.EXTRA_USER)) {
            user = (User) intent.getSerializableExtra(WebsocketService.EXTRA_USER);
            String buddyPic = user.buddyPicture;

            if (buddyPic.length() != 0) {
                image.clearColorFilter();
                String path = buddyPic.substring(4);
                String url = "https://" + mServer + RoomActivity.BUDDY_IMG_PATH + path;
                ThumbnailsCacheManager.LoadImage(url, image, user.displayName, true, true);
            }
            else {
                image.setImageResource(R.drawable.ic_person_white_48dp);
                image.setColorFilter(Color.parseColor("#ff757575"));
            }
            text.setText(user.displayName);
            if (user.message != null) {
                message.setText(user.message);
            }
            else {
                message.setText("");
            }
        }

        videocallButton = (RelativeLayout) findViewById(R.id.videocall_layout);
        callButton = (RelativeLayout) findViewById(R.id.call_layout);
        chatButton = (RelativeLayout) findViewById(R.id.chat_layout);
        shareFileButton = (RelativeLayout) findViewById(R.id.file_layout);

        shareFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == shareFileButton.getId()){
                    Intent intent = new Intent(v.getContext(), RoomActivity.class);
                    intent.setAction(RoomActivity.ACTION_SHARE_FILE);
                    intent.putExtra(WebsocketService.EXTRA_USER, user);
                    v.getContext().startActivity(intent);
                }
            }
        });

        videocallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == videocallButton.getId()){
                    Intent intent = new Intent(v.getContext(), CallActivity.class);
                    intent.setAction(CallActivity.ACTION_NEW_CALL);
                    intent.putExtra(WebsocketService.EXTRA_ADDRESS, mServer);
                    intent.putExtra(WebsocketService.EXTRA_USER, user);
                    intent.putExtra(WebsocketService.EXTRA_OWN_ID, mOwnId);
                    intent.putExtra(CallActivity.EXTRA_VIDEO_CALL, true);
                    v.getContext().startActivity(intent);
                }
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == callButton.getId()){
                    Intent intent = new Intent(v.getContext(), CallActivity.class);
                    intent.setAction(CallActivity.ACTION_NEW_CALL);
                    intent.putExtra(WebsocketService.EXTRA_ADDRESS, mServer);
                    intent.putExtra(WebsocketService.EXTRA_USER, user);
                    intent.putExtra(WebsocketService.EXTRA_OWN_ID, mOwnId);
                    intent.putExtra(CallActivity.EXTRA_VIDEO_CALL, false);
                    v.getContext().startActivity(intent);
                }
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == chatButton.getId()){
                    Intent intent = new Intent(v.getContext(), RoomActivity.class);
                    intent.setAction(RoomActivity.ACTION_NEW_CHAT);
                    intent.putExtra(WebsocketService.EXTRA_USER, user);
                    intent.putExtra(WebsocketService.EXTRA_OWN_ID, mOwnId);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }
}
