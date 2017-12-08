package mabeechen.chatheads.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import mabeechen.chatheads.R;

public class ChatHeadService extends Service {

	private WindowManager windowManager;
	private View chatHead;
	private WindowManager.LayoutParams currentParams;
	private ChatHeadOnTouchListener touchListener;
	private ChatHeadOnClickListener clickListener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		currentParams = getBaselineParams();
		inflateAndAddView();
		setFloatingViewListeners();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null) windowManager.removeView(chatHead);
	}

	@NonNull
	private WindowManager.LayoutParams getBaselineParams() {
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 0;
		return params;
	}

	private void inflateAndAddView() {
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		chatHead = layoutInflater.inflate(R.layout.chat_head, null);
		windowManager.addView(chatHead, currentParams);
	}

	private void setFloatingViewListeners() {
		touchListener = new ChatHeadOnTouchListener();
		clickListener = new ChatHeadOnClickListener();

		try {
			chatHead.setOnTouchListener(touchListener);
		} catch (Exception e) {
			// TODO: handle exception
		}

		chatHead.setOnClickListener(clickListener);
	}

	private class ChatHeadOnTouchListener implements View.OnTouchListener {
		private int initialX;
		private int initialY;
		private float initialTouchX;
		private float initialTouchY;

		@Override public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					initialX = currentParams.x;
					initialY = currentParams.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					currentParams.x = initialX + (int) (event.getRawX() - initialTouchX);
					currentParams.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(chatHead, currentParams);
					break;
			}
			return false;
		}
	}

	private class ChatHeadOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			//initiatePopupWindow(chatHead);
			Button b = (Button) chatHead.findViewById(R.id.show_button);
			if (b.getVisibility() == View.VISIBLE) {
				b.setVisibility(View.GONE);
				windowManager.updateViewLayout(chatHead, currentParams);
			} else {
				b.setVisibility(View.VISIBLE);
				windowManager.updateViewLayout(chatHead, getBaselineParams());
			}
		}
	}
}