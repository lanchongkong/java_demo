package com;

import static java.util.regex.Pattern.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YCKJ3465
 * @since 2022/4/22 下午10:43
 */
public class HtmlParser {
    private String startUrl; // 开始采集网址
    String urlContent;
    String ContentArea;
    private String strAreaBegin, strAreaEnd; // 采集区域开始采集字符串和结束采集字符串
    private String stringInUrl, stringNotInUrl;
    String strContent;// 获得的采集内容
    String[] allHtmlParser; // 采集到的所有网址
    private String regex; // 采集规则

    UrlAndTitle urlAndTitle = new UrlAndTitle(); // 存储网址和标题

    public static void main(String[] args) {
        HtmlParser myurl = new HtmlParser("<body", "/body>");
        myurl.getStartUrl("http://www.baidu.com/");
        myurl.getUrlContent();
        myurl.getContentArea();
        myurl.getStringNotInUrl("google");
        myurl.HtmlParser();

    }

    // 初始化构造函数 strAreaBegin 和strAreaEnd

    public HtmlParser(String strAreaBegin, String strAreaEnd) {
        this.strAreaBegin = strAreaBegin;
        this.strAreaEnd = strAreaEnd;
    }

    //
    public void HtmlParser() {
        int i = 0;
        String regex = "<a.*?/a>";
        Pattern pt = compile(regex);
        Matcher mt = pt.matcher(ContentArea);
        while (mt.find()) {
            System.out.println(mt.group());
            i++;

            // 获取标题
            Matcher title = compile(">.*?</a>").matcher(mt.group());
            while (title.find()) {
                System.out.println("标题:" + title.group().replaceAll(">|</a>", ""));
            }

            // 获取网址
            Matcher myurl = compile("href=.*?>").matcher(mt.group());
            while (myurl.find()) {
                System.out.println("网址:" + myurl.group().replaceAll("href=|>", ""));
            }
            System.out.println();
        }
        System.out.println("共有" + i + "个符合结果");
    }

    // 获得开始采集网址
    public void getStartUrl(String startUrl) {
        this.startUrl = startUrl;
    }

    // 获得网址所在内容;
    public void getUrlContent() {

        StringBuffer is = new StringBuffer();
        try {
            URL myUrl = new URL(startUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(myUrl.openStream()));

            String s;
            while ((s = br.readLine()) != null) {
                is.append(s);
            }
            urlContent = is.toString();
        } catch (Exception e)

        {
            System.out.println("网址文件未能输出");
            e.printStackTrace();
        }

    }

    // 获得网址所在的匹配区域部分
    public void getContentArea() {
        int pos1 = 0, pos2 = 0;
        pos1 = urlContent.indexOf(strAreaBegin) + strAreaBegin.length();
        pos2 = urlContent.indexOf(strAreaEnd, pos1);
        ContentArea = urlContent.substring(pos1, pos2);
    }

    // 以下两个函数获得网址应该要包含的关键字及不能包含的关键字
    // 这里只做初步的实验。后期，保护的关键字及不能包含的关键字应该是不只一个的。
    public void getStringInUrl(String stringInUrl) {
        this.stringInUrl = stringInUrl;

    }

    public void getStringNotInUrl(String stringNotInUrl) {
        this.stringNotInUrl = stringNotInUrl;
    }

    // 获取url网址
    public void getUrl() {
    }

    public String getRegex() {
        return regex;
    }

    class UrlAndTitle {
        String myURL;
        String title;
    }
}