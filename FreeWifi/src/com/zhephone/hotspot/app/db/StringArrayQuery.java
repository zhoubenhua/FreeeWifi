/**
 *
 *项目名：LuluYouLib
 *包名：com.luluyou.android.lib.db
 *文件名：StringArrayQuery.java
 *版本信息：1.0.0
 *创建日期：2013年12月10日-上午10:59:00
 *创建人：jerry.deng
 *Copyright (c) 2013上海路路由信息科技有限公司-版权所有
 * 
 */
package com.zhephone.hotspot.app.db;

import com.zhephone.hotspot.app.db.WifiDatabseAccess.QueryResultHandler;

import android.database.Cursor;

/**
 *
 * 类名称：StringArrayQuery
 * 类描述：
 * 创建人： Jerry.deng
 * 修改人： Jerry.deng
 * 修改时间： 2013年12月10日 上午10:59:00
 * 修改备注：
 * @version 1.0.0
 *
 */
public class StringArrayQuery implements QueryResultHandler<String[]> {

    /* (non-Javadoc)
     * @see com.luluyou.android.lib.db.QueryResultHandler#handle(android.database.Cursor, int)
     */
    @Override
    public String[] handle(Cursor cursor, int numOfCols) {
        String[] record = new String[numOfCols];
        for(int i = 0; i < numOfCols; ++i) {
            record[i] = cursor.getString(i);
        }
        return record;
    }

}
