package com.zhephone.hotspot.app.adapter;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspoti.app.bean.WifiItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WifiHotListAdapter extends BaseAdapter {
	private List<WifiItem> wifiHotspotList;
	private LayoutInflater inflater;
	private Context mContext;
	public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;
	public WifiHotListAdapter(List<WifiItem> wifiHotList, Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.wifiHotspotList = wifiHotList;		
	}
	
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();  
	}

	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView =  inflater.inflate(R.layout.wifi_hotspot_item, null);  
		TextView hotspotNameTv = (TextView) convertView.findViewById(R.id.wifi_ssid_name_tv);
		TextView hotspotConnectStatuTv = (TextView) convertView.findViewById(R.id.wifi_hotspot_connect_statu_tv);
		WifiItem wifiHotspot = wifiHotspotList.get(position);
		if(StringUtils.isNotEmpty(wifiHotspot.getSsidName())) {
			hotspotNameTv.setText(wifiHotspot.getSsidName());
		}
		if(wifiHotspot.getConnectStatu() == Constant.WifiIntentDef.WIFI_CONNECT_SUCCESS) {
			hotspotConnectStatuTv.setText(mContext.getResources().getString(R.string.connected_network));
		} else {
			int security = wifiHotspot.getSecurity();
			switch(security) {
			case SECURITY_WEP:
				hotspotConnectStatuTv.setText("通过"+"WEP"+"进行保护");
				break;
			case SECURITY_PSK:
				hotspotConnectStatuTv.setText("通过"+"PSK"+"进行保护");
				break;
			case SECURITY_EAP:
				hotspotConnectStatuTv.setText("通过"+"EAP"+"进行保护");
				break;
			}
		}
		return convertView;
	}
	
	public int getCount() {
		return wifiHotspotList.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	

}
