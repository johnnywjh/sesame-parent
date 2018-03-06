package kim.sesame.framework.utils;

/**
 * 数组 工具
 * 
 * @author johnny
 * date :  2016年1月8日 下午9:18:51
 */
public class ArrUtils {

	/**
	 * 把一个数组变成一个String {2,3,4} == "2,3,4"
	 * 
	 * @author johnny
	 * date :  2016年1月8日 下午9:20:51
	 * @param arr 待处理的数组
	 * @return String 处理之后的字符串
	 */
	public static String formateArr(Object[] arr) {
		StringBuilder sb = new StringBuilder();
		for (Object i : arr) {
			sb.append(i).append(",");
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	/**
	 * 直接选择排序
	 * @param arr  待处理的字符串
	 */
	public static void selectSort(Integer[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < arr.length; j++) {
				// if(arr[i]>arr[j]){
				// Integer tmp = arr[i];
				// arr[i] = arr[j];
				// arr[j] = tmp;
				// }

				// 找出最小值的索引
				if (arr[minIndex] > arr[j]) {
					minIndex = j;
				}
			}
			//每次比较最多交换一次
			if(i!=minIndex){
				Integer tmp = arr[i];
				arr[i] = arr[minIndex];
				arr[minIndex] = tmp;
			}
		}
	}

	public static void main(String[] args) {
		Integer[] arr = { 21, 30, 2, 45, 21, 56, 23, 76, 34, 12 };
		System.out.println(formateArr(arr));
		selectSort(arr);
		System.out.println(formateArr(arr));
	}

}
