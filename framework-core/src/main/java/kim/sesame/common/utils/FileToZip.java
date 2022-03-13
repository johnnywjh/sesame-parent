package kim.sesame.common.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 程序实现了ZIP压缩。共分为2部分 ： 压缩（compression）与解压（decompression）
 * <p>
 * 大致功能包括用了多态，递归等JAVA核心技术，可以对单个文件和任意级联文件夹进行压缩和解压。 需在代码中自定义源输入路径和目标输出路径。
 * <p>
 * 在本段代码中，实现的是压缩部分；解压部分见本包中Decompression部分。
 *
 * 
 */
public class FileToZip {
	private int k = 1; // 定义递归次数变量

	public FileToZip() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileToZip book = new FileToZip();
		try {
			String outPath = "E:/myeclipseSpace/codeTool/src/main/webapp/aaa/1459480994234.zip";
			book.zip(outPath, new File("E:/myeclipseSpace/codeTool/src/main/webapp/aaa/1459480994234"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 压缩文件
	 * @param zipFileName 输出文件的位置
	 * @param inputFile inputFile
	 * @throws Exception 异常
	 */
	public void zip(String zipFileName, File inputFile) throws Exception {
		System.out.println("压缩中...");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // 输出流关闭
		System.out.println("压缩完成");
	}

	public void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // 方法重载
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
				System.out.println(base + "/");
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
			}
			System.out.println("第" + k + "次递归");
			k++;
		} else {
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // 将字节流写入当前zip目录
			}
			bi.close();
			in.close(); // 输入流关闭
		}
	}
	
	 public static void main1(String[] args) {  
	        // TODO Auto-generated method stub  
	        long startTime=System.currentTimeMillis();  
	        try {  
	            ZipInputStream Zin=new ZipInputStream(new FileInputStream(  
	                    "C:\\Users\\HAN\\Desktop\\stock\\SpectreCompressed.zip"));//输入源zip路径  
	            BufferedInputStream Bin=new BufferedInputStream(Zin);  
	            String Parent="C:\\Users\\HAN\\Desktop"; //输出路径（文件夹目录）  
	            File Fout=null;  
	            ZipEntry entry;  
	            try {  
	                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){  
	                    Fout=new File(Parent,entry.getName());  
	                    if(!Fout.exists()){  
	                        (new File(Fout.getParent())).mkdirs();  
	                    }  
	                    FileOutputStream out=new FileOutputStream(Fout);  
	                    BufferedOutputStream Bout=new BufferedOutputStream(out);  
	                    int b;  
	                    while((b=Bin.read())!=-1){  
	                        Bout.write(b);  
	                    }  
	                    Bout.close();  
	                    out.close();  
	                    System.out.println(Fout+"解压成功");      
	                }  
	                Bin.close();  
	                Zin.close();  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        long endTime=System.currentTimeMillis();  
	        System.out.println("耗费时间： "+(endTime-startTime)+" ms");  
	    }  
	  
}
