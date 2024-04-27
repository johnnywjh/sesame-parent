package kim.sesame.common.web.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Web controller基类
 **/
@Slf4j
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
            log.error("文件下载异常:", e);
        }
    }

    public void setDownloadResponse(String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            fileName = getFileName(request, fileName);
            // 实现文件下载
            response.setContentType("text/plain");
            response.setHeader("Location", fileName);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        } catch (Exception e) {
            log.error("设置文件名称异常:", e);
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
            log.error("获取文件名称异常:", e);
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

    public void tableDataExport(String fileName, List list, Class clazz, HttpServletResponse response) {
        tableDataExport(fileName, list, clazz, response, "1");
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
    public void tableDataExport(String fileName, List list, Class clazz, HttpServletResponse response, String sheetName) {

        try {
            // 这里注意 有同学反应下载的文件名不对。这个时候 请别使用swagger 他会有影响
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

            // 吧设置的文件名称暴露给前端
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

            // 对文件名称进行URI编码
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName());
            // 修改字符编码
//            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);

            // 文件的处理方式。 attachment 表示附件，filename 表示文件的名称
            // ttachment; filename*=UTF-8''aaaa.xlsx
//            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition
//                    .attachment() 			// 附件形式
//                    .filename(fileName, StandardCharsets.UTF_8)  // 文件名称 & 编码
//                    .build()
//                    .toString());

            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName).doWrite(list);

        } catch (Exception e) {
            log.error("表格导出异常异常 {}:", fileName, e);
        }
    }
}
