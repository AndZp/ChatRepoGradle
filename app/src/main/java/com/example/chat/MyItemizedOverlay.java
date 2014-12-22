package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

public class MyItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

	protected Context mContext;

	private MainActivity mainActivity;

	public MyItemizedOverlay(final Context context,
                             final List<OverlayItem> aList) {
		super(context, aList, new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index,
					final OverlayItem item) {
				return false;
			}

			@Override
			public boolean onItemLongPress(final int index,
					final OverlayItem item) {
				return false;
			}
		});
		// TODO Auto-generated constructor stub
		mContext = context;
		//mainActivity = activity;
	}

	@Override
	public boolean addItem(OverlayItem item) {
		// TODO Auto-generated method stub
		return super.addItem(item);
	}
	@Override
	protected boolean onSingleTapUpHelper(final int index,
			final OverlayItem item, final MapView mapView) {
		// Toast.makeText(mContext, "Item " + index + " has been tapped!",
		// Toast.LENGTH_SHORT).show();
		/*AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();*/
        Intent intent = new Intent(mContext, MainActivity.class);
		intent.putExtra("name", item.getTitle());
		intent.putExtra("latitude", item.getPoint().getLatitude());
		intent.putExtra("longitude", item.getPoint().getLongitude());
        mContext.startActivity(intent);
		/*Intent intentForFinish = new Intent();
		intentForFinish.putExtra("name", item.getTitle());
		intentForFinish.putExtra("latitude", item.getPoint().getLatitude());
		intentForFinish.putExtra("longitude", item.getPoint().getLongitude());*/
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
		float x = e.getRawX();
		float y = e.getRawY();
		//GeoPoint gp = (GeoPoint) mapView.getProjection().fromPixels(x,y);
		return super.onSingleTapUp(e, mapView);
	}


}