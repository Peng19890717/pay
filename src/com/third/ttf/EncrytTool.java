package com.third.ttf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class EncrytTool
{
  public static String getEncrySource(Object obj, List<String> execludeParams)
  {
    Map map = objectToMap(obj);
    return getEncrySource(map, execludeParams);
  }

  public static String getEncrySource(Map<String, String> paramMap, List<String> execludeParams)
  {
    if ((execludeParams == null) || (execludeParams.isEmpty())) {
      return getEncrySource(paramMap);
    }

    String exeParamName = null;
    for (int i = 0; i < execludeParams.size(); i++) {
      exeParamName = (String)execludeParams.get(i);
      if (paramMap.containsKey(exeParamName)) {
        paramMap.remove(exeParamName);
      }

    }

    return getEncrySource(paramMap);
  }

  public static String getEncrySource(List<String> datas, List<String> execludeParams)
  {
    if ((execludeParams == null) || (execludeParams.isEmpty())) {
      return getEncrySource(datas);
    }

    List targetPrams = new ArrayList();
    String val = null;
    String[] pair = null;
    for (int i = 0; i < datas.size(); i++) {
      val = (String)datas.get(i);

      if ((val != null) && (!"".equals(val.trim())))
      {
        pair = val.split("=");
        if ((pair == null) || (pair.length <= 1) || (!execludeParams.contains(pair[0])))
        {
          targetPrams.add(val);
        }
      }
    }
    return getEncrySource(targetPrams);
  }

  public static String getEncrySource(List<String> datas)
  {
    if ((datas == null) || (datas.isEmpty())) {
      return "";
    }

    List pamaPairs = new ArrayList();
    String val = null;
    for (int i = 0; i < datas.size(); i++) {
      val = (String)datas.get(i);

      if ((val != null) && (!"".equals(val.trim())))
      {
        if ((!val.startsWith("sign_type=")) && (!val.startsWith("sign=")))
        {
          pamaPairs.add(val);
        }
      }
    }
    Collections.sort(pamaPairs);

    return StringUtils.join(pamaPairs, '&');
  }

  public static String getEncrySource(Map<String, String> paramMap)
  {
    if ((paramMap == null) || (paramMap.isEmpty())) {
      return "";
    }

    List dataPair = new ArrayList();
    Iterator keyIterator = paramMap.keySet().iterator();
    String key = null;
    String val = null;
    while (keyIterator.hasNext()) {
      key = (String)keyIterator.next();
      val = (String)paramMap.get(key);
      if ((val != null) && (!"".equals(val.trim())))
      {
        dataPair.add(key + "=" + val);
      }
    }
    return getEncrySource(dataPair);
  }

  public static String getEncrySource(Object obj)
  {
    Map map = objectToMap(obj);
    return getEncrySource(map);
  }

  private static Map<String, String> objectToMap(Object obj) {
    Map map = new HashMap();
    String jsonString = JSON.toJSONString(obj);
    JSONObject jsonObj = JSON.parseObject(jsonString);
    Set entrySet = jsonObj.entrySet();
    Iterator iter = entrySet.iterator();
    Map.Entry entry = null;
    while (iter.hasNext()) {
      entry = (Map.Entry)iter.next();
      map.put((String)entry.getKey(), entry.getValue().toString());
    }
    return map;
  }
}