package com.tristan.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.tristan.web.model.Url;
import com.tristan.web.mongodb.MongoKit;
import com.tristan.web.mongodb.MongoQuery;

public class IndexAction extends Controller{
	@ActionKey("/")
	public void index() {
		render("Index.jsp");
	}
	@ActionKey("/search")
	public void search() {
		String key = getPara("key");
		if (StrKit.isBlank(key)) {
			render("Index.jsp");return;
		}
		String[] keys = key.split(" ");
//		String reduce = "function(doc, aggr){" + "aggr.count += 1;" + "        }";
		
		MongoQuery query = new MongoQuery();
		for (String k : keys) {
			query.like("token",k);
		}
		List<DBObject> dbObjects = MongoKit.findByQuery("tokens", query, 10);
		List<Url> datas = proccess(dbObjects);
		for (Url  url : datas) {
			List<DBObject> urls = MongoKit.findByQuery("URLID", new MongoQuery().set("URLID", url.getUrlId()), 1);
			DBObject _url = urls.get(0);
			String uv = (String)_url.get("URL");
			url.setUrl(uv);
		}
        setAttr("data", datas);
        keepPara();
		render("Index.jsp");
	}
	
	private List<Url> proccess(List<DBObject> results) {
		Map<Integer, Double> datas = new TreeMap<>();
		for (DBObject dbObject : results) {
			BasicDBList list = (BasicDBList)dbObject.get("URLs");
			for (Object object : list) {
				DBObject object2 = (DBObject)object;
				Integer uriId = (Integer)object2.get("URL");
				Double score = (Double)object2.get("TFIDF");
				Double o = datas.get(uriId);
				if (o == null) {
					datas.put(uriId, score);
				}else {
					datas.put(uriId, score + o);
				}
			}
		}
		List<Url> result = new ArrayList<>();
		for (Map.Entry<Integer,Double> entry : datas.entrySet()) {
			Integer _key = entry.getKey();
			Double _value = entry.getValue();
			Url url = new Url("",_key,_value);
			result.add(url);
		}
		Collections.sort(result);
		Collections.reverse(result);;
		return result.subList(0, 5);
	}
}
