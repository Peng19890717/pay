package com.third.swt.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.FactoryConfigurationError;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class XMLConvertor
{
  public static Map xml2Map(String receiveMsg)
  {
    Map rtnMap = new HashMap();
    try
    {
      SAXReader reader = new SAXReader();
      Document doc = reader.read(new InputSource(new StringReader(receiveMsg)));
      Element root = doc.getRootElement();
      rtnMap.put("retCode", root.attributeValue("retCode"));
      String xml = "retCode=" + root.attributeValue("retCode");
      rtnMap.put("retMsg", root.attributeValue("retMsg"));
      xml = xml + "&retMsg=" + root.attributeValue("retMsg");
      if (root.attributeValue("retCode").equals("0001"))
        if (root.getName().equals("orderPkg")) {
          List odrArrList = new ArrayList();
          List odrList = root.elements("order");
          for (int i = 0; i < odrList.size(); i++) {
            Map odrArrMap = new HashMap();
            Element ordElement = (Element)odrList.get(i);
            Iterator ordElementAtr = ordElement.attributeIterator();
            while (ordElementAtr.hasNext()) {
              Attribute attr = (Attribute)ordElementAtr.next();
              if (attr != null) {
                odrArrMap.put(attr.getName(), attr.getText());
              }
              odrArrList.add(odrArrMap);
            }
          }
          rtnMap.put("odrArrList", odrArrList);
        } else {
          List traInfoList = root.elements();
          for (int i = 0; i < traInfoList.size(); i++) {
            Element temp = (Element)traInfoList.get(i);
            if (temp.getNodeType() == 1) {
              Element tempElement = temp;
              if (!tempElement.getName().equals("signature")) {
                xml = "&" + tempElement.getName() + "=" + tempElement.getText();
              }
              rtnMap.put(tempElement.getName(), tempElement.getText());
            }
          }
          rtnMap.put("xml", xml);
        }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return rtnMap;
  }

  public static String xml2String(String xml) {
    String msg = "";
    try {
      SAXReader reader = new SAXReader();
      Document doc = reader.read(new InputSource(new StringReader(xml)));
      Element root = doc.getRootElement();
      List traInfoList = root.elements();
      for (int i = 0; i < traInfoList.size(); i++) {
        Element temp = (Element)traInfoList.get(i);
        if (temp.getNodeType() == 1) {
          Element tempElement = temp;
          if (i == 0)
            msg = tempElement.getName() + "=" + tempElement.getText();
          else
            msg = msg + "&" + tempElement.getName() + "=" + tempElement.getText();
        }
      }
    }
    catch (DocumentException e) {
      e.printStackTrace();
    } catch (FactoryConfigurationError e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return msg;
  }
}