package com.zhephone.hotspot.app.ui.chat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.AppContext;
import com.zhephone.hotspot.app.AppManager;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.ui.user.LoginActivity;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.NearGroup;
import com.zhephone.hotspoti.app.bean.NearGroupList;


public class NearGroupActivity extends BaseActivity {

	MapView mMapView = null; // 地图View
	LocationClient mLocClient = null;
	LocationData locData = null;
	MyLocationOverlay myLocationOverlay = null;
	private MapController mMapController = null;

	private MyOverLay mOverlay = null;
	private ArrayList<OverlayItem>  mItems = null; 
	private List<NearGroup> nearGroups;
	private Button backBt;
	private Context context;
	
	@Override
	protected void initViews() {
		mMapView = (MapView) findViewById(R.id.bmapsView);
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.near_group));
		mMapController = mMapView.getController();
		mMapController.setZoom(15);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.near_group);
		this.setUpViews();
	}
	
	private void loadNearGroupSucess(String data) {
		try {
			NearGroupList hotList = JSON.parseObject(data, NearGroupList.class);
			if(hotList.getAllchannel() != null && hotList.getAllchannel().size()>0) {
				nearGroups = hotList.getAllchannel();
				/**
				 * 创建自定义overlay
				*/
				 mOverlay = new MyOverLay(getResources().getDrawable(R.drawable.icon_marka),mMapView);	
				 /**
				  * 准备overlay 数据
				  */
				 GeoPoint gp;
				 OverlayItem item;
				 for(NearGroup nearGroup:nearGroups) {
					 if(StringUtils.isNotEmpty(nearGroup.getLatitude()) && StringUtils.isNotEmpty(nearGroup.getLongitude())) {
						 gp = new GeoPoint((int)((Double.valueOf(nearGroup.getLatitude()))*1e6),
								 (int)((Double.valueOf(nearGroup.getLongitude()))*1e6));
						 item = new OverlayItem(gp,"覆盖物","");
						 /**
						  * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
						  */
						 item.setMarker(getResources().getDrawable(R.drawable.icon_geo));
						 mOverlay.addItem(item);
					 }
				 }
				 /**
				  * 保存所有item，以便overlay在reset后重新添加
				  */
				 mItems = new ArrayList<OverlayItem>();
				 mItems.addAll(mOverlay.getAllItem());
				 /**
				  * 将overlay 添加至MapView中
				  */
				 mMapView.getOverlays().add(mOverlay);
				 /**
				  * 刷新地图
				  */
				// 修改定位数据后刷新图层生效
				mMapView.refresh();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		dissmissProgressDialog();
	}
	
	private void loadNearGroupFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	private void loadNearGroup(String userDevice) {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null, getResources().getString(R.string.loading));
			RequestParams paramMap = new RequestParams();
			paramMap.put("udid", userDevice);
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.ALL_GROUP,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						loadNearGroupSucess(data);
					}
				}

				public void onError(String error) {
					loadNearGroupFailed(error);
				}
			});
			api.execute();
		}
	}

	private void initMapData() {
		 //定位初始化
        mLocClient = new LocationClient( this );
        locData = new LocationData();
        mLocClient.registerLocationListener(new MyLocationListenner());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
       
        //定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		
		//设置定位数据
	    myLocationOverlay.setData(locData);
	    //添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//修改定位数据后刷新图层生效
		mMapView.refresh();
	}
	
	@Override
	protected void initData() {
		context = this.getApplicationContext();
		AppContext appContext = AppContext.getInstance();
		if (appContext.mBMapManager == null) {
			appContext.mBMapManager = new BMapManager(getApplicationContext());
	        /**
	        * 如果BMapManager没有初始化则初始化BMapManager
	         */
			appContext.mBMapManager.init(new AppContext.MyGeneralListener());
	    }
		initMapData();
		String userDevice = AppConfig.getInstance().getDeviceToken();
		loadNearGroup(userDevice);
	}
	
	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//AppManager.getAppManager().finishActivity(GroupListActivity.class);
			back();
		}
	};

	@Override
	protected void addListener() {
		backBt.setOnClickListener(backListener);
	}


	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		super.onDestroy();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			try {
				if (location == null)
					return; 
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				// 如果不显示定位精度圈，将accuracy赋值为0即可
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
				// 更新定位数据
				myLocationOverlay.setData(locData);
				mMapView.refresh(); 
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}
	

	
	/**
	 * @author JL
	 * 
	 * 要处理overlay点击事件时需要继承ItemizedOverlay 不处理点击事件时可直接生成ItemizedOverlay.
	 */
	private class MyOverLay extends ItemizedOverlay<OverlayItem> {
	    // 处理tap点击的句柄
	    public MyOverLay(Drawable mark, MapView mapView) {
	        super(mark, mapView);
	        // TODO Auto-generated constructor stub
	    }

	    // 在此处理item点击事件，并且一定会触发MapView的点击事件
	    //arg0表示多个item中的序号
	    @Override
	    protected boolean onTap(int index) {
	    	NearGroup nearGroup = nearGroups.get(index);
	    	Intent intent;
	    	//if(AppConfig.getInstance().isLogin()) {
	    		intent = new Intent(context,GroupChatActivity.class);
	    		intent.putExtra(Constant.ChatIntentDef.GROUP_CHAT_ID, nearGroup.getImei());
	    		intent.putExtra(Constant.ChatIntentDef.GROUP_NAME, nearGroup.getName());
	    		intent.putExtra(Constant.ChatIntentDef.CHAT_SOURCE, Constant.ChatIntentDef.MAP_LIST);
//	    	} else {
//	    		intent = new Intent(context,LoginActivity.class);
//	    	}
	    	startActivityLeft(intent);						
	        return true;
	    }

	    // 在此处理MapView的点击事件，不一定会触发item点击事件
	    @Override
	    public boolean onTap(GeoPoint pt, MapView mapView) {
	        // TODO Auto-generated method stub
	        super.onTap(pt, mapView);
	        return false;
	    }

	}
}
