package com.ewan;

/*
 *  @Author Ewan
 *  @Date  2023/12/4
 *  @Description:
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CrawlerTest {

//    @Test
//    public void testFetchPostData() {

//        //1.获取数据
//        String json = "{\n" +
//                "  \"current\": 1,\n" +
//                "  \"pageSize\": 8,\n" +
//                "  \"sortField\": \"createTime\",\n" +
//                "  \"sortOrder\": \"descend\",\n" +
//                "  \"category\": \"文章\",\n" +
//                "  \"reviewStatus\": 1\n" +
//                "}";
//        String result = HttpRequest.post("https://www.code-nav.cn/api/post/search/page/vo")
//                .body(json).execute().body();
//        Map data = JSONUtil.toBean(result, Map.class);
//        JSONObject jsonObject = (JSONObject) data.get("data");
//        JSONArray records = (JSONArray) jsonObject.get("records");
//        List<Post> postList = new ArrayList<>();
//        for (Object record : records) {
//            JSONObject tempRecord = (JSONObject) record;
//            Post post = new Post();
//            post.setTitle(tempRecord.getStr("title"));
//            post.setContent(tempRecord.getStr("content"));
//            JSONArray tags = (JSONArray) tempRecord.get("tags");
//            List<String> list = tags.toList(String.class);
//            post.setTags(JSONUtil.toJsonStr(list));
//            post.setUserId(1L);
//            postList.add(post);
//        }
//        boolean res = postService.saveBatch(postList);
//        Assert.isTrue(res);
//        System.out.println(result);
//    }
//
//    @Test
//    public void testFetchPhoto() throws IOException {
//        int current = 1;
//        String utl = "https://cn.bing.com/images/search?q=头像&qs=MM&form=QBIR&sp=1&lq=0&pq=touxi&sc=10-5&cvid=94DC11E674334FF5AE6ED1C17FC953B5&ghsh=0&ghacc=0&first=" + current;
//        Document doc = Jsoup.connect(utl).get();
//        Elements elements = doc.select(".iuscp.isv");
//        List<Picture> pictures = new ArrayList<>();
//        for (Element element : elements) {
//            // 取图片地址（murl）
//            String m = element.select(".iusc").get(0).attr("m");
//            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
//            String murl = (String) map.get("murl");
//            System.out.println(murl);
//            // 取标题
//            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);
//            Picture picture = new Picture();
//            picture.setTitle(title);
//            picture.setUrl(murl);
//            pictures.add(picture);
//        }
//        System.out.println(pictures);
//    }

//    public static void main(String[] args) {
//        String searchName = "运动鞋";
//        String utl = "https://s.taobao.com/search?commend=all&ie=utf8&initiative_id=tbindexz_20170306&page=1&q=" + searchName + "&search_type=item&sourceId=tb.index&spm=a21bo.jianhua%2Fa.201856.d13&ssid=s5-e&tab=all";
//        try {
//            Document doc = Jsoup.connect(utl).get();
//            System.out.println(1);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
