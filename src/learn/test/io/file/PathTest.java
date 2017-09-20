package learn.test.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * java7新增加的Path接口测试
 * @Date 2017-6-11下午12:56:41
 */
public class PathTest {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		/*Path p = Paths.get(".");
		System.out.println(p.getFileName());
		//绝对路径
		Path absPath = p.toAbsolutePath();
		System.out.println(absPath);
		//获取根路径和父目录
		System.out.println(absPath.getRoot()+"----"+absPath.getParent());
		//得到路径的个数
		System.out.println(absPath.getNameCount());
		
		System.out.println(System.getProperty("user.dir"));*/
		
		//复制文件XStream.xml到test.txt文件
		/*Files.copy(Paths.get(System.getProperty("user.dir"), "XStream.xml"), 
				new FileOutputStream(new File("test.txt")));*/
		
		/*Path path = Paths.get("f:\\");
		System.out.println(p.isAbsolute());
		FileSystem fs = FileSystems.getDefault();
		System.out.println(fs);*/
		//find(path);
		
		
		//遍历文件
		//Files.walkFileTree(Paths.get("f:\\learn"), new FindFileVisitor("txt"));
		
		/*************以下实现文件的增删改查***************/
		//创建文件，不能创建目录。当文件存在时抛出异常
		/*Path path = Paths.get("f:\\test.txt");
		if (!Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
			Files.createFile(path);
			System.out.println("创建了文件："+path);
		}*/
		//删除文件和目录，当删除目录时，该目录必须为空，否则抛出异常
		//Files.delete(path);
		//创建多级目录
		//Files.createDirectories(Paths.get("f:\\test\\a\\a_1"));
	}
	
	//实现遍历某个路径的所有文件，包括隐藏文件
	public static void find(Path path) {
		//路径存在
		LinkOption[] links = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
		if (Files.exists(path, links)) {
			//得到该目录下的所有第一级子目录，包含隐藏的文件。
			try (DirectoryStream<Path> dir = Files.newDirectoryStream(path)) {
				for (Path p : dir) {
					StringBuilder fileDesc = new StringBuilder();
					if (Files.isDirectory(p, links)) {
						fileDesc.append("这是一个目录,名称："+p.getFileName());
					} else {
						fileDesc.append("这是一个目录,名称："+p.getFileName());
						fileDesc.append("，文件大小："+Files.size(p)+"字节");
					}
					System.out.println(fileDesc.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

//查找文件的文件访问类
class FindFileVisitor extends SimpleFileVisitor<Path> {
	//文件扩展名
	private String extName;

	public FindFileVisitor(String extName) {
		this.extName = extName;
	}
	
	//访问文件调用的方法
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		//以指定的扩展名结束
		if (file.toString().endsWith("."+extName)) {
			System.out.println("查找到扩展名为【"+extName+"】的文件："+file);
		}
		return FileVisitResult.CONTINUE;
	}
}

class InnerFileVisitor implements FileVisitor<Path> {

	//访问目录前要调用的方法，访问文件不会调用该方法
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		System.out.println("aaaaaaa--preVisitDirectory："+dir);
		return FileVisitResult.CONTINUE;
	}

	//访问文件要调用的方法
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		System.out.println("bbbbbbbbbb--visitFile："+file.getFileName());
		return FileVisitResult.CONTINUE;
	}
	
	//方法目录后要调用的方法，访问文件不会调用该方法
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		System.out.println("ddddddddd--postVisitDirectory："+dir.getFileName());
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		System.out.println("cccccccccc--visitFileFailed");
		return null;
	}

}
