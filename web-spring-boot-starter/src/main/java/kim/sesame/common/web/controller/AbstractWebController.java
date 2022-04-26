package kim.sesame.common.web.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * Web controller基类
 **/
public class AbstractWebController extends AbstractController {


    public void download(String fileName, String path, HttpServletRequest request, HttpServletResponse response) {
        try {

            setDownloadResponse(fileName, request, response);

            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = new FileInputStream(path);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            outputStream.write(b);

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDownloadResponse(String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            fileName = getFileName(request, fileName);
            // 实现文件下载
            response.setContentType("text/plain");
            response.setHeader("Location", fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName(HttpServletRequest request, String fileName) {
        try {
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public void jobLogInfo(String str, StringBuffer sb, Log log) {
        log.info(str);
        sb.append("<p>").append(str).append("</p>");
    }

    public void jobLogDebug(String str, StringBuffer sb, Log log) {
        log.debug(str);
        sb.append("<p>").append(str).append("</p>");
    }

    /**
     * 表格数据导出
     *
     * @param fileName 下载的文件名
     * @param list     数据
     * @param clazz    数据类型
     * @param request  req
     * @param response res
     */
    public void tableDataExport(String fileName, List list, Class clazz, HttpServletRequest request, HttpServletResponse response) {

        try {
            // 这里注意 有同学反应下载的文件名不对。这个时候 请别使用swagger 他会有影响
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("1").doWrite(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
