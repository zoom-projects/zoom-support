package com.hb0730.zoom.base.utils;

import com.hb0730.zoom.base.pool.RegexPool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 图标工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/12/21
 */
public class FaviconUtil {

    /**
     * 查找图标
     *
     * @param url url
     * @return 图标地址
     */
    public static String findFavicon(String url) {
        //判断是否是合法的url
        if (!RegexUtil.isMatch(RegexPool.URL_HTTP, url)) {
            return null;
        }
        try {
            Document document = Jsoup.connect(url).get();
            // 解析HTML 查询<link rel="icon" href="favicon.ico" />
            String favicon = document.select("link[rel~=(?i)^(shortcut|icon)$]").attr("href");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTMl 查询<link ref="shortcut icon" href="favicon.ico" />
            favicon = document.select("link[rel~=shortcut icon]").attr("href");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTML 查询<meta property="og:image" content="favicon.ico" />
            favicon = document.select("meta[property=og:image]").attr("content");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTML 查询<meta name="msapplication-TileImage" content="favicon.ico" />
            favicon = document.select("meta[name=msapplication-TileImage]").attr("content");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTML 查询<meta name="apple-touch-icon" content="favicon.ico" />
            favicon = document.select("meta[name=apple-touch-icon]").attr("content");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTML 查询<meta name="apple-touch-icon-precomposed" content="favicon.ico" />
            favicon = document.select("meta[name=apple-touch-icon-precomposed]").attr("content");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTMl 查询是否存在logo.* 的图片
            favicon = document.select("img[src~=logo.*]").attr("src");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTMl 查询是否存在icon.* 的图片
            favicon = document.select("img[src~=icon.*]").attr("src");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            // 解析HTMl 查询是否存在favicon.* 的图片
            favicon = document.select("img[src~=favicon.*]").attr("src");
            favicon = fillUrl(url, favicon);
            if (StrUtil.isNotBlank(favicon)) {
                return favicon;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static String fillUrl(String url, String favicon) {
        if (StrUtil.isNotBlank(favicon)) {
            //判断是否是合法的url
            if (RegexUtil.isMatch(RegexPool.URL_HTTP, favicon)) {
                return favicon;
            }
            if (favicon.startsWith("//")) {
                return "http:" + favicon;
            }
            if (favicon.startsWith("/")) {
                return url + favicon;
            }
        }
        return null;
    }
}
